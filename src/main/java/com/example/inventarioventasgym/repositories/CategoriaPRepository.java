// CategoriaPRepository.java
package com.example.inventarioventasgym.repositories;

import com.example.inventarioventasgym.models.CategoriaP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaPRepository extends JpaRepository<CategoriaP, Integer> {
    List<CategoriaP> findByNombreContainingIgnoreCaseAndEstadoTrue(String nombre);
    List<CategoriaP> findByEstadoTrue();
}
