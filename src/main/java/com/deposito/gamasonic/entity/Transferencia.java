package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transferencia")
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String numeroTransferencia;  // Ej: TRANS-2024-001

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposito_origen_id", nullable = false)
    private Deposito depositoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposito_destino_id", nullable = false)
    private Deposito depositoDestino;

    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTransferencia estado = EstadoTransferencia.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_solicitante_id")
    private Usuario usuarioSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_confirmante_id")
    private Usuario usuarioConfirmante;

    @Column(length = 1000)
    private String observaciones;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaSolicitud = LocalDateTime.now();
        if (this.numeroTransferencia == null) {
            this.numeroTransferencia = "TRANS-" +
                    LocalDateTime.now().getYear() + "-" +
                    String.format("%03d", this.id != null ? this.id : 0);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void confirmar(Usuario confirmante) {
        if (this.estado != EstadoTransferencia.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo transferencias PENDIENTES pueden confirmarse. Estado actual: " + this.estado);
        }
        this.estado = EstadoTransferencia.CONFIRMADA;
        this.usuarioConfirmante = confirmante;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    public void cancelar() {
        if (this.estado != EstadoTransferencia.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo transferencias PENDIENTES pueden cancelarse. Estado actual: " + this.estado);
        }
        this.estado = EstadoTransferencia.CANCELADA;
    }
}