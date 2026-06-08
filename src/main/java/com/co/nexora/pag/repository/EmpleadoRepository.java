package com.co.nexora.pag.repository;

import com.co.nexora.pag.model.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Page<Empleado> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Optional<Empleado> findByUsuarioIgnoreCase(String usuario);
}
