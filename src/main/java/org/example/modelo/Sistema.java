package org.example.modelo;

import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.excepciones.SimulacionException;
import org.example.hilos.Almacenero;
import org.example.hilos.Clasificador;
import org.example.hilos.Empaquetador;
import org.example.hilos.Recepcionista;
import org.example.hilos.Repartidor;
import org.example.hilos.Trabajador;
import org.example.modelo.instancias.Ruta;

public class Sistema {

    private static final int CANTIDAD_CLASIFICADORES = 3;
    private static final int CANTIDAD_EMPAQUETADORES = 2;

    private final Zona recepcion = new Zona("Recepción", 10);
    private final Zona almacen = new Zona("Almacén", 20);
    private final Zona clasificacion = new Zona("Clasificación", 10);
    private final Zona empaquetado = new Zona("Empaquetado", 8);
    private final Zona expedicion = new Zona("Expedición", 15);
    private final Zona entregados = new Zona("Entregados", Zona.SIN_LIMITE);
    private final Zona devueltos = new Zona("Devueltos", Zona.SIN_LIMITE);

    private final Registro registro = new Registro();
    private final Estadisticas estadisticas = new Estadisticas();
    private final ControlSimulacion control = new ControlSimulacion();
    private final GeneradorPaquetes generador = new GeneradorPaquetes();

    private ListaNodos<Trabajador> trabajadores = new ListaNodos<>();
    private ListaNodos<Trabajador> almaceneros = new ListaNodos<>();
    private ListaNodos<Trabajador> clasificadores = new ListaNodos<>();
    private ListaNodos<Trabajador> empaquetadores = new ListaNodos<>();
    private ListaNodos<Repartidor> repartidores = new ListaNodos<>();

    public void iniciar() throws SimulacionException {
        if (control.estaActivo()) {
            throw new SimulacionException("La simulación ya está en marcha");
        }
        if (!trabajadores.estaVacia()) {
            throw new SimulacionException("La simulación se detuvo; usa Reiniciar para empezar de nuevo");
        }
        control.iniciar();
        crearTrabajadores();
        registro.escribir("Simulación iniciada");
        arrancarDesde(trabajadores.getPrimero());
    }

    public void pausar() throws SimulacionException {
        if (!control.estaActivo()) {
            throw new SimulacionException("No hay ninguna simulación en marcha");
        }
        if (control.estaPausado()) {
            throw new SimulacionException("La simulación ya está en pausa");
        }
        control.pausar();
        registro.escribir("Simulación en pausa");
    }

    public void reanudar() throws SimulacionException {
        if (!control.estaPausado()) {
            throw new SimulacionException("La simulación no está en pausa");
        }
        control.reanudar();
        registro.escribir("Simulación reanudada");
    }

    public void detener() throws SimulacionException {
        if (!control.estaActivo()) {
            throw new SimulacionException("No hay ninguna simulación en marcha");
        }
        control.detener();
        interrumpirDesde(trabajadores.getPrimero());
        esperarFinDesde(trabajadores.getPrimero());
        registro.escribir("Simulación detenida");
    }

    public void reiniciar() throws SimulacionException {
        if (control.estaActivo()) {
            detener();
        }
        limpiar();
        registro.limpiar();
        registro.escribir("Sistema reiniciado");
        iniciar();
    }

    private void crearTrabajadores() {
        trabajadores = new ListaNodos<>();
        almaceneros = new ListaNodos<>();
        clasificadores = new ListaNodos<>();
        empaquetadores = new ListaNodos<>();
        repartidores = new ListaNodos<>();

        trabajadores.insertar(new Recepcionista(this));

        Almacenero almacenero = new Almacenero(this);
        almaceneros.insertar(almacenero);
        trabajadores.insertar(almacenero);

        for (int numero = 1; numero <= CANTIDAD_CLASIFICADORES; numero++) {
            Clasificador clasificador = new Clasificador("Clasificador " + numero, this);
            clasificadores.insertar(clasificador);
            trabajadores.insertar(clasificador);
        }

        for (int numero = 1; numero <= CANTIDAD_EMPAQUETADORES; numero++) {
            Empaquetador empaquetador = new Empaquetador("Empaquetador " + numero, this);
            empaquetadores.insertar(empaquetador);
            trabajadores.insertar(empaquetador);
        }

        agregarRepartidor(new Repartidor("Repartidor 1", "Marta", Ruta.RUTA_1, 5, this));
        agregarRepartidor(new Repartidor("Repartidor 2", "Jordi", Ruta.RUTA_2, 4, this));
        agregarRepartidor(new Repartidor("Repartidor 3", "Laura", Ruta.RUTA_3, 6, this));
        agregarRepartidor(new Repartidor("Repartidor 4", "Pau", Ruta.RUTA_4, 5, this));
    }

    private void agregarRepartidor(Repartidor repartidor) {
        repartidores.insertar(repartidor);
        trabajadores.insertar(repartidor);
    }

    private void arrancarDesde(Nodo<Trabajador> actual) {
        if (actual == null) {
            return;
        }
        actual.getValor().arrancar();
        arrancarDesde(actual.getSiguiente());
    }

    private void interrumpirDesde(Nodo<Trabajador> actual) {
        if (actual == null) {
            return;
        }
        actual.getValor().interrumpir();
        interrumpirDesde(actual.getSiguiente());
    }

    private void esperarFinDesde(Nodo<Trabajador> actual) {
        if (actual == null) {
            return;
        }
        try {
            actual.getValor().esperarFin();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        esperarFinDesde(actual.getSiguiente());
    }

    private void limpiar() {
        trabajadores = new ListaNodos<>();
        recepcion.vaciar();
        almacen.vaciar();
        clasificacion.vaciar();
        empaquetado.vaciar();
        expedicion.vaciar();
        entregados.vaciar();
        devueltos.vaciar();
        estadisticas.reiniciar();
        generador.reiniciar();
    }

    public int contarPendientes() {
        return recepcion.contar() + almacen.contar();
    }

    public int contarEnProceso() {
        int finalizados = estadisticas.getEntregados() + estadisticas.getDevueltos();
        return estadisticas.getGenerados() - finalizados - contarPendientes();
    }

    public boolean estaEnMarcha() {
        return control.estaActivo();
    }

    public boolean puedeIniciar() {
        return !control.estaActivo() && trabajadores.estaVacia();
    }

    public boolean estaEnPausa() {
        return control.estaPausado();
    }

    public Zona getRecepcion() {
        return recepcion;
    }

    public Zona getAlmacen() {
        return almacen;
    }

    public Zona getClasificacion() {
        return clasificacion;
    }

    public Zona getEmpaquetado() {
        return empaquetado;
    }

    public Zona getExpedicion() {
        return expedicion;
    }

    public Zona getEntregados() {
        return entregados;
    }

    public Zona getDevueltos() {
        return devueltos;
    }

    public Registro getRegistro() {
        return registro;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public ControlSimulacion getControl() {
        return control;
    }

    public GeneradorPaquetes getGenerador() {
        return generador;
    }

    public ListaNodos<Trabajador> getAlmaceneros() {
        return almaceneros;
    }

    public ListaNodos<Trabajador> getClasificadores() {
        return clasificadores;
    }

    public ListaNodos<Trabajador> getEmpaquetadores() {
        return empaquetadores;
    }

    public ListaNodos<Repartidor> getRepartidores() {
        return repartidores;
    }
}
