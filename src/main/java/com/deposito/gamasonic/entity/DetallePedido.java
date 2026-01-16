package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "cantidad_recibida")
    private Integer cantidadRecibida = 0;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (this.precioUnitario != null && this.cantidad != null) {
            this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
        }
    }

    public boolean estaCompleto() {
        return this.cantidadRecibida != null && this.cantidadRecibida >= this.cantidad;
    }

    public boolean estaPendiente() {
        return this.cantidadRecibida == null || this.cantidadRecibida < this.cantidad;
    }

    public Integer getCantidadPendiente() {
        if (this.cantidadRecibida == null) {
            return this.cantidad;
        }
        return Math.max(0, this.cantidad - this.cantidadRecibida);
    }
}