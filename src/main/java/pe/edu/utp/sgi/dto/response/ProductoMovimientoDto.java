package pe.edu.utp.sgi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMovimientoDto {
    private String nombreProducto;
    private Long cantidadMovimientos; // Sum of quantities moved
}
