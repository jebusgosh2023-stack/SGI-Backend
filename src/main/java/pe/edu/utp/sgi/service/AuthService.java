package pe.edu.utp.sgi.service;

import pe.edu.utp.sgi.dto.request.LoginRequest;
import pe.edu.utp.sgi.dto.request.RegisterRequest;
import pe.edu.utp.sgi.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
