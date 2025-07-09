package com.example.inventarioventasgym.controllers;

import com.example.inventarioventasgym.dto.VentaCreateDTO;
import com.example.inventarioventasgym.dto.VentaResponseDTO;
import com.example.inventarioventasgym.services.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    // Listar todas las ventas (con detalles)
    @GetMapping
    public List<VentaResponseDTO> list() {
        return service.getAll();
    }

    // Obtener una venta por ID (con detalles)
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> get(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear una venta con sus detalles
    @PostMapping
    public ResponseEntity<VentaResponseDTO> create(@RequestBody VentaCreateDTO dto) {
        VentaResponseDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    // Actualizar una venta (incluyendo sus detalles)
    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> update(@PathVariable Integer id, @RequestBody VentaCreateDTO cambios) {
        try {
            VentaResponseDTO updated = service.update(id, cambios);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Anular o eliminar una venta (baja lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
