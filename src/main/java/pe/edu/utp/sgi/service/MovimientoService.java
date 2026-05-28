package pe.edu.utp.sgi.service;

import pe.edu.utp.sgi.dto.request.MovimientoEntradaRequest;
import pe.edu.utp.sgi.dto.request.MovimientoSalidaRequest;
import pe.edu.utp.sgi.dto.request.MovimientoTransferenciaRequest;
import pe.edu.utp.sgi.dto.response.MovimientoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    List<MovimientoResponse> listarTodos();
    List<MovimientoResponse> listarPorSucursalYFecha(Long sucursalId, LocalDateTime desde, LocalDateTime hasta);
    MovimientoResponse registrarEntrada(MovimientoEntradaRequest request, String correoUsuario);
    MovimientoResponse registrarSalida(MovimientoSalidaRequest request, String correoUsuario);
    MovimientoResponse registrarTransferencia(MovimientoTransferenciaRequest request, String correoUsuario);
}
