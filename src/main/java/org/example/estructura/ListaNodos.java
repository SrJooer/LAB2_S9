package org.example.estructura;

public class ListaNodos<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    public void insertar(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor);
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

    public boolean eliminar(T valor) {
        Nodo<T> nodo = buscar(valor);
        if (nodo == null) {
            return false;
        }
        desenlazar(nodo);
        return true;
    }

    private void desenlazar(Nodo<T> nodo) {
        Nodo<T> anterior = nodo.getAnterior();
        Nodo<T> siguiente = nodo.getSiguiente();
        if (anterior == null) {
            cabeza = siguiente;
        } else {
            anterior.setSiguiente(siguiente);
        }
        if (siguiente == null) {
            cola = anterior;
        } else {
            siguiente.setAnterior(anterior);
        }
        nodo.setSiguiente(null);
        nodo.setAnterior(null);
        tamanio--;
    }

    public Nodo<T> buscar(T valor) {
        return buscarDesde(cabeza, valor);
    }

    private Nodo<T> buscarDesde(Nodo<T> actual, T valor) {
        if (actual == null) {
            return null;
        }
        if (actual.getValor().equals(valor)) {
            return actual;
        }
        return buscarDesde(actual.getSiguiente(), valor);
    }

    public T obtener(int indice) {
        return obtenerDesde(cabeza, indice);
    }

    private T obtenerDesde(Nodo<T> actual, int indice) {
        if (actual == null) {
            return null;
        }
        if (indice == 0) {
            return actual.getValor();
        }
        return obtenerDesde(actual.getSiguiente(), indice - 1);
    }

    public boolean contiene(T valor) {
        return buscar(valor) != null;
    }

    public ListaNodos<T> copiar() {
        ListaNodos<T> copia = new ListaNodos<>();
        copiarDesde(cabeza, copia);
        return copia;
    }

    private void copiarDesde(Nodo<T> actual, ListaNodos<T> copia) {
        if (actual == null) {
            return;
        }
        copia.insertar(actual.getValor());
        copiarDesde(actual.getSiguiente(), copia);
    }

    public void recorrer() {
        recorrerDesde(cabeza);
    }

    private void recorrerDesde(Nodo<T> actual) {
        if (actual == null) {
            return;
        }
        System.out.println(actual.getValor());
        recorrerDesde(actual.getSiguiente());
    }

    public void vaciar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    public int getTamanio() {
        return tamanio;
    }

    public Nodo<T> getPrimero() {
        return cabeza;
    }

    public Nodo<T> getUltimo() {
        return cola;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }
}
