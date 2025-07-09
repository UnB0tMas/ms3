package com.example.inventarioventasgym.controllers;

import com.example.inventarioventasgym.dto.ProductoCreateDTO;
import com.example.inventarioventasgym.dto.ProductoResponseDTO;
import com.example.inventarioventasgym.services.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoResponseDTO> list() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> get(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<ProductoResponseDTO> buscarPorNombre(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> create(@RequestBody ProductoCreateDTO dto) {
        ProductoResponseDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> update(@PathVariable Integer id, @RequestBody ProductoCreateDTO cambios) {
        try {
            ProductoResponseDTO updated = service.update(id, cambios);
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
