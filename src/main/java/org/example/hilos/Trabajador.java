package org.example.hilos;

import org.example.excepciones.PaqueteException;
import org.example.modelo.ControlSimulacion;
import org.example.modelo.Registro;
import org.example.modelo.Sistema;
import org.example.modelo.instancias.Paquete;

public abstract class Trabajador implements Runnable {

    private final String nombre;
    private final ControlSimulacion control;
    private final Registro registro;
    private final Thread hilo;
    private Paquete paqueteActual;

    public Trabajador(String nombre, Sistema sistema) {
        this.nombre = nombre;
        this.control = sistema.getControl();
        this.registro = sistema.getRegistro();
        this.hilo = new Thread(this, nombre);
        this.hilo.setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (control.estaActivo()) {
                control.esperarSiPausado();
                try {
                    trabajar();
                } catch (PaqueteException e) {
                    registrar("Error en " + nombre + ": " + e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        terminar();
    }

    protected abstract void trabajar() throws InterruptedException, PaqueteException;

    protected void terminar() {
        setPaqueteActual(null);
    }

    protected void dormir(long milisegundos) throws InterruptedException {
        control.esperarSiPausado();
        Thread.sleep(milisegundos);
        control.esperarSiPausado();
    }

    protected void registrar(String mensaje) {
        registro.escribir(mensaje);
    }

    public void arrancar() {
        hilo.start();
    }

    public void interrumpir() {
        hilo.interrupt();
    }

    public void esperarFin() throws InterruptedException {
        hilo.join(1000);
    }

    public String getNombre() {
        return nombre;
    }

    public synchronized Paquete getPaqueteActual() {
        return paqueteActual;
    }

    protected synchronized void setPaqueteActual(Paquete paquete) {
        this.paqueteActual = paquete;
    }
}
