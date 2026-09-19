package org.example.hilos;

import java.util.Random;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.excepciones.CapacidadExcedidaException;
import org.example.excepciones.PaqueteException;
import org.example.excepciones.TransicionInvalidaException;
import org.example.modelo.Estadisticas;
import org.example.modelo.Sistema;
import org.example.modelo.Zona;
import org.example.modelo.instancias.EstadoRepartidor;
import org.example.modelo.instancias.EstadoTipo;
import org.example.modelo.instancias.Paquete;
import org.example.modelo.instancias.Ruta;

public class Repartidor extends Trabajador {

    private static final int INTENTOS_MAXIMOS = 3;
    private static final int ESPERAS_MAXIMAS = 3;
    private static final int PROBABILIDAD_AUSENTE = 30;
    private static final long TIEMPO_ESPERA_CARGA = 2000;
    private static final long TIEMPO_VIAJE = 2000;
    private static final long TIEMPO_ENTREGA = 1000;
    private static final long TIEMPO_REGRESO = 1200;

    private final String conductor;
    private final Ruta ruta;
    private final int capacidad;
    private final ListaNodos<Paquete> listaReparto = new ListaNodos<>();
    private final Zona expedicion;
    private final Zona entregados;
    private final Zona devueltos;
    private final Estadisticas estadisticas;
    private final Random azar = new Random();
    private EstadoRepartidor estado = EstadoRepartidor.DISPONIBLE;
    private int paquetesEntregados;

    public Repartidor(String nombre, String conductor, Ruta ruta, int capacidad, Sistema sistema) {
        super(nombre, sistema);
        this.conductor = conductor;
        this.ruta = ruta;
        this.capacidad = capacidad;
        this.expedicion = sistema.getExpedicion();
        this.entregados = sistema.getEntregados();
        this.devueltos = sistema.getDevueltos();
        this.estadisticas = sistema.getEstadisticas();
    }

    @Override
    protected void trabajar() throws InterruptedException, PaqueteException {
        cargarVehiculo();
        salirDeRuta();
        entregarPaquetes();
        regresar();
    }

    private void cargarVehiculo() throws InterruptedException, PaqueteException {
        cambiarEstado(EstadoRepartidor.DISPONIBLE);
        Paquete primero = expedicion.sacarDeRuta(ruta);
        cambiarEstado(EstadoRepartidor.CARGANDO);
        cargar(primero);
        int esperas = 0;
        while (!estaLleno() && esperas < ESPERAS_MAXIMAS) {
            Paquete paquete = expedicion.sacarDeRutaSiHay(ruta);
            if (paquete == null) {
                esperas++;
                dormir(TIEMPO_ESPERA_CARGA);
            } else {
                cargar(paquete);
            }
        }
    }

    private synchronized void cargar(Paquete paquete) throws CapacidadExcedidaException {
        if (estaLleno()) {
            throw new CapacidadExcedidaException(getNombre(), capacidad);
        }
        listaReparto.insertar(paquete);
        registrar(paquete.getCodigo() + " cargado en " + getNombre() + " (" + listaReparto.getTamanio() + "/" + capacidad + ")");
    }

    private void salirDeRuta() throws InterruptedException, TransicionInvalidaException {
        cambiarEstado(EstadoRepartidor.EN_RUTA);
        marcarEnReparto(listaReparto.getPrimero());
        int cantidad = contarCarga();
        String unidad = cantidad == 1 ? "paquete" : "paquetes";
        registrar(getNombre() + " sale por " + ruta.getEtiqueta() + " con " + cantidad + " " + unidad);
        dormir(TIEMPO_VIAJE);
    }

    private void marcarEnReparto(Nodo<Paquete> actual) throws TransicionInvalidaException {
        if (actual == null) {
            return;
        }
        actual.getValor().cambiarEstado(EstadoTipo.EN_REPARTO);
        marcarEnReparto(actual.getSiguiente());
    }

    private void entregarPaquetes() throws InterruptedException, PaqueteException {
        while (!listaReparto.estaVacia()) {
            Paquete paquete = listaReparto.getPrimero().getValor();
            setPaqueteActual(paquete);
            cambiarEstado(EstadoRepartidor.ENTREGANDO);
            dormir(TIEMPO_ENTREGA);
            if (clienteAusente()) {
                registrarIntentoFallido(paquete);
            } else {
                entregar(paquete);
            }
            setPaqueteActual(null);
            cambiarEstado(EstadoRepartidor.EN_RUTA);
        }
    }

    private boolean clienteAusente() {
        return azar.nextInt(100) < PROBABILIDAD_AUSENTE;
    }

    private void entregar(Paquete paquete) throws InterruptedException, PaqueteException {
        paquete.cambiarEstado(EstadoTipo.ENTREGADO);
        descargar(paquete);
        entregados.meter(paquete);
        estadisticas.registrarEntrega(paquete.calcularSegundosDesdeCreacion());
        sumarEntrega();
        registrar(paquete.getCodigo() + " entregado a " + paquete.getNombreCliente() + " por " + getNombre());
    }

    private void registrarIntentoFallido(Paquete paquete) throws InterruptedException, PaqueteException {
        paquete.sumarIntento();
        paquete.cambiarEstado(EstadoTipo.NUEVO_INTENTO);
        registrar(paquete.getCodigo() + " intento " + paquete.getIntentos() + ": cliente ausente");
        if (paquete.getIntentos() >= INTENTOS_MAXIMOS) {
            paquete.cambiarEstado(EstadoTipo.DEVUELTO);
            descargar(paquete);
            devueltos.meter(paquete);
            estadisticas.registrarDevolucion();
            registrar(paquete.getCodigo() + " devuelto tras " + INTENTOS_MAXIMOS + " intentos");
        } else {
            paquete.cambiarEstado(EstadoTipo.EN_REPARTO);
            moverAlFinal(paquete);
        }
    }

    private void regresar() throws InterruptedException {
        cambiarEstado(EstadoRepartidor.REGRESANDO);
        registrar(getNombre() + " regresa al centro");
        dormir(TIEMPO_REGRESO);
    }

    @Override
    protected void terminar() {
        super.terminar();
        cambiarEstado(EstadoRepartidor.FUERA_DE_SERVICIO);
    }

    private synchronized void descargar(Paquete paquete) {
        listaReparto.eliminar(paquete);
    }

    private synchronized void moverAlFinal(Paquete paquete) {
        listaReparto.eliminar(paquete);
        listaReparto.insertar(paquete);
    }

    private synchronized void cambiarEstado(EstadoRepartidor nuevoEstado) {
        estado = nuevoEstado;
    }

    private synchronized void sumarEntrega() {
        paquetesEntregados++;
    }

    public synchronized boolean estaLleno() {
        return listaReparto.getTamanio() >= capacidad;
    }

    public synchronized int contarCarga() {
        return listaReparto.getTamanio();
    }

    public synchronized ListaNodos<Paquete> copiarCarga() {
        return listaReparto.copiar();
    }

    public synchronized EstadoRepartidor getEstado() {
        return estado;
    }

    public synchronized int getPaquetesEntregados() {
        return paquetesEntregados;
    }

    public String getConductor() {
        return conductor;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public int getCapacidad() {
        return capacidad;
    }
}
