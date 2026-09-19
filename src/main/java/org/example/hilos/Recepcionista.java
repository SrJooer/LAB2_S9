package org.example.hilos;

import java.util.Random;
import org.example.modelo.Estadisticas;
import org.example.modelo.GeneradorPaquetes;
import org.example.modelo.Sistema;
import org.example.modelo.Zona;
import org.example.modelo.instancias.Paquete;

public class Recepcionista extends Trabajador {

    private static final long INTERVALO_MINIMO = 1000;
    private static final int INTERVALO_VARIABLE = 2500;

    private final GeneradorPaquetes generador;
    private final Zona recepcion;
    private final Estadisticas estadisticas;
    private final Random azar = new Random();

    public Recepcionista(Sistema sistema) {
        super("Recepción", sistema);
        this.generador = sistema.getGenerador();
        this.recepcion = sistema.getRecepcion();
        this.estadisticas = sistema.getEstadisticas();
    }

    @Override
    protected void trabajar() throws InterruptedException {
        Paquete paquete = generador.generar();
        estadisticas.registrarGenerado();
        registrar(paquete.getCodigo() + " recibido (" + paquete.getCiudad() + ", prioridad " + paquete.getPrioridad().getEtiqueta() + ")");
        recepcion.meter(paquete);
        dormir(INTERVALO_MINIMO + azar.nextInt(INTERVALO_VARIABLE));
    }
}
