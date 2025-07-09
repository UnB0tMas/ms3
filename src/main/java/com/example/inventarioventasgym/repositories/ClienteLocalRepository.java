// ClienteLocalRepository.java
package com.example.inventarioventasgym.repositories;

import com.example.inventarioventasgym.models.ClienteLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteLocalRepository extends JpaRepository<ClienteLocal, Integer> {
    List<ClienteLocal> findByNombreCompletoContainingIgnoreCaseAndEstadoTrue(String nombre);
    List<ClienteLocal> findByEstadoTrue();
}
