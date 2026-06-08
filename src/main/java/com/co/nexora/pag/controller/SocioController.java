package com.co.nexora.pag.controller;

import com.co.nexora.pag.model.Socio;
import com.co.nexora.pag.service.SocioService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService service;

    public SocioController(SocioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Socio> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/paginado")
    public Page<Socio> listarPaginado(@RequestParam(defaultValue = "") String nombre,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        if (nombre.isBlank()) {
            return service.listarPaginado(page, size);
        }
        return service.buscarPorNombre(nombre, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Socio> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Socio socio) {
        socio.setId(null);
        try {
            return ResponseEntity.ok(service.crear(socio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizar(@PathVariable Long id, @RequestBody Socio socio) {
        return service.buscarPorId(id)
                .map(e -> ResponseEntity.ok(service.actualizar(id, socio)))
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
