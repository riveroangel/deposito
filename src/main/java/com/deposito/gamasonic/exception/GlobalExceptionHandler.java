package com.deposito.gamasonic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicado(DuplicadoException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 409,
                        "error", "Duplicado",
                        "message", ex.getMessage()
                ));
    }
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleStockInsuficiente(StockInsuficienteException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "error", "Stock insuficiente",
                        "message", ex.getMessage()
                ));
    }

}
