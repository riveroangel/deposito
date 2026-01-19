package com.deposito.gamasonic.entity;

public enum EstadoTransferencia {
    PENDIENTE("Pendiente", "badge-warning", true, false),
    CONFIRMADA("Confirmada", "badge-success", false, true),
    CANCELADA("Cancelada", "badge-secondary", false, false),
    RECHAZADA("Rechazada", "badge-danger", false, false),
    EN_TRANSITO("En tránsito", "badge-info", true, false),
    PARCIALMENTE_COMPLETADA("Parcialmente completada", "badge-primary", false, true);

    private final String descripcion;
    private final String cssClass; // Para UI
    private final boolean editable; // Si se puede modificar
    private final boolean completada; // Si está finalizada

    EstadoTransferencia(String descripcion, String cssClass, boolean editable, boolean completada) {
        this.descripcion = descripcion;
        this.cssClass = cssClass;
        this.editable = editable;
        this.completada = completada;
    }

    // Getters
    public String getDescripcion() {
        return descripcion;
    }

    public String getCssClass() {
        return cssClass;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isCompletada() {
        return completada;
    }

    // Métodos utilitarios
    public boolean permiteRecepcion() {
        return this == EN_TRANSITO || this == PENDIENTE;
    }

    public boolean puedeCancelar() {
        return this == PENDIENTE || this == EN_TRANSITO;
    }

    public boolean isFinalEstado() {
        return this == CONFIRMADA || this == CANCELADA || this == RECHAZADA;
    }

    // Transiciones válidas
    public boolean puedeCambiarA(EstadoTransferencia nuevoEstado) {
        switch (this) {
            case PENDIENTE:
                return nuevoEstado == CONFIRMADA ||
                        nuevoEstado == EN_TRANSITO ||
                        nuevoEstado == CANCELADA ||
                        nuevoEstado == RECHAZADA;
            case EN_TRANSITO:
                return nuevoEstado == CONFIRMADA ||
                        nuevoEstado == PARCIALMENTE_COMPLETADA ||
                        nuevoEstado == CANCELADA;
            case PARCIALMENTE_COMPLETADA:
                return nuevoEstado == CONFIRMADA || nuevoEstado == CANCELADA;
            default:
                return false; // Estados finales no pueden cambiar
        }
    }
}