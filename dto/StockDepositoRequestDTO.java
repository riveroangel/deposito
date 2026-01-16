package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockDepositoRequestDTO {

    @NotNull(message = "El producto es requerido")
    private Long productoId;

    @NotNull(message = "El depósito es requerido")
    private Long depositoId;

    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;

    @Min(value = 0, message = "El stock máximo no puede ser negativo")
    private Integer stockMaximo;

    private String ubicacion;
}