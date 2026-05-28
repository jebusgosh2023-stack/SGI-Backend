package pe.edu.utp.sgi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sgi.dto.request.MovimientoEntradaRequest;
import pe.edu.utp.sgi.dto.request.MovimientoSalidaRequest;
import pe.edu.utp.sgi.dto.request.MovimientoTransferenciaRequest;
import pe.edu.utp.sgi.dto.response.MovimientoResponse;
import pe.edu.utp.sgi.entity.*;
import pe.edu.utp.sgi.exception.BusinessRuleException;
import pe.edu.utp.sgi.exception.ResourceNotFoundException;
import pe.edu.utp.sgi.exception.StockInsuficienteException;
import pe.edu.utp.sgi.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final StockSucursalRepository stockSucursalRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarTodos() {
        return movimientoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarPorSucursalYFecha(
            Long sucursalId, LocalDateTime desde, LocalDateTime hasta) {
        return movimientoRepository.findMovimientosPorSucursalYFecha(sucursalId, desde, hasta)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MovimientoResponse registrarEntrada(
            MovimientoEntradaRequest request, String correoUsuario) {

        // RN-MOV-005: cantidad > 0
        if (request.getCantidad() <= 0) {
            throw new BusinessRuleException("RN-MOV-005: La cantidad del movimiento debe ser mayor a 0");
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + request.getProductoId()));

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal no encontrada con id: " + request.getSucursalId()));

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con correo: " + correoUsuario));

        // RN-INV-004: Actualizar stock
        StockSucursal stock = stockSucursalRepository
                .findByProductoAndSucursal(producto, sucursal)
                .orElseGet(() -> StockSucursal.builder()
                        .producto(producto)
                        .sucursal(sucursal)
                        .cantidad(0)
                        .build());

        stock.setCantidad(stock.getCantidad() + request.getCantidad());
        stockSucursalRepository.save(stock);

        // RN-GEN-003: Registrar movimiento con fecha, hora y usuario
        Movimiento movimiento = Movimiento.builder()
                .tipo(TipoMovimiento.ENTRADA)
                .producto(producto)
                .cantidad(request.getCantidad())
                .sucursalDestino(sucursal)
                .usuario(usuario)
                .observacion(request.getObservacion())
                .build();

        return toResponse(movimientoRepository.save(movimiento));
    }

    @Override
    @Transactional
    public MovimientoResponse registrarSalida(
            MovimientoSalidaRequest request, String correoUsuario) {

        // RN-MOV-005: cantidad > 0
        if (request.getCantidad() <= 0) {
            throw new BusinessRuleException("RN-MOV-005: La cantidad del movimiento debe ser mayor a 0");
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + request.getProductoId()));

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal no encontrada con id: " + request.getSucursalId()));

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con correo: " + correoUsuario));

        // RN-MOV-002 / RN-INV-002: Verificar stock suficiente
        StockSucursal stock = stockSucursalRepository
                .findByProductoAndSucursal(producto, sucursal)
                .orElseThrow(() -> new StockInsuficienteException(
                        "RN-INV-002: No existe stock del producto '" + producto.getNombre()
                                + "' en la sucursal '" + sucursal.getNombre() + "'"));

        if (stock.getCantidad() < request.getCantidad()) {
            throw new StockInsuficienteException(
                    "RN-INV-002: Stock insuficiente. Disponible: " + stock.getCantidad()
                            + ", solicitado: " + request.getCantidad());
        }

        stock.setCantidad(stock.getCantidad() - request.getCantidad());
        stockSucursalRepository.save(stock);

        // RN-INV-003: Alerta cuando stock cae a nivel mínimo o menos
        if (stock.getCantidad() <= producto.getStockMinimo()) {
            log.warn("RN-INV-003: ALERTA - Stock bajo para producto '{}' (SKU: {}) en sucursal '{}'. " +
                            "Stock actual: {}, Stock mínimo: {}",
                    producto.getNombre(), producto.getSku(), sucursal.getNombre(),
                    stock.getCantidad(), producto.getStockMinimo());
        }

        Movimiento movimiento = Movimiento.builder()
                .tipo(TipoMovimiento.SALIDA)
                .producto(producto)
                .cantidad(request.getCantidad())
                .sucursalOrigen(sucursal)
                .usuario(usuario)
                .observacion(request.getObservacion())
                .build();

        return toResponse(movimientoRepository.save(movimiento));
    }

    @Override
    @Transactional
    public MovimientoResponse registrarTransferencia(
            MovimientoTransferenciaRequest request, String correoUsuario) {

        // RN-MOV-005: cantidad > 0
        if (request.getCantidad() <= 0) {
            throw new BusinessRuleException("RN-MOV-005: La cantidad del movimiento debe ser mayor a 0");
        }

        // RN-MOV-003: Origen != Destino
        if (request.getSucursalOrigenId().equals(request.getSucursalDestinoId())) {
            throw new BusinessRuleException(
                    "RN-MOV-003: La sucursal de origen debe ser diferente a la sucursal de destino");
        }

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + request.getProductoId()));

        Sucursal origen = sucursalRepository.findById(request.getSucursalOrigenId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal origen no encontrada con id: " + request.getSucursalOrigenId()));

        Sucursal destino = sucursalRepository.findById(request.getSucursalDestinoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal destino no encontrada con id: " + request.getSucursalDestinoId()));

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con correo: " + correoUsuario));

        // RN-MOV-002 / RN-INV-002: Verificar stock en origen
        StockSucursal stockOrigen = stockSucursalRepository
                .findByProductoAndSucursal(producto, origen)
                .orElseThrow(() -> new StockInsuficienteException(
                        "RN-INV-002: No existe stock del producto '" + producto.getNombre()
                                + "' en la sucursal origen '" + origen.getNombre() + "'"));

        if (stockOrigen.getCantidad() < request.getCantidad()) {
            throw new StockInsuficienteException(
                    "RN-INV-002: Stock insuficiente en origen. Disponible: " + stockOrigen.getCantidad()
                            + ", solicitado: " + request.getCantidad());
        }

        // Descontar de origen
        stockOrigen.setCantidad(stockOrigen.getCantidad() - request.getCantidad());
        stockSucursalRepository.save(stockOrigen);

        // RN-INV-003: Alerta stock bajo en origen
        if (stockOrigen.getCantidad() <= producto.getStockMinimo()) {
            log.warn("RN-INV-003: ALERTA - Stock bajo para producto '{}' (SKU: {}) en sucursal '{}'. " +
                            "Stock actual: {}, Stock mínimo: {}",
                    producto.getNombre(), producto.getSku(), origen.getNombre(),
                    stockOrigen.getCantidad(), producto.getStockMinimo());
        }

        // Agregar a destino
        StockSucursal stockDestino = stockSucursalRepository
                .findByProductoAndSucursal(producto, destino)
                .orElseGet(() -> StockSucursal.builder()
                        .producto(producto)
                        .sucursal(destino)
                        .cantidad(0)
                        .build());

        stockDestino.setCantidad(stockDestino.getCantidad() + request.getCantidad());
        stockSucursalRepository.save(stockDestino);

        Movimiento movimiento = Movimiento.builder()
                .tipo(TipoMovimiento.TRANSFERENCIA)
                .producto(producto)
                .cantidad(request.getCantidad())
                .sucursalOrigen(origen)
                .sucursalDestino(destino)
                .usuario(usuario)
                .observacion(request.getObservacion())
                .build();

        return toResponse(movimientoRepository.save(movimiento));
    }

    private MovimientoResponse toResponse(Movimiento m) {
        return MovimientoResponse.builder()
                .id(m.getId())
                .tipo(m.getTipo().name())
                .producto(m.getProducto().getNombre())
                .skuProducto(m.getProducto().getSku())
                .cantidad(m.getCantidad())
                .sucursalOrigen(m.getSucursalOrigen() != null ? m.getSucursalOrigen().getNombre() : null)
                .sucursalDestino(m.getSucursalDestino() != null ? m.getSucursalDestino().getNombre() : null)
                .usuario(m.getUsuario().getNombre())
                .observacion(m.getObservacion())
                .fecha(m.getFecha())
                .build();
    }
}
