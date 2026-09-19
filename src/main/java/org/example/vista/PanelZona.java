package org.example.vista;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.hilos.Trabajador;
import org.example.modelo.Zona;
import org.example.modelo.instancias.Paquete;

public class PanelZona extends Tarjeta {

    private final Zona zona;
    private final Label contador = crearTextoCentrado("texto-suave");
    private final ProgressBar barra = new ProgressBar(0);
    private final FlowPane paquetes = new FlowPane(6, 6);
    private final VBox trabajadores = new VBox(2);
    private String claveAnterior = "";

    public PanelZona(Zona zona, boolean muestraTrabajadores) {
        super(zona.getNombre());
        this.zona = zona;
        barra.getStyleClass().add("barra-progreso");
        barra.setMaxWidth(Double.MAX_VALUE);
        getChildren().addAll(contador, barra, paquetes);
        if (muestraTrabajadores) {
            getChildren().add(trabajadores);
        }
    }

    public void actualizar() {
        ListaNodos<Paquete> copia = zona.copiar();
        contador.setText("Paquetes: " + copia.getTamanio() + " / " + zona.getCapacidad());
        barra.setProgress((double) copia.getTamanio() / zona.getCapacidad());
        String clave = EtiquetaPaquete.construirClave(copia);
        if (clave.equals(claveAnterior)) {
            return;
        }
        claveAnterior = clave;
        paquetes.getChildren().clear();
        Nodo<Paquete> nodo = copia.getPrimero();
        while (nodo != null) {
            paquetes.getChildren().add(new EtiquetaPaquete(nodo.getValor()));
            nodo = nodo.getSiguiente();
        }
    }

    public void mostrarTrabajadores(ListaNodos<Trabajador> lista) {
        trabajadores.getChildren().clear();
        Nodo<Trabajador> nodo = lista.getPrimero();
        while (nodo != null) {
            trabajadores.getChildren().add(crearFila(nodo.getValor()));
            nodo = nodo.getSiguiente();
        }
    }

    private HBox crearFila(Trabajador trabajador) {
        Label nombre = new Label(trabajador.getNombre());
        nombre.getStyleClass().add("nombre-trabajador");
        Label detalle = new Label(describir(trabajador.getPaqueteActual()));
        detalle.getStyleClass().add("estado-trabajador");
        HBox fila = new HBox(nombre, detalle);
        fila.getStyleClass().add("fila-trabajador");
        return fila;
    }

    private String describir(Paquete paquete) {
        if (paquete == null) {
            return "Libre";
        }
        return paquete.getCodigo() + " · " + paquete.getEstado().getEtiqueta();
    }
}
