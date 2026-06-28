package pe.edu.utp.sgi.service;

import pe.edu.utp.sgi.dto.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse obtenerResumen(String correoUsuario);
}
