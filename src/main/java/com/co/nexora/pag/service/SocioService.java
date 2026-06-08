package com.co.nexora.pag.service;

import com.co.nexora.pag.model.Socio;
import com.co.nexora.pag.repository.EmpleadoRepository;
import com.co.nexora.pag.repository.SocioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocioService {

    private final SocioRepository repository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public SocioService(SocioRepository repository, EmpleadoRepository empleadoRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Socio> listarTodos() {
        return repository.findAll();
    }

    public Page<Socio> listarPaginado(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public Page<Socio> buscarPorNombre(String nombre, int page, int size) {
        return repository.findByNombreContainingIgnoreCase(nombre, PageRequest.of(page, size));
    }

    public Optional<Socio> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Socio crear(Socio socio) {
        if (socio.getUsuario() != null) {
            if (repository.findByUsuarioIgnoreCase(socio.getUsuario()).isPresent()
                    || empleadoRepository.findByUsuarioIgnoreCase(socio.getUsuario()).isPresent()) {
                throw new RuntimeException("El usuario ya existe");
            }
        }
        if (socio.getPassword() != null) {
            socio.setPassword(passwordEncoder.encode(socio.getPassword()));
        }
        return repository.save(socio);
    }

    public Socio actualizar(Long id, Socio socio) {
        socio.setId(id);
        if (socio.getPassword() != null && !socio.getPassword().startsWith("$2a$")) {
            socio.setPassword(passwordEncoder.encode(socio.getPassword()));
        }
        return repository.save(socio);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
