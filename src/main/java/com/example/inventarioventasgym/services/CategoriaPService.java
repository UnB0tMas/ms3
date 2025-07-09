package com.example.inventarioventasgym.services;

import com.example.inventarioventasgym.dto.CategoriaPCreateDTO;
import com.example.inventarioventasgym.dto.CategoriaPResponseDTO;
import com.example.inventarioventasgym.models.CategoriaP;
import com.example.inventarioventasgym.repositories.CategoriaPRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaPService {

    private final CategoriaPRepository repo;

    public CategoriaPService(CategoriaPRepository repo) {
        this.repo = repo;
    }

    public List<CategoriaPResponseDTO> getAll() {
        return repo.findByEstadoTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoriaPResponseDTO> getById(Integer id) {
        return repo.findById(id).filter(CategoriaP::getEstado).map(this::toDTO);
    }

    @Transactional
    public CategoriaPResponseDTO create(CategoriaPCreateDTO dto) {
        CategoriaP cat = new CategoriaP();
        cat.setNombre(dto.getNombre());
        cat.setDescripcion(dto.getDescripcion());
        cat.setEstado(true);
        cat = repo.save(cat);
        return toDTO(cat);
    }

    @Transactional
    public CategoriaPResponseDTO update(Integer id, CategoriaPCreateDTO cambios) {
        return repo.findById(id).map(cat -> {
            cat.setNombre(cambios.getNombre());
            cat.setDescripcion(cambios.getDescripcion());
            return toDTO(repo.save(cat));
        }).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Transactional
    public void delete(Integer id) {
        repo.findById(id).ifPresent(cat -> {
            cat.setEstado(false);
            repo.save(cat);
        });
    }

    public List<CategoriaPResponseDTO> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCaseAndEstadoTrue(nombre)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private CategoriaPResponseDTO toDTO(CategoriaP cat) {
        return new CategoriaPResponseDTO(
                cat.getCategoriaPId(),
                cat.getNombre(),
                cat.getDescripcion(),
                cat.getEstado()
        );
    }
}
