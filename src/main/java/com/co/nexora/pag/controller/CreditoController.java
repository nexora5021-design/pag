package com.co.nexora.pag.controller;

import com.co.nexora.pag.dto.CreditoPaginadoResponse;
import com.co.nexora.pag.dto.VisionGeneralResponse;
import com.co.nexora.pag.model.Credito;
import com.co.nexora.pag.service.CreditoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    private final CreditoService service;

    public CreditoController(CreditoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Credito> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/paginado")
    public Page<Credito> listarPaginado(@RequestParam(defaultValue = "") String nombre,
                                         @RequestParam(required = false) Long idEmpleado,
                                         @RequestParam(required = false) Long idSocio,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        if (!nombre.isBlank() && idEmpleado != null) {
            return service.buscarPorNombreClienteYPrestamista(nombre, idEmpleado, page, size);
        }
        if (!nombre.isBlank() && idSocio != null) {
            return service.buscarPorNombreClienteYSocio(nombre, idSocio, page, size);
        }
        if (!nombre.isBlank()) {
            return service.buscarPorNombreCliente(nombre, page, size);
        }
        if (idEmpleado != null) {
            return service.listarPorPrestamista(idEmpleado, page, size);
        }
        if (idSocio != null) {
            return service.listarPorSocio(idSocio, page, size);
        }
        return service.listarPaginado(page, size);
    }

    @GetMapping("/por-estado")
    public CreditoPaginadoResponse listarPorEstado(@RequestParam(required = false) String estado,
                                                    @RequestParam(required = false) Long idEmpleado,
                                                    @RequestParam(required = false) Long idSocio,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.listarPorEstadoConContadores(estado, idEmpleado, idSocio, page, size);
    }

    @GetMapping("/cliente/{clienteId}")
    public Page<Credito> buscarPorCliente(@PathVariable Long clienteId,
                                           @RequestParam(required = false) Long idEmpleado,
                                           @RequestParam(required = false) Long idSocio,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return service.buscarPorCliente(clienteId, idEmpleado, idSocio, page, size);
    }

    @GetMapping("/vision-general")
    public VisionGeneralResponse visionGeneral() {
        return service.obtenerVisionGeneral();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credito> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Credito crear(@RequestBody Credito credito) {
        credito.setId(null);
        return service.crear(credito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Credito> actualizar(@PathVariable Long id, @RequestBody Credito credito) {
        return service.buscarPorId(id)
                .map(e -> ResponseEntity.ok(service.actualizar(id, credito)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/movimiento")
    public ResponseEntity<Credito> registrarMovimiento(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        String accion = (String) body.get("accion");
        Double valor = body.get("valor") != null ? ((Number) body.get("valor")).doubleValue() : null;
        return service.registrarMovimiento(id, accion, valor)
                .map(ResponseEntity::ok)
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
