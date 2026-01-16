package com.deposito.gamasonic.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProveedorResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String ruc;
    private String direccion;
    private String telefono;
    private String email;
    private String contacto;
    private String plazoPago;
    private String observaciones;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private Integer totalPedidos;

    public ProveedorResponseDTO(Long id, String codigo, String nombre, String ruc,
                                String direccion, String telefono, String email,
                                String contacto, String plazoPago, String observaciones,
                                boolean activo, LocalDateTime fechaCreacion,
                                LocalDateTime fechaActualizacion, Integer totalPedidos) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.ruc = ruc;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.contacto = contacto;
        this.plazoPago = plazoPago;
        this.observaciones = observaciones;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.totalPedidos = totalPedidos;
    }
}