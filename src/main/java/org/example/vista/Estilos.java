package org.example.vista;

public class Estilos {

    private static final String RUTA_HOJA = "/org/example/estilos.css";

    public static String obtenerHoja() {
        return Estilos.class.getResource(RUTA_HOJA).toExternalForm();
    }
}
