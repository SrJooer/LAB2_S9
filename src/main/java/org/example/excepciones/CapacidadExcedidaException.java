package org.example.excepciones;

public class CapacidadExcedidaException extends PaqueteException {

    public CapacidadExcedidaException(String nombre, int capacidad) {
        super(nombre + " ya está lleno (capacidad " + capacidad + ")");
    }
}
