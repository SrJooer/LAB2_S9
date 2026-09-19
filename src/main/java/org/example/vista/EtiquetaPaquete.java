package org.example.vista;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.modelo.instancias.Paquete;

public class EtiquetaPaquete extends Label {

    private static final double ANCHO = 130;

    public EtiquetaPaquete(Paquete paquete) {
        super(String.format("%-8s %s", paquete.getCodigo(), paquete.getPrioridad().getEtiqueta()));
        getStyleClass().add("etiqueta-paquete");
        setPrefWidth(ANCHO);
        setTooltip(new Tooltip(describir(paquete)));
    }

    private String describir(Paquete paquete) {
        return "Cliente: " + paquete.getNombreCliente() + "\n"
                + "Dirección: " + paquete.getDireccion() + ", " + paquete.getCiudad() + "\n"
                + "Peso: " + paquete.getPeso() + " kg\n"
                + "Prioridad: " + paquete.getPrioridad().getEtiqueta() + "\n"
                + "Estado: " + paquete.getEstado().getEtiqueta() + "\n"
                + "Intentos: " + paquete.getIntentos();
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
}
