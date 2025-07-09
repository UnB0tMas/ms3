package com.example.inventarioventasgym.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaPResponseDTO {
    private Integer categoriaPId;
    private String nombre;
    private String descripcion;
    private Boolean estado;
}
