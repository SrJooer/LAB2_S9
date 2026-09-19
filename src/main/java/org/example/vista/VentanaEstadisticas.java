package org.example.vista;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.modelo.Sistema;

public class VentanaEstadisticas extends Stage {

    private static final double ANCHO = 420;
    private static final double ALTO = 300;

    private final PanelEstadisticas panel;

    public VentanaEstadisticas(Sistema sistema) {
        panel = new PanelEstadisticas(sistema);
        VBox contenido = new VBox(panel);
        contenido.getStyleClass().add("ventana");
        contenido.setPadding(new Insets(16));

        Scene escena = new Scene(contenido, ANCHO, ALTO);
        escena.getStylesheets().add(Estilos.obtenerHoja());
        setTitle("Estadísticas");
        setScene(escena);
    }

    public void mostrar() {
        panel.actualizar();
        show();
        toFront();
    }

    public void actualizar() {
        if (isShowing()) {
            panel.actualizar();
        }
    }
}
