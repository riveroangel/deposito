package com.deposito.gamasonic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "productos", indexes = {
        @Index(name = "idx_producto_codigo", columnList = "codigoBarra"),
        @Index(name = "idx_producto_categoria", columnList = "categoria"),
        @Index(name = "idx_producto_activo", columnList = "activo")
})
public class Producto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código de barras es obligatorio")
    @Size(min = 3, max = 50, message = "El código debe tener entre 3 y 50 caracteres")
    @Column(unique = true, nullable = false)
    private String codigoBarra;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;


    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProducto categoria;


    @Column(precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Formato de precio inválido")
    @Column(precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(length = 100)
    private String marca;
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
   // @Column (name = "stock_minimo")
    private Integer stockMinimo;

    @Column(length = 50)
    private String ubicacion;

    @Column(nullable = false)
    private boolean activo = true;

    //@Column(name = "fecha_creacion")
    @Column(name = "fecha_creacion", updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    //@Column(name = "fecha_actualizacion")
    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;
//esto se modifico 17/01/2026 a las 13:45
   // @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
  //  private List<Movimiento> movimientos = new ArrayList<>();

    @OneToMany(mappedBy = "producto")
    @JsonIgnore  // ← Evitar recursión
    private List<Movimiento> movimientos;

    // 🔥 Métodos para auditoría automática

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
     /*
        esto es para agregar auditoria completa
        @CreatedBy
        @Column(name = "creado_por")
        private String creadoPor;

        @LastModifiedBy
        @Column(name = "actualizado_por")
        private String actualizadoPor;
*/    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // 🔥 Método útil para negocio
    public boolean necesitaReposicion() {

        return stockMinimo != null && stock <= stockMinimo;
    }

    // 🔥 Método para agregar movimiento de inventario
    public void agregarMovimiento(TipoMovimiento tipo, int cantidad, Usuario usuario) {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(tipo);
        movimiento.setCantidad(cantidad);
        movimiento.setProducto(this);
        movimiento.setUsuario(usuario);
        movimiento.setFecha(LocalDateTime.now());
        this.movimientos.add(movimiento);



        // Actualizar stock según tipo de movimiento
        if (tipo == TipoMovimiento.ENTRADA) {
            this.stock += cantidad;
        } else if (tipo == TipoMovimiento.SALIDA) {
            this.stock -= cantidad;
        }


    }

    // 🔥 Método para verificar si se puede vender cierta cantidad
    public boolean puedeVender(int cantidad) {
        return this.activo && this.stock >= cantidad;
    }

    // 🔥 Método para descontar stock
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (this.stock < cantidad) {
            throw new IllegalStateException("Stock insuficiente");
        }
        this.stock -= cantidad;
        this.preUpdate(); // Actualizar fecha
    }

    // 🔥 Método para reponer stock
    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.stock += cantidad;
        this.preUpdate(); // Actualizar fecha
    }


}
/*
// CORREGIR comparación:
    if (tipo == TipoMovimiento.ENTRADA) {  // ← ASÍ
        this.stock += cantidad;
    } else if (tipo == TipoMovimiento.SALIDA) {
        this.stock -= cantidad;
    }
}

*/

