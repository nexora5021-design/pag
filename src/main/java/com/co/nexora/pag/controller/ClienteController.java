package com.co.nexora.pag.controller;

import com.co.nexora.pag.model.Cliente;
import com.co.nexora.pag.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cliente> listarTodos(@RequestParam(required = false) Long idEmpleado) {
        if (idEmpleado != null) {
            return service.listarPorPrestamista(idEmpleado);
        }
        return service.listarTodos();
    }

    @GetMapping("/paginado")
    public Page<Cliente> listarPaginado(@RequestParam(defaultValue = "") String nombre,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        if (nombre.isBlank()) {
            return service.listarPaginado(page, size);
        }
        return service.buscarPorNombre(nombre, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente crear(@RequestBody Cliente cliente) {
        return service.crear(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return service.buscarPorId(id)
                .map(e -> ResponseEntity.ok(service.actualizar(id, cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(e -> {
                    service.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
