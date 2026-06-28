package pe.edu.utp.sgi.service;

import pe.edu.utp.sgi.dto.request.ProductoRequest;
import pe.edu.utp.sgi.dto.response.ProductoResponse;

import java.util.List;

public interface ProductoService {
    List<ProductoResponse> listarTodos(Long sucursalId);
    ProductoResponse obtenerPorId(Long id);
    List<ProductoResponse> buscarPorNombre(String nombre, Long sucursalId);
    List<ProductoResponse> listarBajoStock();
    ProductoResponse crear(ProductoRequest request);
    ProductoResponse actualizar(Long id, ProductoRequest request);
    void desactivar(Long id);
}
