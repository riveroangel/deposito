package com.deposito.gamasonic.dto;

import lombok.Data;

@Data
public class StockDepositoResponseDTO {
    private Long id;
    private ProductoDTO producto;
    private DepositoResponseDTO deposito;
    private Integer stock;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String ubicacion;
    private boolean necesitaReposicion;
    private boolean excedeMaximo;

    public StockDepositoResponseDTO(Long id, ProductoDTO producto, DepositoResponseDTO deposito,
                                    Integer stock, Integer stockMinimo, Integer stockMaximo,
                                    String ubicacion, boolean necesitaReposicion, boolean excedeMaximo) {
        this.id = id;
        this.producto = producto;
        this.deposito = deposito;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
        this.ubicacion = ubicacion;
        this.necesitaReposicion = necesitaReposicion;
        this.excedeMaximo = excedeMaximo;
    }
}
