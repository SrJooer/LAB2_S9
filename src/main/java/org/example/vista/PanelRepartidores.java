package org.example.vista;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.hilos.Repartidor;
import org.example.modelo.instancias.EstadoRepartidor;
import org.example.modelo.instancias.Paquete;

public class PanelRepartidores extends Tarjeta {

    private final HBox tarjetas = new HBox(10);
    private String claveAnterior = "";

    public PanelRepartidores() {
        super("Repartidores");
        getChildren().add(tarjetas);
    }

    public void mostrar(ListaNodos<Repartidor> repartidores) {
        String clave = construirClave(repartidores.getPrimero(), "");
        if (clave.equals(claveAnterior)) {
            return;
        }
        claveAnterior = clave;
        tarjetas.getChildren().clear();
        Nodo<Repartidor> nodo = repartidores.getPrimero();
        while (nodo != null) {
            tarjetas.getChildren().add(crearTarjeta(nodo.getValor()));
            nodo = nodo.getSiguiente();
        }
    }

    private String construirClave(Nodo<Repartidor> actual, String clave) {
        if (actual == null) {
            return clave;
        }
        Repartidor repartidor = actual.getValor();
        String detalle = repartidor.getNombre() + describirEstado(repartidor) + repartidor.getPaquetesEntregados()
                + EtiquetaPaquete.construirClave(repartidor.copiarCarga()) + "|";
        return construirClave(actual.getSiguiente(), clave + detalle);
    }

    private VBox crearTarjeta(Repartidor repartidor) {
        Label nombre = new Label(repartidor.getNombre() + " · " + repartidor.getConductor());
        nombre.getStyleClass().add("nombre-repartidor");

        Label ruta = new Label(repartidor.getRuta().getEtiqueta());
        ruta.getStyleClass().add("texto-suave");

        Circle punto = new Circle(4, colorDe(repartidor.getEstado()));
        Label estado = new Label(describirEstado(repartidor));
        estado.getStyleClass().add("estado-trabajador");
        HBox filaEstado = new HBox(6, punto, estado);
        filaEstado.setAlignment(Pos.CENTER_LEFT);

        int carga = repartidor.contarCarga();
        Label capacidad = new Label("Carga " + carga + " / " + repartidor.getCapacidad());
        capacidad.getStyleClass().add("texto-suave");
        ProgressBar barra = new ProgressBar((double) carga / repartidor.getCapacidad());
        barra.getStyleClass().add("barra-progreso");
        barra.setMaxWidth(Double.MAX_VALUE);

        Label entregados = new Label("Entregados: " + repartidor.getPaquetesEntregados());
        entregados.getStyleClass().add("texto-suave");

        VBox tarjeta = new VBox(4, nombre, ruta, filaEstado, capacidad, barra, crearCarga(repartidor), entregados);
        tarjeta.getStyleClass().add("tarjeta-repartidor");
        HBox.setHgrow(tarjeta, Priority.ALWAYS);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        return tarjeta;
    }

    private FlowPane crearCarga(Repartidor repartidor) {
        FlowPane carga = new FlowPane(4, 4);
        Nodo<Paquete> nodo = repartidor.copiarCarga().getPrimero();
        while (nodo != null) {
            carga.getChildren().add(new EtiquetaPaquete(nodo.getValor()));
            nodo = nodo.getSiguiente();
        }
        return carga;
    }

    private String describirEstado(Repartidor repartidor) {
        Paquete actual = repartidor.getPaqueteActual();
        if (actual == null) {
            return repartidor.getEstado().getEtiqueta();
        }
        return repartidor.getEstado().getEtiqueta() + " " + actual.getCodigo();
    }

    private Color colorDe(EstadoRepartidor estado) {
        switch (estado) {
            case DISPONIBLE:
                return Color.web("#16A34A");
            case CARGANDO:
                return Color.web("#F59E0B");
            case EN_RUTA:
                return Color.web("#2563EB");
            case ENTREGANDO:
                return Color.web("#7C3AED");
            case REGRESANDO:
                return Color.web("#0891B2");
            default:
                return Color.web("#9CA3AF");
        }
    }
}
