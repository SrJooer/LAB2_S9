package org.example.modelo.instancias;

public final class PrioridadTipo {

    private static final PrioridadTipo PRIORIDAD_NORMAL = new PrioridadTipo("Normal");
    private static final PrioridadTipo PRIORIDAD_URGENTE = new PrioridadTipo("Urgente");

    public final String prioridad;

    private PrioridadTipo(String prioridad) {
        this.prioridad = prioridad;
    }
}
