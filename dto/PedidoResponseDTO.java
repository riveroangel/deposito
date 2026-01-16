package com.deposito.gamasonic.dto;

import com.deposito.gamasonic.entity.EstadoPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {
    private Long id;
    private String numeroPedido;
    private ProveedorResponseDTO proveedor;
    private EstadoPedido estado;
    private LocalDate fechaPedido;
    private LocalDate fechaEsperadaEntrega;
    private LocalDate fechaRealEntrega;
    private String observaciones;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<DetallePedidoResponseDTO> detalles;
}