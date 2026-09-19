package org.example.vista;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Tarjeta extends VBox {

    protected final Label titulo;

    public Tarjeta(String texto) {
        getStyleClass().add("tarjeta");
        titulo = new Label(texto.toUpperCase());
        titulo.getStyleClass().add("titulo-tarjeta");
        titulo.setMaxWidth(Double.MAX_VALUE);
        titulo.setAlignment(Pos.CENTER);
        getChildren().add(titulo);
    }

    protected Label crearTextoCentrado(String estilo) {
        Label etiqueta = new Label();
        etiqueta.getStyleClass().add(estilo);
        etiqueta.setMaxWidth(Double.MAX_VALUE);
        etiqueta.setAlignment(Pos.CENTER);
        return etiqueta;
    }
}
