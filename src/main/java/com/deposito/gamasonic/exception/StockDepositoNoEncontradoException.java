package com.deposito.gamasonic.exception;

public class StockDepositoNoEncontradoException extends RuntimeException {
    public StockDepositoNoEncontradoException(String message) {
        super(message);
    }
}