package com.example.inventarioventasgym.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCreateDTO {
    private Integer categoriaPId;  // Solo el ID, Angular envía el id seleccionado
    private String unidadMedida;
    private BigDecimal precio;
    private Integer stock;
    private String nombre;
}
