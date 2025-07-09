package com.example.inventarioventasgym.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponseDTO {
    private Integer ventaId;
    private LocalDateTime fechaVenta;
    private Integer personaId;
    private String nombreCliente;
    private String documentoCliente;
    private BigDecimal igv;
    private String metodoPago;
    private BigDecimal total;
    private Boolean estado;
    private List<DetalleVentaResponseDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleVentaResponseDTO {
        private Integer detalleVentaId;
        private Integer productoId;
        private String nombreProducto;
        private Integer cant;
    }
}
