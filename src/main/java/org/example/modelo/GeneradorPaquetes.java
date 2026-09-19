package org.example.modelo;

import java.util.Random;
import org.example.modelo.instancias.Paquete;
import org.example.modelo.instancias.PrioridadTipo;
import org.example.modelo.instancias.Ruta;

public class GeneradorPaquetes {

    private static final String[] NOMBRES = {
            "Carlos López", "Ana Martín", "Marc Puig", "Lucía Gómez",
            "Pere Soler", "Elena Ruiz", "Joan Vidal", "Sara Torres"
    };

    private static final String[] CALLES = {
            "Carrer de Balmes", "Gran Via", "Carrer de Provença",
            "Rambla del Poblenou", "Avinguda Diagonal", "Carrer de Sants"
    };

    private final Random azar = new Random();
    private int contador;

    public synchronized Paquete generar() {
        contador++;
        String codigo = String.format("PKG-%03d", contador);
        String cliente = elegir(NOMBRES);
        String direccion = elegir(CALLES) + " " + (1 + azar.nextInt(200));
        String ciudad = elegir(Ruta.CIUDADES);
        double peso = (5 + azar.nextInt(80)) / 10.0;
        return new Paquete(codigo, cliente, direccion, ciudad, peso, elegirPrioridad());
    }

    private PrioridadTipo elegirPrioridad() {
        int numero = azar.nextInt(100);
        if (numero < 10) {
            return PrioridadTipo.URGENTE;
        }
        if (numero < 30) {
            return PrioridadTipo.ALTA;
        }
        if (numero < 80) {
            return PrioridadTipo.NORMAL;
        }
        return PrioridadTipo.BAJA;
    }

    private String elegir(String[] opciones) {
        return opciones[azar.nextInt(opciones.length)];
    }

    public synchronized void reiniciar() {
        contador = 0;
    }
}
