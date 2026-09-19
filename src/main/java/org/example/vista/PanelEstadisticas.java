package org.example.vista;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
    private final GridPane tablaRepartidores = new GridPane();

    public PanelEstadisticas(Sistema sistema) {
        super("Estadísticas");
        this.sistema = sistema;

        GridPane tabla = new GridPane();
        tabla.setHgap(32);
        tabla.setVgap(14);
        tabla.setAlignment(Pos.CENTER);
        agregarDato(tabla, 0, 0, "Generados", generados);
        agregarDato(tabla, 1, 0, "Entregados", entregados);
        agregarDato(tabla, 2, 0, "Devueltos", devueltos);
        agregarDato(tabla, 0, 1, "En proceso", enProceso);
        agregarDato(tabla, 1, 1, "Pendientes", pendientes);
        agregarDato(tabla, 2, 1, "Tiempo promedio", tiempoPromedio);

        Label subtitulo = crearTextoCentrado("subtitulo");
        subtitulo.setText("ENTREGAS POR REPARTIDOR");
        tablaRepartidores.setHgap(16);
        tablaRepartidores.setVgap(4);
        tablaRepartidores.setAlignment(Pos.CENTER);

        getChildren().addAll(tabla, subtitulo, tablaRepartidores);
    }

    private Label crearValor() {
        Label valor = new Label("0");
        valor.getStyleClass().add("valor-estadistica");
        return valor;
    }

    private void agregarDato(GridPane tabla, int columna, int fila, String nombre, Label valor) {
        Label etiqueta = new Label(nombre);
        etiqueta.getStyleClass().add("texto-suave");
        VBox celda = new VBox(2, valor, etiqueta);
        celda.setAlignment(Pos.CENTER);
        tabla.add(celda, columna, fila);
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
            Label nombre = new Label(repartidor.getNombre() + " · " + repartidor.getConductor());
            nombre.getStyleClass().add("texto-suave");
            Label cantidad = new Label(String.valueOf(repartidor.getPaquetesEntregados()));
            cantidad.getStyleClass().add("valor-pequeno");
            tablaRepartidores.add(nombre, 0, fila);
            tablaRepartidores.add(cantidad, 1, fila);
            fila++;
            nodo = nodo.getSiguiente();
        }
    }
}
