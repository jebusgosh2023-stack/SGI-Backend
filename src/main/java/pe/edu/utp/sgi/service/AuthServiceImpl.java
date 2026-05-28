package pe.edu.utp.sgi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sgi.dto.request.LoginRequest;
import pe.edu.utp.sgi.dto.request.RegisterRequest;
import pe.edu.utp.sgi.dto.response.AuthResponse;
import pe.edu.utp.sgi.entity.Rol;
import pe.edu.utp.sgi.entity.Sucursal;
import pe.edu.utp.sgi.entity.Usuario;
import pe.edu.utp.sgi.exception.BusinessRuleException;
import pe.edu.utp.sgi.exception.ResourceNotFoundException;
import pe.edu.utp.sgi.repository.RolRepository;
import pe.edu.utp.sgi.repository.SucursalRepository;
import pe.edu.utp.sgi.repository.UsuarioRepository;
import pe.edu.utp.sgi.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SucursalRepository sucursalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessRuleException("RN-USR-003: Ya existe un usuario con el correo: " + request.getCorreo());
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + request.getRolId()));

        Sucursal sucursal = null;
        if (request.getSucursalId() != null) {
            sucursal = sucursalRepository.findById(request.getSucursalId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sucursal no encontrada con id: " + request.getSucursalId()));
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .activo(true)
                .rol(rol)
                .sucursal(sucursal)
                .build();

        usuario = usuarioRepository.save(usuario);
        String token = jwtService.generarToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(rol.getNombre())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword()));

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con correo: " + request.getCorreo()));

        String token = jwtService.generarToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().getNombre())
                .build();
    }
}
