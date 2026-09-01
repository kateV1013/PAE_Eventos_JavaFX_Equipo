module org.example.practicasemana3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.practicasemana3 to javafx.fxml;
    exports org.example.practicasemana3;
}