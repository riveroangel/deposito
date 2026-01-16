package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "deposito")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "direccion", length = 500)
    private String direccion;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "responsable", length = 100)
    private String responsable;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "es_principal")
    private boolean esPrincipal = false;

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (codigo == null || codigo.isEmpty()) {
            codigo = "DEP-" + String.format("%03d", (id != null ? id : System.currentTimeMillis() % 1000));
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public boolean puedeDesactivarse() {
        return !this.esPrincipal;
    }
}