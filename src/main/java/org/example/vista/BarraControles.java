package org.example.vista;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.example.excepciones.SimulacionException;
import org.example.modelo.Sistema;

public class BarraControles extends Tarjeta {

    private final Sistema sistema;
    private final Button iniciar = crearBoton("Iniciar");
    private final Button pausar = crearBoton("Pausar");
    private final Button reanudar = crearBoton("Reanudar");
    private final Button detener = crearBoton("Detener");
    private final Button reiniciar = crearBoton("Reiniciar");
    private final Button estadisticas = crearBoton("Estadísticas");

    public BarraControles(Sistema sistema, VentanaEstadisticas ventanaEstadisticas) {
        super("Sistema de paquetería");
        this.sistema = sistema;
        titulo.getStyleClass().add("titulo-app");

        iniciar.getStyleClass().add("boton-principal");
        iniciar.setOnAction(evento -> ejecutar(() -> sistema.iniciar()));
        pausar.setOnAction(evento -> ejecutar(() -> sistema.pausar()));
        reanudar.setOnAction(evento -> ejecutar(() -> sistema.reanudar()));
        detener.setOnAction(evento -> ejecutar(() -> sistema.detener()));
        reiniciar.setOnAction(evento -> ejecutar(() -> sistema.reiniciar()));
        estadisticas.setOnAction(evento -> ventanaEstadisticas.mostrar());

        HBox botones = new HBox(8, iniciar, pausar, reanudar, detener, reiniciar, estadisticas);
        botones.setAlignment(Pos.CENTER);
        getChildren().add(botones);
        actualizar();
    }

    private Button crearBoton(String texto) {
        Button boton = new Button(texto);
        boton.getStyleClass().add("boton");
        return boton;
    }

    private void ejecutar(AccionSimulacion accion) {
        try {
            accion.ejecutar();
        } catch (SimulacionException e) {
            sistema.getRegistro().escribir("Aviso: " + e.getMessage());
        }
        actualizar();
    }

    public void actualizar() {
        boolean enMarcha = sistema.estaEnMarcha();
        boolean enPausa = sistema.estaEnPausa();
        iniciar.setDisable(!sistema.puedeIniciar());
        pausar.setDisable(!enMarcha || enPausa);
        reanudar.setDisable(!enPausa);
        detener.setDisable(!enMarcha);
    }
}
