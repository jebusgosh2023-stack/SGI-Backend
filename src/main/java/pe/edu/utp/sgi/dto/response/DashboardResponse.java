package pe.edu.utp.sgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    // Totales Globales (Solo para Admin/Gerente)
    private Long totalUsuarios;
    private Long totalVendedores;
    private Long totalAlmaceneros;
    private Long totalSucursales;

    // Totales de Movimientos (Globales o por Sucursal según rol)
    private Long totalEntradas;
    private Long totalSalidas;
    private Long totalTransferencias;

    // Listas
    private List<ProductoDashboardDto> productosBajoStock;
    private List<ProductoMovimientoDto> productosMasMovidos;
}
