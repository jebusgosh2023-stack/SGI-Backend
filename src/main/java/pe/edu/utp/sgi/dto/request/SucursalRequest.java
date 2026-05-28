package pe.edu.utp.sgi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucursalRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    private String nombre;

    @Size(max = 200, message = "La dirección no debe exceder 200 caracteres")
    private String direccion;

    @Size(max = 80, message = "El distrito no debe exceder 80 caracteres")
    private String distrito;
}
