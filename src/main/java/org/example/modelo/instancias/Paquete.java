package org.example.modelo.instancias;

import org.example.excepciones.TransicionInvalidaException;

public class Paquete {

    private final String codigo;
    private final String nombreCliente;
    private final String direccion;
    private final String ciudad;
    private final double peso;
    private final PrioridadTipo prioridad;
    private final long horaCreacion;
    private EstadoTipo estado;
    private Ruta ruta;
    private int intentos;

    public Paquete(String codigo, String nombreCliente, String direccion, String ciudad, double peso, PrioridadTipo prioridad) {
        this.codigo = codigo;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.peso = peso;
        this.prioridad = prioridad;
        this.horaCreacion = System.currentTimeMillis();
        this.estado = EstadoTipo.RECIBIDO;
    }

    public synchronized void cambiarEstado(EstadoTipo nuevoEstado) throws TransicionInvalidaException {
        if (!estado.puedePasarA(nuevoEstado)) {
            throw new TransicionInvalidaException(codigo, estado, nuevoEstado);
        }
        estado = nuevoEstado;
    }

    public synchronized EstadoTipo getEstado() {
        return estado;
    }

    public synchronized Ruta getRuta() {
        return ruta;
    }

    public synchronized void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public synchronized int getIntentos() {
        return intentos;
    }

    public synchronized void sumarIntento() {
        intentos++;
    }

    public long calcularTiempoEmpaquetado() {
        if (peso <= 2) {
            return 1000;
        }
        if (peso <= 5) {
            return 2000;
        }
        return 3000;
    }

    public double calcularSegundosDesdeCreacion() {
        return (System.currentTimeMillis() - horaCreacion) / 1000.0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public double getPeso() {
        return peso;
    }

    public PrioridadTipo getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        return codigo;
    }
}
