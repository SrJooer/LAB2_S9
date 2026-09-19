package org.example.modelo;

import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.excepciones.PaqueteNoEncontradoException;
import org.example.modelo.instancias.EstadoTipo;
import org.example.modelo.instancias.Paquete;
import org.example.modelo.instancias.Ruta;

public class Zona {

    public static final int SIN_LIMITE = -1;

    private final String nombre;
    private final int capacidad;
    private final ListaNodos<Paquete> paquetes = new ListaNodos<>();

    public Zona(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public synchronized void meter(Paquete paquete) throws InterruptedException {
        while (estaLlena()) {
            wait();
        }
        paquetes.insertar(paquete);
        notifyAll();
    }

    public synchronized Paquete sacarMasPrioritario(EstadoTipo estado) throws InterruptedException {
        return sacarEsperando(estado, null);
    }

    public synchronized Paquete sacarDeRuta(Ruta ruta) throws InterruptedException {
        return sacarEsperando(null, ruta);
    }

    public synchronized Paquete sacarDeRutaSiHay(Ruta ruta) {
        Paquete paquete = buscarMasPrioritario(paquetes.getPrimero(), null, ruta, null);
        if (paquete != null) {
            paquetes.eliminar(paquete);
            notifyAll();
        }
        return paquete;
    }

    public synchronized void sacar(Paquete paquete) throws PaqueteNoEncontradoException {
        if (!paquetes.eliminar(paquete)) {
            throw new PaqueteNoEncontradoException(paquete.getCodigo(), nombre);
        }
        notifyAll();
    }

    public synchronized void avisarCambio() {
        notifyAll();
    }

    private Paquete sacarEsperando(EstadoTipo estado, Ruta ruta) throws InterruptedException {
        Paquete paquete = buscarMasPrioritario(paquetes.getPrimero(), estado, ruta, null);
        while (paquete == null) {
            wait();
            paquete = buscarMasPrioritario(paquetes.getPrimero(), estado, ruta, null);
        }
        paquetes.eliminar(paquete);
        notifyAll();
        return paquete;
    }

    private Paquete buscarMasPrioritario(Nodo<Paquete> actual, EstadoTipo estado, Ruta ruta, Paquete mejor) {
        if (actual == null) {
            return mejor;
        }
        Paquete candidato = actual.getValor();
        if (cumpleFiltro(candidato, estado, ruta) && esMejorQue(candidato, mejor)) {
            mejor = candidato;
        }
        return buscarMasPrioritario(actual.getSiguiente(), estado, ruta, mejor);
    }

    private boolean cumpleFiltro(Paquete paquete, EstadoTipo estado, Ruta ruta) {
        boolean estadoCorrecto = estado == null || paquete.getEstado() == estado;
        boolean rutaCorrecta = ruta == null || paquete.getRuta() == ruta;
        return estadoCorrecto && rutaCorrecta;
    }

    private boolean esMejorQue(Paquete candidato, Paquete mejor) {
        return mejor == null || candidato.getPrioridad().esMasPrioritariaQue(mejor.getPrioridad());
    }

    public synchronized boolean estaLlena() {
        return capacidad != SIN_LIMITE && paquetes.getTamanio() >= capacidad;
    }

    public synchronized int contar() {
        return paquetes.getTamanio();
    }

    public synchronized ListaNodos<Paquete> copiar() {
        return paquetes.copiar();
    }

    public synchronized void vaciar() {
        paquetes.vaciar();
        notifyAll();
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }
}
