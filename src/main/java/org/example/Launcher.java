package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    private static final String TITLE = "Sistema de Paquetes";
    private static final double WIDTH = 1280;
    private static final double HEIGHT = 760;

    @Override
    public void start(Stage stage) {
        stage.setTitle(TITLE);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);



        stage.show();
    }
}
