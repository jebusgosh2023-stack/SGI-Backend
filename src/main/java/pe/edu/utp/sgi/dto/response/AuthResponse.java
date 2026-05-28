package pe.edu.utp.sgi.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private Long usuarioId;
    private String nombre;
    private String correo;
    private String rol;
}
