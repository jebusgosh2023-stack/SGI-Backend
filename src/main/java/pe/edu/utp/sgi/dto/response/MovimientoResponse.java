package pe.edu.utp.sgi.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoResponse {
    private Long id;
    private String tipo;
    private String producto;
    private String skuProducto;
    private Integer cantidad;
    private String sucursalOrigen;
    private String sucursalDestino;
    private String usuario;
    private String observacion;
    private LocalDateTime fecha;
}
