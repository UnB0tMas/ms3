package com.example.inventarioventasgym.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productoId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_p_id", nullable = false)
    private CategoriaP categoriaP;

    @Column(nullable = false, length = 100)
    private String nombre; // Nombre del producto

    @Column(nullable = false, length = 50)
    private String unidadMedida;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Boolean estado = true;
}
