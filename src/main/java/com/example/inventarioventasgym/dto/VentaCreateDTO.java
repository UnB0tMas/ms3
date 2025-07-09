package com.example.inventarioventasgym.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaCreateDTO {
    private LocalDateTime fechaVenta;
    private Integer personaId;
    private BigDecimal igv;
    private String metodoPago;
    private BigDecimal total;
    private List<DetalleVentaDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleVentaDTO {
        private Integer productoId;
        private Integer cant;
    }
}
