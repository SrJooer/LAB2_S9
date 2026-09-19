package org.example.estructura;

public class ListaNodos {

    private Nodo cabeza;
    private Nodo cola;
    private int tamanio;

    public ListaNodos() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    public void insertar(Object valor) {
        Nodo nuevoNodo = new Nodo(valor);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.setSiguiente(nuevoNodo);
            nuevoNodo.setAnterior(cola);
            cola = nuevoNodo;
        }
        tamanio++;
    }

    public void eliminar() {
        if (cabeza != null) {
            if (cabeza == cola) {
                cabeza = null;
                cola = null;
            } else {
                cola = cola.getAnterior();
                cola.setSiguiente(null);
            }
            tamanio--;
        }
    }

    public int getTamanio() {
        return tamanio;
    }

    public Nodo getPrimero() {
        return cabeza;
    }

    public Nodo getUltimo() {
        return cola;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public void imprimir() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getValor());
            actual = actual.getSiguiente();
        }
    }

    public void recorrer() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getValor());
        }
    }
}
