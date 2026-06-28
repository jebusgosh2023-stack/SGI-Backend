package pe.edu.utp.sgi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sgi.dto.request.ProductoRequest;
import pe.edu.utp.sgi.dto.response.ProductoResponse;
import pe.edu.utp.sgi.entity.Categoria;
import pe.edu.utp.sgi.entity.Producto;
import pe.edu.utp.sgi.entity.StockSucursal;
import pe.edu.utp.sgi.entity.Usuario;
import pe.edu.utp.sgi.exception.BusinessRuleException;
import pe.edu.utp.sgi.exception.ResourceNotFoundException;
import pe.edu.utp.sgi.repository.CategoriaRepository;
import pe.edu.utp.sgi.repository.ProductoRepository;
import pe.edu.utp.sgi.repository.StockSucursalRepository;
import pe.edu.utp.sgi.repository.UsuarioRepository;
import pe.edu.utp.sgi.repository.SucursalRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final StockSucursalRepository stockSucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final SucursalRepository sucursalRepository;

    private Long getSucursalIdSegunRol(Long sucursalIdSolicitada) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        String rol = usuario.getRol().getNombre();
        if ("VENDEDOR".equals(rol) || "JEFE_ALMACEN".equals(rol)) {
            if (usuario.getSucursal() == null) {
                throw new BusinessRuleException("El usuario no tiene una sucursal asignada");
            }
            return usuario.getSucursal().getId();
        }
        return sucursalIdSolicitada; // ADMIN o GERENTE pueden mandar null o un ID específico
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos(Long sucursalId) {
        Long targetSucursalId = getSucursalIdSegunRol(sucursalId);
        if (targetSucursalId != null) {
            return stockSucursalRepository.findBySucursalIdAndProductoActivoTrue(targetSucursalId).stream()
                    .map(this::toResponseWithStock)
                    .collect(Collectors.toList());
        }
        
        List<ProductoResponse> responses = productoRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
                
        return poblarStockTotal(responses);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        ProductoResponse response = toResponse(producto);
        
        List<StockSucursal> stocks = stockSucursalRepository.findByProductoId(id);
        if (!stocks.isEmpty()) {
            // Asumimos la primera sucursal como la asignada (o principal)
            StockSucursal stock = stocks.get(0);
            response.setSucursalId(stock.getSucursal().getId());
            response.setSucursal(stock.getSucursal().getNombre());
        }
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombre(String nombre, Long sucursalId) {
        Long targetSucursalId = getSucursalIdSegunRol(sucursalId);
        if (targetSucursalId != null) {
            return stockSucursalRepository.buscarPorSucursalIdYNombreProducto(targetSucursalId, nombre).stream()
                    .map(this::toResponseWithStock)
                    .collect(Collectors.toList());
        }
        List<ProductoResponse> responses = productoRepository.buscarPorNombre(nombre).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
                
        return poblarStockTotal(responses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarBajoStock() {
        return productoRepository.findProductosBajoStockMinimo().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        // RN-PRD-001: SKU único
        if (productoRepository.existsBySku(request.getSku())) {
            throw new BusinessRuleException("RN-PRD-001: Ya existe un producto con el SKU: " + request.getSku());
        }

        // RN-PRD-002: Categoría activa
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + request.getCategoriaId()));
        if (!categoria.getActiva()) {
            throw new BusinessRuleException("RN-PRD-002: La categoría debe estar activa para asignarla a un producto");
        }

        // RN-PRD-003: precioVenta > precioCompra
        if (request.getPrecioVenta().compareTo(request.getPrecioCompra()) <= 0) {
            throw new BusinessRuleException(
                    "RN-PRD-003: El precio de venta debe ser mayor al precio de compra");
        }

        // RN-PRD-004: stockMinimo <= stockMaximo
        if (request.getStockMinimo() > request.getStockMaximo()) {
            throw new BusinessRuleException(
                    "RN-PRD-004: El stock mínimo debe ser menor o igual al stock máximo");
        }

        pe.edu.utp.sgi.entity.Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal no encontrada con id: " + request.getSucursalId()));

        Producto producto = Producto.builder()
                .sku(request.getSku())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precioCompra(request.getPrecioCompra())
                .precioVenta(request.getPrecioVenta())
                .unidadMedida(request.getUnidadMedida())
                .stockMinimo(request.getStockMinimo())
                .stockMaximo(request.getStockMaximo())
                .activo(true)
                .categoria(categoria)
                .build();

        producto = productoRepository.save(producto);

        StockSucursal stockSucursal = StockSucursal.builder()
                .producto(producto)
                .sucursal(sucursal)
                .cantidad(0)
                .build();
        stockSucursalRepository.save(stockSucursal);

        ProductoResponse response = toResponse(producto);
        response.setSucursalId(sucursal.getId());
        response.setSucursal(sucursal.getNombre());
        return response;
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // RN-PRD-001: SKU único (si cambió)
        if (!producto.getSku().equals(request.getSku())
                && productoRepository.existsBySku(request.getSku())) {
            throw new BusinessRuleException("RN-PRD-001: Ya existe un producto con el SKU: " + request.getSku());
        }

        // RN-PRD-002: Categoría activa
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + request.getCategoriaId()));
        if (!categoria.getActiva()) {
            throw new BusinessRuleException("RN-PRD-002: La categoría debe estar activa para asignarla a un producto");
        }

        // RN-PRD-003: precioVenta > precioCompra
        if (request.getPrecioVenta().compareTo(request.getPrecioCompra()) <= 0) {
            throw new BusinessRuleException(
                    "RN-PRD-003: El precio de venta debe ser mayor al precio de compra");
        }

        // RN-PRD-004: stockMinimo <= stockMaximo
        if (request.getStockMinimo() > request.getStockMaximo()) {
            throw new BusinessRuleException(
                    "RN-PRD-004: El stock mínimo debe ser menor o igual al stock máximo");
        }

        pe.edu.utp.sgi.entity.Sucursal nuevaSucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sucursal no encontrada con id: " + request.getSucursalId()));

        producto.setSku(request.getSku());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setStockMaximo(request.getStockMaximo());
        producto.setCategoria(categoria);

        producto = productoRepository.save(producto);

        java.util.Optional<StockSucursal> stockOpt = stockSucursalRepository.findByProductoAndSucursal(producto, nuevaSucursal);
        if (stockOpt.isEmpty()) {
            StockSucursal stockSucursal = StockSucursal.builder()
                    .producto(producto)
                    .sucursal(nuevaSucursal)
                    .cantidad(0)
                    .build();
            stockSucursalRepository.save(stockSucursal);
        }

        ProductoResponse response = toResponse(producto);
        response.setSucursalId(nuevaSucursal.getId());
        response.setSucursal(nuevaSucursal.getNombre());
        return response;
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        // RN-PRD-005: Productos se desactivan, nunca se eliminan
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    private ProductoResponse toResponse(Producto p) {
        return ProductoResponse.builder()
                .id(p.getId())
                .sku(p.getSku())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precioCompra(p.getPrecioCompra())
                .precioVenta(p.getPrecioVenta())
                .unidadMedida(p.getUnidadMedida())
                .stockMinimo(p.getStockMinimo())
                .stockMaximo(p.getStockMaximo())
                .activo(p.getActivo())
                .categoria(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
                .categoriaId(p.getCategoria() != null ? p.getCategoria().getId() : null)
                .build();
    }

    private ProductoResponse toResponseWithStock(StockSucursal s) {
        ProductoResponse res = toResponse(s.getProducto());
        res.setStockActual(s.getCantidad());
        res.setSucursalId(s.getSucursal().getId());
        res.setSucursal(s.getSucursal().getNombre());
        return res;
    }

    private List<ProductoResponse> poblarStockTotal(List<ProductoResponse> responses) {
        List<Object[]> totales = stockSucursalRepository.sumarStockPorProductoActivo();
        Map<Long, Integer> mapaTotales = totales.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Number) row[1]).intValue()
                ));
        
        responses.forEach(r -> {
            r.setStockActual(mapaTotales.getOrDefault(r.getId(), 0));
        });
        
        return responses;
    }
}
