package pe.edu.utp.sgi.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String correo;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private String rol;
    private String sucursal;
}
