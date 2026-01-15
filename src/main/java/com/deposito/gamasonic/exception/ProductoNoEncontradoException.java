// exception/ProductoNoEncontradoException.java
package com.deposito.gamasonic.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(String message) {
        super(message);
    }
}