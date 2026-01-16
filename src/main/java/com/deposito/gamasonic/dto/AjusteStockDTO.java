package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AjusteStockDTO {

    @NotNull(message = "El producto es requerido")
    private Long productoId;

    @NotNull(message = "El depósito es requerido")
    private Long depositoId;

    @NotNull(message = "La cantidad es requerida")
    private Integer cantidad;  // Positivo = agregar, Negativo = reducir

    @NotNull(message = "El tipo de ajuste es requerido")
    private String tipoAjuste;  // "INVENTARIO", "DIFERENCIA", "DONACION", "PERDIDA"

    private String motivo;
    private String observaciones;
}