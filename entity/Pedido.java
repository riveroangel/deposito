package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String numeroPedido;  // Ej: PED-2024-001

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.BORRADOR;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @Column(name = "fecha_esperada_entrega")
    private LocalDate fechaEsperadaEntrega;

    @Column(name = "fecha_real_entrega")
    private LocalDate fechaRealEntrega;

    @Column(length = 1000)
    private String observaciones;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal impuestos = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.fechaPedido == null) {
            this.fechaPedido = LocalDate.now();
        }
        calcularTotales();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        calcularTotales();
    }

    public void calcularTotales() {
        this.subtotal = BigDecimal.ZERO;

        for (DetallePedido detalle : this.detalles) {
            if (detalle.getPrecioUnitario() != null && detalle.getCantidad() > 0) {
                BigDecimal subtotalDetalle = detalle.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(detalle.getCantidad()));
                this.subtotal = this.subtotal.add(subtotalDetalle);
            }
        }

        // Calcular impuestos (ej: 18% IVA)
        this.impuestos = this.subtotal.multiply(new BigDecimal("0.18"));
        this.total = this.subtotal.add(this.impuestos);
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalle.setPedido(this);
        this.detalles.add(detalle);
        calcularTotales();
    }

    public void removerDetalle(DetallePedido detalle) {
        this.detalles.remove(detalle);
        detalle.setPedido(null);
        calcularTotales();
    }
}