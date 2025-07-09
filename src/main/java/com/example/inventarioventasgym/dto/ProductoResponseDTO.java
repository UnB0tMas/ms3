package com.example.inventarioventasgym.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Integer productoId;
    private String nombre;
    private Integer categoriaPId;
    private String nombreCategoria;
    private String unidadMedida;
    private BigDecimal precio;
    private Integer stock;
    private Boolean estado;
}
