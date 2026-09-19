package org.example.modelo.instancias;

public enum PrioridadTipo {

    URGENTE("Urgente", 1),
    ALTA("Alta", 2),
    NORMAL("Normal", 3),
    BAJA("Baja", 4);

    private final String etiqueta;
    private final int orden;

    PrioridadTipo(String etiqueta, int orden) {
        this.etiqueta = etiqueta;
        this.orden = orden;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public boolean esMasPrioritariaQue(PrioridadTipo otra) {
        return orden < otra.orden;
    }
}
