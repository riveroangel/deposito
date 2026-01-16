package com.deposito.gamasonic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProveedorRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @Pattern(regexp = "^[0-9]{11,13}$", message = "RUC debe tener 11-13 dígitos")
    private String ruc;

    private String direccion;

    @Pattern(regexp = "^[0-9\\s\\-+()]{7,20}$", message = "Teléfono inválido")
    private String telefono;

    @Email(message = "Email inválido")
    private String email;

    private String contacto;
    private String plazoPago;
    private String observaciones;
}