package org.example.vista;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.hilos.Trabajador;
import org.example.modelo.Zona;
import org.example.modelo.instancias.Paquete;

public class PanelZona extends Tarjeta {

    private final Zona zona;
    private final Label contador = crearTextoCentrado("texto");
    private final ProgressBar barra = new ProgressBar(0);
    private final FlowPane paquetes = new FlowPane(8, 4);
    private final VBox trabajadores = new VBox(2);
    private String claveAnterior = "";

    public PanelZona(Zona zona, boolean muestraTrabajadores) {
        super(zona.getNombre());
        this.zona = zona;
        barra.getStyleClass().add("barra-progreso");
        barra.setMaxWidth(Double.MAX_VALUE);
        getChildren().addAll(contador, barra, paquetes);
        if (muestraTrabajadores) {
            trabajadores.getStyleClass().add("lista-trabajadores");
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
            Label fila = new Label(describir(nodo.getValor()));
            fila.getStyleClass().add("texto");
            trabajadores.getChildren().add(fila);
            nodo = nodo.getSiguiente();
        }
    }

    private String describir(Trabajador trabajador) {
        Paquete paquete = trabajador.getPaqueteActual();
        if (paquete == null) {
            return trabajador.getNombre() + ": libre";
        }
        return trabajador.getNombre() + ": " + paquete.getCodigo() + " (" + paquete.getEstado().getEtiqueta() + ")";
    }
}
