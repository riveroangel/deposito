package com.deposito.gamasonic.entity;

public enum EstadoTransferencia {
    PENDIENTE,      // Solicitada pero no confirmada
    CONFIRMADA,     // Confirmada y ejecutada
    CANCELADA,      // Cancelada
    RECHAZADA       // Rechazada por el depósito destino
}
