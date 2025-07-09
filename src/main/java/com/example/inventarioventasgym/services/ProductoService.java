package com.example.inventarioventasgym.services;

import com.example.inventarioventasgym.dto.ProductoCreateDTO;
import com.example.inventarioventasgym.dto.ProductoResponseDTO;
import com.example.inventarioventasgym.models.CategoriaP;
import com.example.inventarioventasgym.models.Producto;
import com.example.inventarioventasgym.repositories.CategoriaPRepository;
import com.example.inventarioventasgym.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository repo;
    private final CategoriaPRepository categoriaRepo;

    public ProductoService(ProductoRepository repo, CategoriaPRepository categoriaRepo) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
    }

    // Listar todos los productos activos
    public List<ProductoResponseDTO> getAll() {
        return repo.findByEstadoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener producto por ID (si está activo)
    public Optional<ProductoResponseDTO> getById(Integer id) {
        return repo.findById(id)
                .filter(Producto::getEstado)
                .map(this::toDTO);
    }

    // Crear producto a partir del DTO
    @Transactional
    public ProductoResponseDTO create(ProductoCreateDTO dto) {
        CategoriaP categoria = categoriaRepo.findById(dto.getCategoriaPId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto prod = new Producto();
        prod.setCategoriaP(categoria);
        prod.setUnidadMedida(dto.getUnidadMedida());
        prod.setPrecio(dto.getPrecio());
        prod.setStock(dto.getStock());
        prod.setNombre(dto.getNombre());
        prod.setEstado(true);

        prod = repo.save(prod);
        return toDTO(prod);
    }

    // Actualizar producto
    @Transactional
    public ProductoResponseDTO update(Integer id, ProductoCreateDTO cambios) {
        return repo.findById(id).map(prod -> {
            CategoriaP categoria = categoriaRepo.findById(cambios.getCategoriaPId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            prod.setCategoriaP(categoria);
            prod.setUnidadMedida(cambios.getUnidadMedida());
            prod.setPrecio(cambios.getPrecio());
            prod.setStock(cambios.getStock());
            prod.setNombre(cambios.getNombre());
            return toDTO(repo.save(prod));
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Borrado lógico
    @Transactional
    public void delete(Integer id) {
        repo.findById(id).ifPresent(prod -> {
            prod.setEstado(false);
            repo.save(prod);
        });
    }

    // Buscar productos por nombre
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCaseAndEstadoTrue(nombre)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Conversion de entidad a DTO de respuesta
    private ProductoResponseDTO toDTO(Producto prod) {
        return new ProductoResponseDTO(
                prod.getProductoId(),
                prod.getNombre(),
                prod.getCategoriaP().getCategoriaPId(),
                prod.getCategoriaP().getNombre(),
                prod.getUnidadMedida(),
                prod.getPrecio(),
                prod.getStock(),
                prod.getEstado()
        );
    }
}
