package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecepcionDetalleDTO {

    @NotNull(message = "El detalle del pedido es requerido")
    private Long detallePedidoId;

    @NotNull(message = "La cantidad recibida es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidadRecibida;

    private String observaciones;  // Calidad, daños, etc.
}