package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovimientoEntradaDTO {

    @NotBlank(message = "El código de barras es requerido")
    private String codigoBarra;
    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;// ← Cambia a Integer

    // Constructor
    public MovimientoEntradaDTO() {}

    public MovimientoEntradaDTO(String codigoBarra, int cantidad) {
        this.codigoBarra = codigoBarra;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public String getCodigoBarra() { return codigoBarra; }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}