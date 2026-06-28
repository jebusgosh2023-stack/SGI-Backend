package pe.edu.utp.sgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDashboardDto {
    private String nombre;
    private String sku;
    private String categoria;
    private Integer stockActual;
    private Integer stockMinimo;
    private String sucursal;
}
