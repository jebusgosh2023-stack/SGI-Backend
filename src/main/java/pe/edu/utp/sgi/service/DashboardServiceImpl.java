package pe.edu.utp.sgi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sgi.dto.response.DashboardResponse;
import pe.edu.utp.sgi.dto.response.ProductoDashboardDto;
import pe.edu.utp.sgi.dto.response.ProductoMovimientoDto;
import pe.edu.utp.sgi.entity.*;
import pe.edu.utp.sgi.exception.ResourceNotFoundException;
import pe.edu.utp.sgi.repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;
    private final MovimientoRepository movimientoRepository;
    private final StockSucursalRepository stockSucursalRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtenerResumen(String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correoUsuario));

        String rol = usuario.getRol().getNombre().toUpperCase();
        DashboardResponse.DashboardResponseBuilder builder = DashboardResponse.builder();

        if ("ADMIN".equals(rol) || "GERENTE".equals(rol)) {
            builder.totalUsuarios(usuarioRepository.count());
            builder.totalVendedores(usuarioRepository.countByRolNombreIgnoreCase("VENDEDOR"));
            builder.totalAlmaceneros(usuarioRepository.countByRolNombreIgnoreCase("JEFE_ALMACEN"));
            builder.totalSucursales(sucursalRepository.count());

            builder.totalEntradas(movimientoRepository.countByTipo(TipoMovimiento.ENTRADA));
            builder.totalSalidas(movimientoRepository.countByTipo(TipoMovimiento.SALIDA));
            builder.totalTransferencias(movimientoRepository.countByTipo(TipoMovimiento.TRANSFERENCIA));

            List<ProductoDashboardDto> bajoStock = stockSucursalRepository.findProductosBajoStockGlobal().stream()
                    .map(s -> ProductoDashboardDto.builder()
                            .nombre(s.getProducto().getNombre())
                            .sku(s.getProducto().getSku())
                            .categoria(s.getProducto().getCategoria() != null ? s.getProducto().getCategoria().getNombre() : "-")
                            .stockActual(s.getCantidad())
                            .stockMinimo(s.getProducto().getStockMinimo())
                            .sucursal(s.getSucursal().getNombre())
                            .build())
                    .collect(Collectors.toList());
            builder.productosBajoStock(bajoStock);

            Pageable limitTen = PageRequest.of(0, 10);
            List<Object[]> masMovidosData = movimientoRepository.findTopProductosMasMovidosGlobal(
                    List.of(TipoMovimiento.SALIDA), limitTen);
            List<ProductoMovimientoDto> masMovidos = masMovidosData.stream()
                    .map(obj -> ProductoMovimientoDto.builder()
                            .nombreProducto((String) obj[0])
                            .cantidadMovimientos((Long) obj[1])
                            .build())
                    .collect(Collectors.toList());
            builder.productosMasMovidos(masMovidos);

        } else if ("VENDEDOR".equals(rol)) {
            Sucursal sucursal = usuario.getSucursal();
            if (sucursal != null) {
                builder.totalSalidas(movimientoRepository.countByTipoAndSucursalOrigenId(TipoMovimiento.SALIDA, sucursal.getId()));
                builder.totalEntradas(0L);
                builder.totalTransferencias(0L);

                List<ProductoDashboardDto> bajoStock = stockSucursalRepository.findProductosBajoStockPorSucursal(sucursal.getId()).stream()
                        .map(s -> ProductoDashboardDto.builder()
                                .nombre(s.getProducto().getNombre())
                                .sku(s.getProducto().getSku())
                                .categoria(s.getProducto().getCategoria() != null ? s.getProducto().getCategoria().getNombre() : "-")
                                .stockActual(s.getCantidad())
                                .stockMinimo(s.getProducto().getStockMinimo())
                                .sucursal(sucursal.getNombre())
                                .build())
                        .collect(Collectors.toList());
                builder.productosBajoStock(bajoStock);

                Pageable limitFive = PageRequest.of(0, 5);
                List<ProductoMovimientoDto> masStock = stockSucursalRepository.findProductosMasStockPorSucursal(sucursal.getId(), limitFive).stream()
                        .map(s -> ProductoMovimientoDto.builder()
                                .nombreProducto(s.getProducto().getNombre())
                                .cantidadMovimientos(Long.valueOf(s.getCantidad()))
                                .build())
                        .collect(Collectors.toList());
                builder.productosMasMovidos(masStock);
            }
        } else if ("JEFE_ALMACEN".equals(rol)) {
            Sucursal sucursal = usuario.getSucursal();
            if (sucursal != null) {
                builder.totalEntradas(movimientoRepository.countByTipoAndSucursalDestinoId(TipoMovimiento.ENTRADA, sucursal.getId()));
                builder.totalTransferencias(movimientoRepository.countByTipoAndSucursalOrigenId(TipoMovimiento.TRANSFERENCIA, sucursal.getId()));
                builder.totalSalidas(0L);

                List<ProductoDashboardDto> bajoStock = stockSucursalRepository.findProductosBajoStockPorSucursal(sucursal.getId()).stream()
                        .map(s -> ProductoDashboardDto.builder()
                                .nombre(s.getProducto().getNombre())
                                .sku(s.getProducto().getSku())
                                .categoria(s.getProducto().getCategoria() != null ? s.getProducto().getCategoria().getNombre() : "-")
                                .stockActual(s.getCantidad())
                                .stockMinimo(s.getProducto().getStockMinimo())
                                .sucursal(sucursal.getNombre())
                                .build())
                        .collect(Collectors.toList());
                builder.productosBajoStock(bajoStock);

                Pageable limitFive = PageRequest.of(0, 5);
                List<ProductoMovimientoDto> menosStock = stockSucursalRepository.findProductosMenosStockPorSucursal(sucursal.getId(), limitFive).stream()
                        .map(s -> ProductoMovimientoDto.builder()
                                .nombreProducto(s.getProducto().getNombre())
                                .cantidadMovimientos(Long.valueOf(s.getCantidad()))
                                .build())
                        .collect(Collectors.toList());
                builder.productosMasMovidos(menosStock);
            }
        }

        return builder.build();
    }
}
