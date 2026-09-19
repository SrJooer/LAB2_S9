package org.example.vista;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.modelo.instancias.Paquete;
import org.example.modelo.instancias.PrioridadTipo;

public class EtiquetaPaquete extends HBox {

    public EtiquetaPaquete(Paquete paquete) {
        getStyleClass().add("etiqueta-paquete");
        Circle punto = new Circle(4, colorDe(paquete.getPrioridad()));
        Label codigo = new Label(paquete.getCodigo());
        getChildren().addAll(punto, codigo);
        Tooltip.install(this, new Tooltip(describir(paquete)));
    }

    private String describir(Paquete paquete) {
        return paquete.getNombreCliente() + "\n"
                + paquete.getDireccion() + ", " + paquete.getCiudad() + "\n"
                + paquete.getPeso() + " kg · " + paquete.getPrioridad().getEtiqueta() + "\n"
                + paquete.getEstado().getEtiqueta();
    }

    public static String construirClave(ListaNodos<Paquete> paquetes) {
        return construirClaveDesde(paquetes.getPrimero(), "");
    }

    private static String construirClaveDesde(Nodo<Paquete> actual, String clave) {
        if (actual == null) {
            return clave;
        }
        Paquete paquete = actual.getValor();
        return construirClaveDesde(actual.getSiguiente(), clave + paquete.getCodigo() + paquete.getEstado() + ";");
    }

    public static Color colorDe(PrioridadTipo prioridad) {
        switch (prioridad) {
            case URGENTE:
                return Color.web("#DC2626");
            case ALTA:
                return Color.web("#F59E0B");
            case NORMAL:
                return Color.web("#2563EB");
            default:
                return Color.web("#9CA3AF");
        }
    }
}
