package org.example.practicasemana3.Ejercicio3;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Optional;

public class HelloController {
    private final ObservableList<Producto> productos = FXCollections.observableArrayList(
            new Producto("A001", "Hamaca Masaya", "Textiles", 850.00, 6, Color.web("#2f7d6d")),
            new Producto("A002", "Jicara pintada", "Decoracion", 185.50, 14, Color.web("#c85f2f")),
            new Producto("A003", "Bolso tejido", "Accesorios", 420.00, 9, Color.web("#365f91")),
            new Producto("A004", "Ceramica negra", "Ceramica", 310.75, 4, Color.web("#5a4a42"))
    );

    @FXML
    private TableView<Producto> tblProductos;

    @FXML
    private TableColumn<Producto, ImageView> colImagen;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, Number> colPrecio;

    @FXML
    private TableColumn<Producto, Number> colCantidad;

    @FXML
    private Label lblDetalle;

    @FXML
    private Label lblEstado;

    @FXML
    private void initialize() {
        colImagen.setCellValueFactory(data -> data.getValue().imagenProperty());
        colCodigo.setCellValueFactory(data -> data.getValue().codigoProperty());
        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
        colCategoria.setCellValueFactory(data -> data.getValue().categoriaProperty());
        colPrecio.setCellValueFactory(data -> data.getValue().precioProperty());
        colCantidad.setCellValueFactory(data -> data.getValue().cantidadProperty());

        tblProductos.setItems(productos);
        tblProductos.getSelectionModel().selectFirst();
        tblProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> mostrarDetalle(seleccionado)
        );
        mostrarDetalle(tblProductos.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void nuevoProducto(ActionEvent event) {
        Producto producto = new Producto(
                String.format("A%03d", productos.size() + 1),
                "Nueva artesania",
                "Catalogo",
                0.00,
                0,
                Color.web("#7a5cbd")
        );
        productos.add(producto);
        tblProductos.getSelectionModel().select(producto);
        tblProductos.scrollTo(producto);
        mostrarDetalle(producto);
        lblEstado.setText("Nuevo producto agregado al catalogo.");
    }

    @FXML
    private void guardarCatalogo(ActionEvent event) {
        lblEstado.setText("Catalogo guardado localmente con " + productos.size() + " productos.");
    }

    @FXML
    private void eliminarProducto(ActionEvent event) {
        Producto producto = tblProductos.getSelectionModel().getSelectedItem();
        if (producto == null) {
            lblEstado.setText("Seleccione un producto para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar producto");
        confirmacion.setHeaderText("Eliminar " + producto.nombre());
        confirmacion.setContentText("Esta accion quitara el producto del catalogo.");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            productos.remove(producto);
            Producto seleccionado = tblProductos.getSelectionModel().getSelectedItem();
            mostrarDetalle(seleccionado);
            lblEstado.setText("Producto eliminado del catalogo.");
        }
    }

    @FXML
    private void editarCantidad(ActionEvent event) {
        Producto producto = tblProductos.getSelectionModel().getSelectedItem();
        if (producto == null) {
            lblEstado.setText("Seleccione un producto para editar su cantidad.");
            return;
        }

        TextInputDialog dialogo = new TextInputDialog(String.valueOf(producto.cantidad()));
        dialogo.setTitle("Editar cantidad");
        dialogo.setHeaderText("Editar existencias de " + producto.nombre());
        dialogo.setContentText("Nueva cantidad:");

        Optional<String> respuesta = dialogo.showAndWait();
        respuesta.ifPresent(texto -> {
            try {
                int nuevaCantidad = Integer.parseInt(texto.trim());
                if (nuevaCantidad < 0) {
                    lblEstado.setText("La cantidad no puede ser negativa.");
                    return;
                }

                producto.setCantidad(nuevaCantidad);
                tblProductos.refresh();
                mostrarDetalle(producto);
                lblEstado.setText("Cantidad actualizada para " + producto.nombre() + ".");
            } catch (NumberFormatException ex) {
                lblEstado.setText("Ingrese una cantidad valida en numeros enteros.");
            }
        });
    }

    @FXML
    private void buscarProducto(ActionEvent event) {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Buscar producto");
        dialogo.setHeaderText("Buscar en el catalogo");
        dialogo.setContentText("Codigo o nombre:");

        Optional<String> respuesta = dialogo.showAndWait();
        respuesta.ifPresent(texto -> {
            String criterio = texto.trim().toLowerCase();
            for (Producto producto : productos) {
                if (producto.codigo().toLowerCase().contains(criterio)
                        || producto.nombre().toLowerCase().contains(criterio)) {
                    tblProductos.getSelectionModel().select(producto);
                    tblProductos.scrollTo(producto);
                    mostrarDetalle(producto);
                    lblEstado.setText("Producto encontrado.");
                    return;
                }
            }
            lblEstado.setText("No se encontraron productos con ese criterio.");
        });
    }

    @FXML
    private void mostrarCatalogo(ActionEvent event) {
        lblEstado.setText("Vista de catalogo activa.");
    }

    @FXML
    private void registrarVenta(ActionEvent event) {
        Producto producto = tblProductos.getSelectionModel().getSelectedItem();
        if (producto == null) {
            lblEstado.setText("Seleccione un producto para registrar una venta.");
            return;
        }
        if (producto.cantidad() == 0) {
            lblEstado.setText("No hay existencias disponibles para vender.");
            return;
        }

        producto.restarUnidad();
        tblProductos.refresh();
        mostrarDetalle(producto);
        lblEstado.setText("Venta registrada para " + producto.nombre() + ".");
    }

    @FXML
    private void mostrarAyuda(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ayuda");
        alerta.setHeaderText("Tienda de artesanias");
        alerta.setContentText("Use el menu o la barra de herramientas para crear, guardar, buscar y registrar ventas.");
        alerta.showAndWait();
    }

    @FXML
    private void mostrarDetalleConMouse() {
        Producto producto = tblProductos.getSelectionModel().getSelectedItem();
        mostrarDetalle(producto);
        if (producto != null) {
            lblEstado.setText("Detalle mostrado para " + producto.nombre() + ".");
        }
    }

    private void mostrarDetalle(Producto producto) {
        if (producto == null) {
            lblDetalle.setText("Seleccione un producto para ver sus detalles.");
            return;
        }

        lblDetalle.setText(String.format(
                "%s - %s | Categoria: %s | Precio: C$ %.2f | Existencias: %d",
                producto.codigo(),
                producto.nombre(),
                producto.categoria(),
                producto.precio(),
                producto.cantidad()
        ));
    }

    public static class Producto {
        private final SimpleStringProperty codigo;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty categoria;
        private final SimpleDoubleProperty precio;
        private final SimpleIntegerProperty cantidad;
        private final SimpleObjectProperty<ImageView> imagen;

        public Producto(String codigo, String nombre, String categoria, double precio, int cantidad, Color color) {
            this.codigo = new SimpleStringProperty(codigo);
            this.nombre = new SimpleStringProperty(nombre);
            this.categoria = new SimpleStringProperty(categoria);
            this.precio = new SimpleDoubleProperty(precio);
            this.cantidad = new SimpleIntegerProperty(cantidad);
            ImageView vista = new ImageView(crearImagen(color));
            vista.setFitWidth(56);
            vista.setFitHeight(40);
            vista.setPreserveRatio(false);
            this.imagen = new SimpleObjectProperty<>(vista);
        }

        private static Image crearImagen(Color base) {
            WritableImage imagen = new WritableImage(56, 40);
            for (int y = 0; y < 40; y++) {
                for (int x = 0; x < 56; x++) {
                    boolean borde = x < 3 || y < 3 || x > 52 || y > 36;
                    boolean figura = x > 16 && x < 40 && y > 9 && y < 31;
                    Color color = borde ? Color.web("#f2d29b") : Color.web("#f8efe1");
                    if (figura) {
                        color = base;
                    }
                    if ((x + y) % 13 == 0) {
                        color = color.brighter();
                    }
                    imagen.getPixelWriter().setColor(x, y, color);
                }
            }
            return imagen;
        }

        public SimpleStringProperty codigoProperty() {
            return codigo;
        }

        public SimpleStringProperty nombreProperty() {
            return nombre;
        }

        public SimpleStringProperty categoriaProperty() {
            return categoria;
        }

        public SimpleDoubleProperty precioProperty() {
            return precio;
        }

        public SimpleIntegerProperty cantidadProperty() {
            return cantidad;
        }

        public SimpleObjectProperty<ImageView> imagenProperty() {
            return imagen;
        }

        public String codigo() {
            return codigo.get();
        }

        public String nombre() {
            return nombre.get();
        }

        public String categoria() {
            return categoria.get();
        }

        public double precio() {
            return precio.get();
        }

        public int cantidad() {
            return cantidad.get();
        }

        public void restarUnidad() {
            cantidad.set(cantidad.get() - 1);
        }

        public void setCantidad(int cantidad) {
            this.cantidad.set(cantidad);
        }
    }
}
