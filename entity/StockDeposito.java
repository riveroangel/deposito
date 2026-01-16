package com.deposito.gamasonic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "stock_deposito",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "deposito_id"}))
public class StockDeposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposito_id", nullable = false)
    private Deposito deposito;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "stock_maximo")
    private Integer stockMaximo;

    @Column(length = 100)
    private String ubicacion;  // Ej: "Estante A-12", "Bodega 3"

    // Métodos de negocio
    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad;
    }

    public boolean necesitaReposicion() {
        return this.stockMinimo != null && this.stock < this.stockMinimo;
    }

    public boolean excedeMaximo() {
        return this.stockMaximo != null && this.stock > this.stockMaximo;
    }

    public void agregarStock(int cantidad) {
        this.stock += cantidad;
    }

    public void reducirStock(int cantidad) {
        if (cantidad > this.stock) {
            throw new IllegalArgumentException(
                    "No hay suficiente stock. Disponible: " + this.stock + ", Solicitado: " + cantidad);
        }
        this.stock -= cantidad;
    }

    // Actualizar stock mínimo/máximo desde producto
    public void actualizarDesdeProducto() {
        if (this.producto != null) {
            this.stockMinimo = this.producto.getStockMinimo();
            // Podrías calcular stock máximo basado en capacidad del depósito
        }
    }
}