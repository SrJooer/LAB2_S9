package org.example.modelo.instancias;

import org.example.excepciones.RutaDesconocidaException;

public enum Ruta {

    RUTA_1("Ruta 1"),
    RUTA_2("Ruta 2"),
    RUTA_3("Ruta 3"),
    RUTA_4("Ruta 4");

    private final String etiqueta;

    Ruta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static Ruta obtenerPorCiudad(String ciudad) throws RutaDesconocidaException {
        switch (ciudad) {
            case "Barcelona Centro":
            case "Eixample":
                return RUTA_1;
            case "Gràcia":
                return RUTA_2;
            case "Sant Martí":
                return RUTA_3;
            case "Badalona":
                return RUTA_4;
            default:
                throw new RutaDesconocidaException(ciudad);
        }
    }
}
