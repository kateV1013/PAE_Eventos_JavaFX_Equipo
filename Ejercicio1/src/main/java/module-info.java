module org.example.practicasemana3 {
    requires javafx.controls;
    requires javafx.fxml;

    exports org.example.practicasemana3.Ejercicio1;
    opens org.example.practicasemana3.Ejercicio1 to javafx.fxml;
}
