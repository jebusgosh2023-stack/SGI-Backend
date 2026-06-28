package pe.edu.utp.sgi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.sgi.dto.response.DashboardResponse;
import pe.edu.utp.sgi.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','JEFE_ALMACEN','VENDEDOR')")
    public ResponseEntity<DashboardResponse> obtenerResumen(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.obtenerResumen(authentication.getName()));
    }
}
