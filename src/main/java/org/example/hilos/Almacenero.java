package org.example.hilos;

import org.example.excepciones.PaqueteException;
import org.example.modelo.Sistema;
import org.example.modelo.Zona;
import org.example.modelo.instancias.EstadoTipo;
import org.example.modelo.instancias.Paquete;

public class Almacenero extends Trabajador {

    private static final long TIEMPO_TRASLADO = 1500;

    private final Zona recepcion;
    private final Zona almacen;

    public Almacenero(Sistema sistema) {
        super("Almacenero", sistema);
        this.recepcion = sistema.getRecepcion();
        this.almacen = sistema.getAlmacen();
    }

    @Override
    protected void trabajar() throws InterruptedException, PaqueteException {
        Paquete paquete = recepcion.sacarMasPrioritario(EstadoTipo.RECIBIDO);
        esperarSiPausado();
        setPaqueteActual(paquete);
        dormir(TIEMPO_TRASLADO);
        paquete.cambiarEstado(EstadoTipo.ALMACENADO);
        registrar(paquete.getCodigo() + " almacenado");
        almacen.meter(paquete);
        setPaqueteActual(null);
    }
}
