package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;


// Genera getters, setters, toString, equals, hashCode
@Data

public class MovimientoSalidaDTO {

    @NotBlank(message = "El código de barras es requerido")
    private String codigoBarra;

    @Positive(message = "La cantidad debe ser mayor a 0")
    private int cantidad;

    public MovimientoSalidaDTO() {}

    public MovimientoSalidaDTO(String codigoBarra, int cantidad) {
        this.codigoBarra = codigoBarra;
        this.cantidad = cantidad;
    }

    public String getCodigoBarra() { return codigoBarra; }
    public void setCodigoBarra(String codigoBarra) { this.codigoBarra = codigoBarra; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}