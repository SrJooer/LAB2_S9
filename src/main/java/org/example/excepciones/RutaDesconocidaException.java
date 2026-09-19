package org.example.excepciones;

public class RutaDesconocidaException extends PaqueteException {

    public RutaDesconocidaException(String ciudad) {
        super("No hay ninguna ruta para la ciudad " + ciudad);
    }
}
