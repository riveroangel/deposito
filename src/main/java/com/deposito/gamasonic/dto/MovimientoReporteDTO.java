package com.deposito.gamasonic.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoReporteDTO {
    private Long id;
    private LocalDateTime fecha;
    private String tipo;
    private Integer cantidad;
    private String codigoBarra;
    private String nombreProducto;
    private String usuario;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal valorTotal;

    public MovimientoReporteDTO(Long id, LocalDateTime fecha, String tipo,
                                Integer cantidad, String codigoBarra,
                                String nombreProducto, String usuario,
                                BigDecimal precioCompra, BigDecimal precioVenta) {
        this.id = id;
        this.fecha = fecha;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.codigoBarra = codigoBarra;
        this.nombreProducto = nombreProducto;
        this.usuario = usuario;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.valorTotal = calcularValorTotal();
    }

    private BigDecimal calcularValorTotal() {
        if ("ENTRADA".equals(tipo) && precioCompra != null) {
            return precioCompra.multiply(BigDecimal.valueOf(cantidad));
        } else if ("SALIDA".equals(tipo) && precioVenta != null) {
            return precioVenta.multiply(BigDecimal.valueOf(cantidad));
        }
        return BigDecimal.ZERO;
    }
}
