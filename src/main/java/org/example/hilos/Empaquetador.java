package org.example.hilos;

import org.example.excepciones.PaqueteException;
import org.example.modelo.Sistema;
import org.example.modelo.Zona;
import org.example.modelo.instancias.EstadoTipo;
import org.example.modelo.instancias.Paquete;

public class Empaquetador extends Trabajador {

    private final Zona clasificacion;
    private final Zona empaquetado;
    private final Zona expedicion;

    public Empaquetador(String nombre, Sistema sistema) {
        super(nombre, sistema);
        this.clasificacion = sistema.getClasificacion();
        this.empaquetado = sistema.getEmpaquetado();
        this.expedicion = sistema.getExpedicion();
    }

    @Override
    protected void trabajar() throws InterruptedException, PaqueteException {
        Paquete paquete = clasificacion.sacarMasPrioritario(EstadoTipo.CLASIFICADO);
        esperarSiPausado();
        setPaqueteActual(paquete);
        paquete.cambiarEstado(EstadoTipo.EMPAQUETANDO);
        empaquetado.meter(paquete);
        registrar(paquete.getCodigo() + " tomado por " + getNombre() + " (" + paquete.getPeso() + " kg)");
        dormir(paquete.calcularTiempoEmpaquetado());
        paquete.cambiarEstado(EstadoTipo.EMPAQUETADO);
        registrar(paquete.getCodigo() + " empaquetado");
        empaquetado.sacar(paquete);
        paquete.cambiarEstado(EstadoTipo.EN_EXPEDICION);
        registrar(paquete.getCodigo() + " en expedición, " + paquete.getRuta().getEtiqueta());
        expedicion.meter(paquete);
        setPaqueteActual(null);
    }
}
