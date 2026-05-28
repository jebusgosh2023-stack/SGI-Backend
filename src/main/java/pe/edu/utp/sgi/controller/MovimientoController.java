package pe.edu.utp.sgi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.sgi.dto.request.MovimientoEntradaRequest;
import pe.edu.utp.sgi.dto.request.MovimientoSalidaRequest;
import pe.edu.utp.sgi.dto.request.MovimientoTransferenciaRequest;
import pe.edu.utp.sgi.dto.response.MovimientoResponse;
import pe.edu.utp.sgi.service.MovimientoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','JEFE_ALMACEN')")
    public ResponseEntity<List<MovimientoResponse>> listarTodos() {
        return ResponseEntity.ok(movimientoService.listarTodos());
    }

    @GetMapping("/sucursal/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','JEFE_ALMACEN')")
    public ResponseEntity<List<MovimientoResponse>> listarPorSucursal(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(movimientoService.listarPorSucursalYFecha(id, desde, hasta));
    }

    @PostMapping("/entrada")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_ALMACEN')")
    public ResponseEntity<MovimientoResponse> registrarEntrada(
            @Valid @RequestBody MovimientoEntradaRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarEntrada(request, authentication.getName()));
    }

    @PostMapping("/salida")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<MovimientoResponse> registrarSalida(
            @Valid @RequestBody MovimientoSalidaRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarSalida(request, authentication.getName()));
    }

    @PostMapping("/transferencia")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_ALMACEN')")
    public ResponseEntity<MovimientoResponse> registrarTransferencia(
            @Valid @RequestBody MovimientoTransferenciaRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarTransferencia(request, authentication.getName()));
    }
}
