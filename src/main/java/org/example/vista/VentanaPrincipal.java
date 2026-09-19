package org.example.vista;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.excepciones.SimulacionException;
import org.example.modelo.Sistema;

public class VentanaPrincipal extends BorderPane {

    private static final double INTERVALO_REFRESCO = 250;
    private static final double ALTO_REGISTRO = 170;

    private final Sistema sistema;
    private final BarraControles barra;
    private final PanelZona panelRecepcion;
    private final PanelZona panelAlmacen;
    private final PanelZona panelClasificacion;
    private final PanelZona panelEmpaquetado;
    private final PanelExpedicion panelExpedicion;
    private final PanelRepartidores panelRepartidores;
    private final PanelRegistro panelRegistro;
    private final VentanaEstadisticas ventanaEstadisticas;
    private final Timeline refresco;

    public VentanaPrincipal(Sistema sistema) {
        this.sistema = sistema;
        getStyleClass().add("ventana");

        ventanaEstadisticas = new VentanaEstadisticas(sistema);
        barra = new BarraControles(sistema, ventanaEstadisticas);
        panelRecepcion = new PanelZona(sistema.getRecepcion(), true);
        panelAlmacen = new PanelZona(sistema.getAlmacen(), false);
        panelClasificacion = new PanelZona(sistema.getClasificacion(), true);
        panelEmpaquetado = new PanelZona(sistema.getEmpaquetado(), true);
        panelExpedicion = new PanelExpedicion(sistema.getExpedicion());
        panelRepartidores = new PanelRepartidores();
        panelRegistro = new PanelRegistro();
        sistema.getRegistro().setEscucha(panelRegistro);

        ScrollPane desplazable = new ScrollPane(crearContenido());
        desplazable.setFitToWidth(true);
        desplazable.setFitToHeight(true);
        desplazable.getStyleClass().add("desplazable");
        setCenter(desplazable);

        refresco = new Timeline(new KeyFrame(Duration.millis(INTERVALO_REFRESCO), evento -> refrescar()));
        refresco.setCycleCount(Animation.INDEFINITE);
        refresco.play();
    }

    private VBox crearContenido() {
        HBox filaZonas = new HBox(10, panelRecepcion, panelAlmacen, panelClasificacion, panelEmpaquetado);
        repartirAncho(panelRecepcion);
        repartirAncho(panelAlmacen);
        repartirAncho(panelClasificacion);
        repartirAncho(panelEmpaquetado);

        panelRegistro.setMinHeight(ALTO_REGISTRO);
        VBox.setVgrow(panelRegistro, Priority.ALWAYS);

        VBox contenido = new VBox(10, barra, filaZonas, panelExpedicion, panelRepartidores, panelRegistro);
        contenido.setPadding(new Insets(12));
        return contenido;
    }

    private void repartirAncho(VBox panel) {
        HBox.setHgrow(panel, Priority.ALWAYS);
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.setPrefWidth(100);
    }

    private void refrescar() {
        panelRecepcion.actualizar();
        panelRecepcion.mostrarTrabajadores(sistema.getAlmaceneros());
        panelAlmacen.actualizar();
        panelClasificacion.actualizar();
        panelClasificacion.mostrarTrabajadores(sistema.getClasificadores());
        panelEmpaquetado.actualizar();
        panelEmpaquetado.mostrarTrabajadores(sistema.getEmpaquetadores());
        panelExpedicion.actualizar();
        panelRepartidores.mostrar(sistema.getRepartidores());
        ventanaEstadisticas.actualizar();
        barra.actualizar();
    }

    public void cerrar() {
        refresco.stop();
        ventanaEstadisticas.close();
        if (sistema.estaEnMarcha()) {
            try {
                sistema.detener();
            } catch (SimulacionException e) {
                sistema.getRegistro().escribir("Aviso: " + e.getMessage());
            }
        }
    }
}
