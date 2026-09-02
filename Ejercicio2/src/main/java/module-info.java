module ni.edu.uam.ejercicio2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.ejercicio2 to javafx.fxml;
    exports ni.edu.uam.ejercicio2;
}