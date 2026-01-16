package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepositoUpdateDTO {

    @Size(max = 50, message = "El codigo no puede exceder los 50 caracteres")
    private String codigo;

    @Size(max = 200, message = "El nombre no puede exceder los 200 caracteres")
    private String nombre;

    @Size(max = 500, message = "La direccion no puede exceder los 500 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede exceder los 100 caracteres")
    private String ciudad;

    @Size(max = 100, message = "El responsable no puede exceder los 100 caracteres")
    private String responsable;

    @Size(max = 50, message = "El telefono no puede exceder los 50 caracteres")
    private String telefono;

    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres")
    private String observaciones;

    private Boolean activo;
    private Boolean esPrincipal;
    private Integer capacidadMaxima;
}