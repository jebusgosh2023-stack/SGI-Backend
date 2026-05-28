package pe.edu.utp.sgi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sgi.dto.request.SucursalRequest;
import pe.edu.utp.sgi.dto.response.SucursalResponse;
import pe.edu.utp.sgi.entity.Sucursal;
import pe.edu.utp.sgi.exception.ResourceNotFoundException;
import pe.edu.utp.sgi.repository.SucursalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listarTodas() {
        return sucursalRepository.findByActivaTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con id: " + id));
        return toResponse(sucursal);
    }

    @Override
    @Transactional
    public SucursalResponse crear(SucursalRequest request) {
        Sucursal sucursal = Sucursal.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .distrito(request.getDistrito())
                .activa(true)
                .build();
        return toResponse(sucursalRepository.save(sucursal));
    }

    @Override
    @Transactional
    public SucursalResponse actualizar(Long id, SucursalRequest request) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con id: " + id));
        sucursal.setNombre(request.getNombre());
        sucursal.setDireccion(request.getDireccion());
        sucursal.setDistrito(request.getDistrito());
        return toResponse(sucursalRepository.save(sucursal));
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con id: " + id));
        sucursal.setActiva(false);
        sucursalRepository.save(sucursal);
    }

    private SucursalResponse toResponse(Sucursal s) {
        return SucursalResponse.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .distrito(s.getDistrito())
                .activa(s.getActiva())
                .build();
    }
}
