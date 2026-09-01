package org.example.practicasemana3.Ejercicio1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/practicasemana3/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 520, 520);
        stage.setTitle("Inventario de pulperia");
        stage.setScene(scene);
        stage.show();
    }
    //
}
