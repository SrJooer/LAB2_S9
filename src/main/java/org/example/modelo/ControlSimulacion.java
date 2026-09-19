package org.example.modelo;

public class ControlSimulacion {

    private boolean activo;
    private boolean pausado;

    public synchronized void iniciar() {
        activo = true;
        pausado = false;
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        pausado = false;
        notifyAll();
    }

    public synchronized void detener() {
        activo = false;
        pausado = false;
        notifyAll();
    }

    public synchronized void esperarSiPausado() throws InterruptedException {
        while (pausado) {
            wait();
        }
    }

    public synchronized boolean estaActivo() {
        return activo;
    }

    public synchronized boolean estaPausado() {
        return pausado;
    }
}
