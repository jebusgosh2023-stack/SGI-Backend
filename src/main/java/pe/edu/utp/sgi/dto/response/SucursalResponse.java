package pe.edu.utp.sgi.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucursalResponse {
    private Long id;
    private String nombre;
    private String direccion;
    private String distrito;
    private Boolean activa;
}
