package org.example.vista;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.modelo.EscuchaRegistro;

public class PanelRegistro extends Tarjeta implements EscuchaRegistro {

    private static final int LINEAS_MAXIMAS = 400;

    private final TextArea area = new TextArea();

    public PanelRegistro() {
        super("Registro del sistema");
        area.getStyleClass().add("area-registro");
        area.setEditable(false);
        area.setWrapText(false);
        VBox.setVgrow(area, Priority.ALWAYS);
        getChildren().add(area);
    }

    @Override
    public void nuevaLinea(String linea) {
        Platform.runLater(() -> {
            area.appendText(linea + "\n");
            recortarSiHaceFalta();
            area.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void recortarSiHaceFalta() {
        if (area.getParagraphs().size() > LINEAS_MAXIMAS) {
            int finPrimeraLinea = area.getText().indexOf('\n') + 1;
            area.deleteText(0, finPrimeraLinea);
        }
    }

    @Override
    public void limpiar() {
        Platform.runLater(() -> {
            area.clear();
        });
    }
}
