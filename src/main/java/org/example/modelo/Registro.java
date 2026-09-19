package org.example.modelo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Registro {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private EscuchaRegistro escucha;

    public synchronized void setEscucha(EscuchaRegistro escucha) {
        this.escucha = escucha;
    }

    public synchronized void escribir(String mensaje) {
        String linea = LocalTime.now().format(FORMATO_HORA) + " | " + mensaje;
        if (escucha != null) {
            escucha.nuevaLinea(linea);
        }
    }

    public synchronized void limpiar() {
        if (escucha != null) {
            escucha.limpiar();
        }
    }
}
