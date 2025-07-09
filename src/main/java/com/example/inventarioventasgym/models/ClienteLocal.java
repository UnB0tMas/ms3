// src/main/java/com/example/inventarioventasgym/models/ClienteLocal.java
package com.example.inventarioventasgym.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente_local")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteLocal {
    @Id
    private Integer clienteId; // Debe ser igual al que manda MS2

    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @Column(nullable = false, length = 50)
    private String documento;

    @Column(nullable = true)
    private Double pesoInicio;

    @Column(nullable = true)
    private Integer membresiaId;

    @Column(nullable = false)
    private Boolean estado = true;
}
