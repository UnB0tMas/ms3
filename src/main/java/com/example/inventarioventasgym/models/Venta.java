package com.example.inventarioventasgym.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ventaId;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    // FK: Cliente/personaId sincronizado desde microservicio de clientes
    @Column(nullable = false)
    private Integer personaId;

    // SNAPSHOT (recomendado, opcional)
    @Column(nullable = false, length = 150)
    private String nombreCliente;

    @Column(nullable = false, length = 50)
    private String documentoCliente;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal igv;

    @Column(nullable = false, length = 50)
    private String metodoPago;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private Boolean estado = true;
}
