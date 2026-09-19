package org.example.vista;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.hilos.Repartidor;
import org.example.modelo.instancias.Paquete;

public class PanelRepartidores extends Tarjeta {

    private final HBox columnas = new HBox(16);
    private String claveAnterior = "";

    public PanelRepartidores() {
        super("Repartidores");
        getChildren().add(columnas);
    }

    public void mostrar(ListaNodos<Repartidor> repartidores) {
        String clave = construirClave(repartidores.getPrimero(), "");
        if (clave.equals(claveAnterior)) {
            return;
        }
        claveAnterior = clave;
        columnas.getChildren().clear();
        Nodo<Repartidor> nodo = repartidores.getPrimero();
        while (nodo != null) {
            columnas.getChildren().add(crearColumna(nodo.getValor()));
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

    private VBox crearColumna(Repartidor repartidor) {
        Label nombre = new Label(repartidor.getNombre() + " (" + repartidor.getConductor() + ")");
        nombre.getStyleClass().add("texto-negrita");
        Label ruta = crearTexto("Ruta: " + repartidor.getRuta().getEtiqueta());
        Label estado = crearTexto("Estado: " + describirEstado(repartidor));
        int carga = repartidor.contarCarga();
        Label capacidad = crearTexto("Carga: " + carga + " / " + repartidor.getCapacidad());
        ProgressBar barra = new ProgressBar((double) carga / repartidor.getCapacidad());
        barra.getStyleClass().add("barra-progreso");
        barra.setMaxWidth(Double.MAX_VALUE);
        Label entregados = crearTexto("Entregados: " + repartidor.getPaquetesEntregados());

        VBox columna = new VBox(3, nombre, ruta, estado, capacidad, barra, crearCarga(repartidor), entregados);
        HBox.setHgrow(columna, Priority.ALWAYS);
        columna.setMaxWidth(Double.MAX_VALUE);
        return columna;
    }

    private Label crearTexto(String contenido) {
        Label etiqueta = new Label(contenido);
        etiqueta.getStyleClass().add("texto");
        return etiqueta;
    }

    private FlowPane crearCarga(Repartidor repartidor) {
        FlowPane carga = new FlowPane(8, 2);
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
}
