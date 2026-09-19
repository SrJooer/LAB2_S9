package org.example.vista;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.estructura.ListaNodos;
import org.example.estructura.Nodo;
import org.example.hilos.Repartidor;
import org.example.modelo.Estadisticas;
import org.example.modelo.Sistema;

public class PanelEstadisticas extends Tarjeta {

    private final Sistema sistema;
    private final Label generados = crearValor();
    private final Label entregados = crearValor();
    private final Label devueltos = crearValor();
    private final Label enProceso = crearValor();
    private final Label pendientes = crearValor();
    private final Label tiempoPromedio = crearValor();
    private final GridPane tablaRepartidores = crearTabla();

    public PanelEstadisticas(Sistema sistema) {
        super("Estadísticas");
        this.sistema = sistema;

        GridPane tabla = crearTabla();
        agregarFila(tabla, 0, "Paquetes generados:", generados);
        agregarFila(tabla, 1, "Entregados:", entregados);
        agregarFila(tabla, 2, "Devueltos:", devueltos);
        agregarFila(tabla, 3, "En proceso:", enProceso);
        agregarFila(tabla, 4, "Pendientes:", pendientes);
        agregarFila(tabla, 5, "Tiempo promedio:", tiempoPromedio);

        Label subtitulo = new Label("Entregas por repartidor");
        subtitulo.getStyleClass().add("subtitulo");

        getChildren().addAll(tabla, subtitulo, tablaRepartidores);
    }

    private GridPane crearTabla() {
        GridPane tabla = new GridPane();
        tabla.setHgap(24);
        tabla.setVgap(4);
        return tabla;
    }

    private Label crearValor() {
        Label valor = new Label("0");
        valor.getStyleClass().add("texto");
        return valor;
    }

    private void agregarFila(GridPane tabla, int fila, String nombre, Label valor) {
        Label etiqueta = new Label(nombre);
        etiqueta.getStyleClass().add("texto");
        tabla.add(etiqueta, 0, fila);
        tabla.add(valor, 1, fila);
    }

    public void actualizar() {
        Estadisticas datos = sistema.getEstadisticas();
        generados.setText(String.valueOf(datos.getGenerados()));
        entregados.setText(String.valueOf(datos.getEntregados()));
        devueltos.setText(String.valueOf(datos.getDevueltos()));
        enProceso.setText(String.valueOf(sistema.contarEnProceso()));
        pendientes.setText(String.valueOf(sistema.contarPendientes()));
        tiempoPromedio.setText(String.format("%.1f s", datos.calcularTiempoPromedio()));
        mostrarRepartidores(sistema.getRepartidores());
    }

    private void mostrarRepartidores(ListaNodos<Repartidor> repartidores) {
        tablaRepartidores.getChildren().clear();
        int fila = 0;
        Nodo<Repartidor> nodo = repartidores.getPrimero();
        while (nodo != null) {
            Repartidor repartidor = nodo.getValor();
            String nombre = repartidor.getNombre() + " (" + repartidor.getConductor() + "):";
            Label valor = crearValor();
            valor.setText(String.valueOf(repartidor.getPaquetesEntregados()));
            agregarFila(tablaRepartidores, fila, nombre, valor);
            fila++;
            nodo = nodo.getSiguiente();
        }
    }
}
