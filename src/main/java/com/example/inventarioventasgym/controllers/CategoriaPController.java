package com.example.inventarioventasgym.controllers;

import com.example.inventarioventasgym.dto.CategoriaPCreateDTO;
import com.example.inventarioventasgym.dto.CategoriaPResponseDTO;
import com.example.inventarioventasgym.services.CategoriaPService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaPController {

    private final CategoriaPService service;

    public CategoriaPController(CategoriaPService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaPResponseDTO> list() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaPResponseDTO> get(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<CategoriaPResponseDTO> buscarPorNombre(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }

    @PostMapping
    public ResponseEntity<CategoriaPResponseDTO> create(@RequestBody CategoriaPCreateDTO dto) {
        CategoriaPResponseDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaPResponseDTO> update(@PathVariable Integer id, @RequestBody CategoriaPCreateDTO cambios) {
        try {
            CategoriaPResponseDTO updated = service.update(id, cambios);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
