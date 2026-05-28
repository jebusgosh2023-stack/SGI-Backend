package pe.edu.utp.sgi.service;

import pe.edu.utp.sgi.dto.request.UsuarioRequest;
import pe.edu.utp.sgi.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponse> listarTodos();
    UsuarioResponse obtenerPorId(Long id);
    UsuarioResponse crear(UsuarioRequest request);
    UsuarioResponse actualizar(Long id, UsuarioRequest request);
    void desactivar(Long id);
}
