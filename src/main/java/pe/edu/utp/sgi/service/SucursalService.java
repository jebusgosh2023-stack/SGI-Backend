package pe.edu.utp.sgi.service;

import pe.edu.utp.sgi.dto.request.SucursalRequest;
import pe.edu.utp.sgi.dto.response.SucursalResponse;

import java.util.List;

public interface SucursalService {
    List<SucursalResponse> listarTodas();
    SucursalResponse obtenerPorId(Long id);
    SucursalResponse crear(SucursalRequest request);
    SucursalResponse actualizar(Long id, SucursalRequest request);
    void desactivar(Long id);
}
