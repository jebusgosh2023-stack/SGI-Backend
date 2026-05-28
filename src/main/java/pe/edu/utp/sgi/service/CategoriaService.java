package pe.edu.utp.sgi.service;

import pe.edu.utp.sgi.dto.request.CategoriaRequest;
import pe.edu.utp.sgi.dto.response.CategoriaResponse;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponse> listarTodas();
    CategoriaResponse obtenerPorId(Long id);
    CategoriaResponse crear(CategoriaRequest request);
    CategoriaResponse actualizar(Long id, CategoriaRequest request);
    void desactivar(Long id);
}
