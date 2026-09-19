package org.example.vista;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.modelo.Zona;
import org.example.modelo.instancias.Paquete;
import org.example.modelo.instancias.Ruta;

public class PanelExpedicion extends Tarjeta {

    private final Zona expedicion;
    private final Label contador = crearTextoCentrado("texto");
    private final VBox[] columnas = new VBox[Ruta.values().length];
    private final Label[] cabeceras = new Label[Ruta.values().length];
    private String claveAnterior = "";

    public PanelExpedicion(Zona expedicion) {
        super(expedicion.getNombre());
        this.expedicion = expedicion;
        HBox rutas = new HBox(16);
        for (int posicion = 0; posicion < columnas.length; posicion++) {
            rutas.getChildren().add(crearColumna(posicion));
        }
        getChildren().addAll(contador, rutas);
    }

    private VBox crearColumna(int posicion) {
        cabeceras[posicion] = new Label();
        cabeceras[posicion].getStyleClass().add("texto-negrita");
        columnas[posicion] = new VBox(2);
        VBox columna = new VBox(4, cabeceras[posicion], columnas[posicion]);
        HBox.setHgrow(columna, Priority.ALWAYS);
        columna.setMaxWidth(Double.MAX_VALUE);
        return columna;
    }

    public void actualizar() {
        ListaNodos<Paquete> copia = expedicion.copiar();
        contador.setText("Paquetes: " + copia.getTamanio() + " / " + expedicion.getCapacidad());
        String clave = EtiquetaPaquete.construirClave(copia);
        if (clave.equals(claveAnterior)) {
            return;
        }
        claveAnterior = clave;
        for (int posicion = 0; posicion < columnas.length; posicion++) {
            columnas[posicion].getChildren().clear();
        }
        Nodo<Paquete> nodo = copia.getPrimero();
        while (nodo != null) {
            int posicion = nodo.getValor().getRuta().ordinal();
            columnas[posicion].getChildren().add(new EtiquetaPaquete(nodo.getValor()));
            nodo = nodo.getSiguiente();
        }
        for (int posicion = 0; posicion < columnas.length; posicion++) {
            int cantidad = columnas[posicion].getChildren().size();
            cabeceras[posicion].setText(Ruta.values()[posicion].getEtiqueta() + ": " + cantidad);
        }
    }
}
