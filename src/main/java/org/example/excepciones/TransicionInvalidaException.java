package org.example.excepciones;

import org.example.modelo.instancias.EstadoTipo;

public class TransicionInvalidaException extends PaqueteException {

    public TransicionInvalidaException(String codigo, EstadoTipo actual, EstadoTipo nuevo) {
        super("El paquete " + codigo + " no puede pasar de " + actual.getEtiqueta() + " a " + nuevo.getEtiqueta());
    }
}
