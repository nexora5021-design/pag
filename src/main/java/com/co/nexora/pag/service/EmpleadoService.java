package com.co.nexora.pag.service;

import com.co.nexora.pag.model.Empleado;
import com.co.nexora.pag.repository.EmpleadoRepository;
import com.co.nexora.pag.repository.SocioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repository;
    private final SocioRepository socioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreditoService creditoService;

    public EmpleadoService(EmpleadoRepository repository, SocioRepository socioRepository, PasswordEncoder passwordEncoder, CreditoService creditoService) {
        this.repository = repository;
        this.socioRepository = socioRepository;
        this.passwordEncoder = passwordEncoder;
        this.creditoService = creditoService;
    }

    public List<Empleado> listarTodos() {
        return repository.findAll();
    }

    public Page<Empleado> buscarPorNombre(String nombre, int page, int size) {
        return repository.findByNombreContainingIgnoreCase(nombre, PageRequest.of(page, size));
    }

    public Page<Empleado> listarPaginado(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public Optional<Empleado> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Empleado crear(Empleado empleado) {
        if (empleado.getUsuario() != null) {
            if (repository.findByUsuarioIgnoreCase(empleado.getUsuario()).isPresent()
                    || socioRepository.findByUsuarioIgnoreCase(empleado.getUsuario()).isPresent()) {
                throw new RuntimeException("El usuario ya existe");
            }
        }
        if (empleado.getPassword() != null) {
            empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
        }
        return repository.save(empleado);
    }

    public Empleado actualizar(Long id, Empleado empleado) {
        empleado.setId(id);
        if (empleado.getPassword() != null && !empleado.getPassword().startsWith("$2a$")) {
            empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
        }
        return repository.save(empleado);
    }

    public void eliminar(Long id) {
        creditoService.desasociarPrestamista(id);
        repository.deleteById(id);
    }
}
