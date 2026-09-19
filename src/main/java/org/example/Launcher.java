package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.modelo.Sistema;
import org.example.vista.Estilos;
import org.example.vista.VentanaPrincipal;

public class Launcher extends Application {

    private static final String TITULO = "Sistema de paquetería";
    private static final double ANCHO = 1280;
    private static final double ALTO = 940;

    @Override
    public void start(Stage escenario) {
        Sistema sistema = new Sistema();
        VentanaPrincipal ventana = new VentanaPrincipal(sistema);

        Scene escena = new Scene(ventana, ANCHO, ALTO);
        escena.getStylesheets().add(Estilos.obtenerHoja());

        escenario.setTitle(TITULO);
        escenario.setScene(escena);
        escenario.setMinWidth(1100);
        escenario.setMinHeight(700);
        escenario.setOnCloseRequest(evento -> ventana.cerrar());
        escenario.show();
    }
}
