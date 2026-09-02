module org.example.practicasemana3 {
    requires javafx.controls;
    requires javafx.fxml;

    exports org.example.practicasemana3.Ejercicio3;
    opens org.example.practicasemana3.Ejercicio3 to javafx.fxml;
}
