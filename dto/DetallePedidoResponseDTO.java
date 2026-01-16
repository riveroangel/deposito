package com.deposito.gamasonic.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetallePedidoResponseDTO {
    private Long id;
    private ProductoDTO producto;
    private Integer cantidad;
    private Integer cantidadRecibida;
    private Integer cantidadPendiente;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private boolean completo;
}