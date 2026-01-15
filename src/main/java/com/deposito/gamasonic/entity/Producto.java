package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoBarra;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    //@Min(value = 0, message = "El stock no puede ser negativo")
    //private int stock;
    // 🔥 NUEVOS CAMPOS

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private int stock;

    @Column(length = 100)
    private String categoria;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(length = 100)
    private String marca;

    @Column (name = "stock_minimo")
    private Integer stockMinimo;

    @Column(length = 50)
    private String ubicacion;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos = new ArrayList<>();

    // 🔥 Métodos para auditoría automática
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // 🔥 Método útil para negocio
    public boolean necesitaReposicion() {
        return stockMinimo != null && stock < stockMinimo;
    }
}