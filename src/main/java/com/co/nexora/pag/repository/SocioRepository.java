package com.co.nexora.pag.repository;

import com.co.nexora.pag.model.Socio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    Page<Socio> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Optional<Socio> findByUsuarioIgnoreCase(String usuario);
}
