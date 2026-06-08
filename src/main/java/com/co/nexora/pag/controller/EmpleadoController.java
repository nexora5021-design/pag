package com.co.nexora.pag.controller;

import com.co.nexora.pag.model.Empleado;
import com.co.nexora.pag.service.EmpleadoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Empleado> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/paginado")
    public Page<Empleado> listarPaginado(@RequestParam(defaultValue = "") String nombre,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        if (nombre.isBlank()) {
            return service.listarPaginado(page, size);
        }
        return service.buscarPorNombre(nombre, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Empleado empleado) {
        try {
            return ResponseEntity.ok(service.crear(empleado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        return service.buscarPorId(id)
                .map(e -> ResponseEntity.ok(service.actualizar(id, empleado)))
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
