package pe.edu.utp.sgi.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponse {
    private Long id;
    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private String unidadMedida;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private Boolean activo;
    private String categoria;
    private Long categoriaId;
}
