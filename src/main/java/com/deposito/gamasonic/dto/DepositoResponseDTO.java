package com.deposito.gamasonic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepositoResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String responsable;
    private String telefono;
    private String observaciones;
    private boolean activo;
    private boolean esPrincipal;
    private Integer capacidadMaxima;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaActualizacion;
}