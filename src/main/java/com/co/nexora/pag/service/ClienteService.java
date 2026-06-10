package com.co.nexora.pag.service;

import com.co.nexora.pag.model.Cliente;
import com.co.nexora.pag.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Page<Cliente> listarPaginado(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public Page<Cliente> buscarPorNombre(String nombre, int page, int size) {
        return repository.findByNombreContainingIgnoreCase(nombre, PageRequest.of(page, size));
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Cliente crear(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente actualizar(Long id, Cliente cliente) {
        cliente.setId(id);
        return repository.save(cliente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
