package com.example.inventarioventasgym.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_p")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoriaPId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean estado = true;
}


