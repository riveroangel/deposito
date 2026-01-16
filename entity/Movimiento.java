package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Movimiento {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    private Producto producto;// JPA creará: producto_id



    //@ManyToOne(optional = true)
//@JoinColumn(nullable = true)
@ManyToOne(fetch = FetchType.LAZY)

private Usuario usuario;// JPA creará: usuario_id  ← ESTE ERA EL ERROR

    @PrePersist
    public void prePersist(){
        this.fecha = LocalDateTime.now();
    }
    // getters/setters
}
