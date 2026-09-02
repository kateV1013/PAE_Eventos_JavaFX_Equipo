module ni.edu.uam.menuprincipal {
    requires javafx.controls;
    requires javafx.fxml;

    opens ni.edu.uam.menuprincipal to javafx.fxml;
    exports ni.edu.uam.menuprincipal;
}
