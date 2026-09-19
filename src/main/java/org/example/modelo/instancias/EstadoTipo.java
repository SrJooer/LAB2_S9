package org.example.modelo.instancias;

public enum EstadoTipo {

    RECIBIDO("Recibido"),
    ALMACENADO("Almacenado"),
    CLASIFICANDO("Clasificando"),
    CLASIFICADO("Clasificado"),
    EMPAQUETANDO("Empaquetando"),
    EMPAQUETADO("Empaquetado"),
    EN_EXPEDICION("En expedición"),
    EN_REPARTO("En reparto"),
    NUEVO_INTENTO("Nuevo intento"),
    ENTREGADO("Entregado"),
    DEVUELTO("Devuelto");

    private final String etiqueta;

    EstadoTipo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public boolean puedePasarA(EstadoTipo siguiente) {
        switch (this) {
            case RECIBIDO:
                return siguiente == ALMACENADO;
            case ALMACENADO:
                return siguiente == CLASIFICANDO;
            case CLASIFICANDO:
                return siguiente == CLASIFICADO;
            case CLASIFICADO:
                return siguiente == EMPAQUETANDO;
            case EMPAQUETANDO:
                return siguiente == EMPAQUETADO;
            case EMPAQUETADO:
                return siguiente == EN_EXPEDICION;
            case EN_EXPEDICION:
                return siguiente == EN_REPARTO;
            case EN_REPARTO:
                return siguiente == ENTREGADO || siguiente == NUEVO_INTENTO;
            case NUEVO_INTENTO:
                return siguiente == EN_REPARTO || siguiente == DEVUELTO;
            default:
                return false;
        }
    }
}
