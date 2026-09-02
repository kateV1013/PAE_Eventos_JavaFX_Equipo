package ni.edu.uam.menuprincipal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;

public class MenuController {

    @FXML
    private void abrirEjercicio1() {
        abrirPrograma("Ejercicio1");
    }

    @FXML
    private void abrirEjercicio2() {
        abrirPrograma("Ejercicio2");
    }

    @FXML
    private void abrirEjercicio3() {
        abrirPrograma("Ejercicio3");
    }

    private void abrirPrograma(String carpeta) {
        try {
            File carpetaActual = new File("").getAbsoluteFile();
            File raiz;

            if (carpetaActual.getName().equals("MenuPrincipal")) {
                raiz = carpetaActual.getParentFile();
            } else {
                raiz = carpetaActual;
            }

            File proyecto = new File(raiz, carpeta);

            ProcessBuilder proceso = new ProcessBuilder("cmd", "/c", "mvnw.cmd", "javafx:run");
            proceso.directory(proyecto);
            proceso.start();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir " + carpeta);
            alerta.showAndWait();
        }
    }
}
