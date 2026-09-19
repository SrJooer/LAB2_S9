package org.example.excepciones;

public class PaqueteNoEncontradoException extends PaqueteException {

    public PaqueteNoEncontradoException(String codigo, String zona) {
        super("El paquete " + codigo + " no está en " + zona);
    }
}
