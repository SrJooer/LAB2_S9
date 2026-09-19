package org.example.hilos;

import org.example.excepciones.PaqueteException;
import org.example.modelo.Sistema;
import org.example.modelo.Zona;
import org.example.modelo.instancias.EstadoTipo;
import org.example.modelo.instancias.Paquete;
import org.example.modelo.instancias.Ruta;

public class Clasificador extends Trabajador {

    private static final long TIEMPO_CLASIFICACION = 3500;

    private final Zona almacen;
    private final Zona clasificacion;

    public Clasificador(String nombre, Sistema sistema) {
        super(nombre, sistema);
        this.almacen = sistema.getAlmacen();
        this.clasificacion = sistema.getClasificacion();
    }

    @Override
    protected void trabajar() throws InterruptedException, PaqueteException {
        Paquete paquete = almacen.sacarMasPrioritario(EstadoTipo.ALMACENADO);
        setPaqueteActual(paquete);
        paquete.cambiarEstado(EstadoTipo.CLASIFICANDO);
        clasificacion.meter(paquete);
        registrar(paquete.getCodigo() + " tomado por " + getNombre());
        dormir(TIEMPO_CLASIFICACION);
        Ruta ruta = Ruta.obtenerPorCiudad(paquete.getCiudad());
        paquete.setRuta(ruta);
        paquete.cambiarEstado(EstadoTipo.CLASIFICADO);
        clasificacion.avisarCambio();
        registrar(paquete.getCodigo() + " clasificado → " + ruta.getEtiqueta());
        setPaqueteActual(null);
    }
}
