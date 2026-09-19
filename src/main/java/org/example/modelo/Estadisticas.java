package org.example.modelo;

public class Estadisticas {

    private int generados;
    private int entregados;
    private int devueltos;
    private double sumaSegundosEntrega;

    public synchronized void registrarGenerado() {
        generados++;
    }

    public synchronized void registrarEntrega(double segundos) {
        entregados++;
        sumaSegundosEntrega += segundos;
    }

    public synchronized void registrarDevolucion() {
        devueltos++;
    }

    public synchronized double calcularTiempoPromedio() {
        if (entregados == 0) {
            return 0;
        }
        return sumaSegundosEntrega / entregados;
    }

    public synchronized void reiniciar() {
        generados = 0;
        entregados = 0;
        devueltos = 0;
        sumaSegundosEntrega = 0;
    }

    public synchronized int getGenerados() {
        return generados;
    }

    public synchronized int getEntregados() {
        return entregados;
    }

    public synchronized int getDevueltos() {
        return devueltos;
    }
}
