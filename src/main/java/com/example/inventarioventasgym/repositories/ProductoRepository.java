// ProductoRepository.java
package com.example.inventarioventasgym.repositories;

import com.example.inventarioventasgym.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContainingIgnoreCaseAndEstadoTrue(String nombre);
    List<Producto> findByEstadoTrue();
}
