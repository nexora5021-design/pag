package com.co.nexora.pag.repository;

import com.co.nexora.pag.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
