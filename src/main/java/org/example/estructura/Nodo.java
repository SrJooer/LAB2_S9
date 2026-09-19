package org.example.estructura;

public class Nodo {

    private Object valor;
    private Nodo siguiente;
    private Nodo anterior;

    public Nodo(Object valor) {
        this.valor = valor;
    }

    public Nodo(Object valor, Nodo siguiente, Nodo anterior) {
        this.valor = valor;
        this.siguiente = siguiente;
        this.anterior = anterior;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public void setAnterior(Nodo anterior) {
        this.anterior = anterior;
    }

    public Object getValor() {
        return valor;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }
    public Nodo getAnterior() {
        return anterior;
    }
}
