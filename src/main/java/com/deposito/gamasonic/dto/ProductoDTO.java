package com.deposito.gamasonic.dto;

import com.deposito.gamasonic.entity.CategoriaProducto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor  // Genera el constructor vacío
@AllArgsConstructor // Genera el constructor con todos los campos (el que comentaste)
@Data
public class ProductoDTO {
    private Long id;
    private String codigoBarra;
    private String nombre;
    private String descripcion;
    private int stock;
    private CategoriaProducto categoria;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private String marca;
    private Integer stockMinimo;
    private String ubicacion;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private boolean necesitaReposicion;


    // 🔥 Campo calculado
    // private String estado; // "DISPONIBLE", "BAJO_STOCK", "AGOTADO"
/*
    // Constructor para compatibilidad con código existente
    public ProductoDTO(Long id, String codigoBarra, String nombre, int stock) {
        this.id = id;
        this.codigoBarra = codigoBarra;
        this.nombre = nombre;
        this.stock = stock;
    }

    // Constructor completo
        public ProductoDTO(
                        Long id,
                        String codigoBarra,
                        String nombre,
                        String descripcion,
                        int stock,
                        CategoriaProducto categoria,
                        BigDecimal precioCompra,
                        BigDecimal precioVenta,
                        String marca,
                        Integer stockMinimo,
                        String ubicacion,
                        boolean activo,
                        LocalDateTime fechaCreacion,
                        LocalDateTime fechaActualizacion,
                        boolean necesitaReposicion
                                                    )
                        {
        this.id = id;
        this.codigoBarra = codigoBarra;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.categoria = categoria;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.marca = marca;
        this.stockMinimo = stockMinimo;
        this.ubicacion = ubicacion;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.necesitaReposicion = necesitaReposicion;

  */  }

