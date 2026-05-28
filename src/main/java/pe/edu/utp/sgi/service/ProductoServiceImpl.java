package pe.edu.utp.sgi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sgi.dto.request.ProductoRequest;
import pe.edu.utp.sgi.dto.response.ProductoResponse;
import pe.edu.utp.sgi.entity.Categoria;
import pe.edu.utp.sgi.entity.Producto;
import pe.edu.utp.sgi.exception.BusinessRuleException;
import pe.edu.utp.sgi.exception.ResourceNotFoundException;
import pe.edu.utp.sgi.repository.CategoriaRepository;
import pe.edu.utp.sgi.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toResponse(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return productoRepository.buscarPorNombre(nombre).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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

        return toResponse(productoRepository.save(producto));
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

        producto.setSku(request.getSku());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setStockMaximo(request.getStockMaximo());
        producto.setCategoria(categoria);

        return toResponse(productoRepository.save(producto));
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
}
