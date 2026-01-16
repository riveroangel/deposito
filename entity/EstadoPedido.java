package com.deposito.gamasonic.entity;

public enum EstadoPedido {
    BORRADOR,      // Pedido en creación
    PENDIENTE,     // Enviado al proveedor
    PARCIAL,       // Parcialmente recibido
    COMPLETADO,    // Completamente recibido
    CANCELADO      // Cancelado
}