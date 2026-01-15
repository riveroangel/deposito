package com.deposito.gamasonic.exception;

public class DuplicadoException extends RuntimeException{
    public DuplicadoException(String mensaje){
        super(mensaje);
    }
}
