package org.example.modelo.instancias;

public enum EstadoRepartidor {

    DISPONIBLE("Disponible"),
    CARGANDO("Cargando"),
    EN_RUTA("En ruta"),
    ENTREGANDO("Entregando"),
    REGRESANDO("Regresando"),
    FUERA_DE_SERVICIO("Fuera de servicio");

    private final String etiqueta;

    EstadoRepartidor(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
