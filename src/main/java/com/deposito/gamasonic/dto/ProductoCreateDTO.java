package com.deposito.gamasonic.dto;

import com.deposito.gamasonic.entity.CategoriaProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
//Dios es mi Fortaleza El que Creo los Cielos y la Tierra
@Data
public class ProductoCreateDTO {

        @NotBlank(message = "El código de barras es requerido")
        private String codigoBarra;

        @NotBlank(message = "El nombre es requerido")
        private String nombre;

        private String descripcion;

        @NotNull(message = "El stock inicial es requerido")
        @PositiveOrZero(message = "El stock no puede ser negativo")

        private Integer stock;

        @NotNull(message = "La categoría es requerida")
        private CategoriaProducto categoria;

        @NotNull(message = "El precio de compra es requerido")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        private BigDecimal precioCompra;

        @NotNull(message = "El precio de venta es requerido")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        private BigDecimal precioVenta;

        private String marca;
        private String ubicacion;

        @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
        private Integer stockMinimo;
}