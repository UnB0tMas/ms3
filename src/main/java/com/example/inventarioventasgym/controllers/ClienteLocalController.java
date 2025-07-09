// src/main/java/com/example/inventarioventasgym/controllers/ClienteLocalController.java
package com.example.inventarioventasgym.controllers;

import com.example.inventarioventasgym.models.ClienteLocal;
import com.example.inventarioventasgym.repositories.ClienteLocalRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes-local")
public class ClienteLocalController {

    private final ClienteLocalRepository repo;

    public ClienteLocalController(ClienteLocalRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ClienteLocal> list() {
        return repo.findAll().stream()
                .filter(ClienteLocal::getEstado)
                .toList();
    }
}
