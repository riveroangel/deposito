
package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String codigo;  // Ej: PROV-001

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 20)
    private String ruc;  // O CUIT, según país

    @Column(length = 500)
    private String direccion;

    @Column(length = 50)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String contacto;  // Nombre de persona de contacto

    @Column(length = 50)
    private String plazoPago;  // "30 días", "Contado", etc.

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.codigo == null || this.codigo.isEmpty()) {
            this.codigo = "PROV-" + String.format("%03d", this.id != null ? this.id : 0);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}