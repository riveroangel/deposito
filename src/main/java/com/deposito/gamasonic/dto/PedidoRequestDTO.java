package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "El proveedor es requerido")
    private Long proveedorId;

    @FutureOrPresent(message = "La fecha de entrega debe ser hoy o en el futuro")
    private LocalDate fechaEsperadaEntrega;

    private String observaciones;

    @NotNull(message = "Los detalles del pedido son requeridos")
    private List<DetallePedidoRequestDTO> detalles = new ArrayList<>();
}