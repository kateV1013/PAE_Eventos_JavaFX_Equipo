package org.example.practicasemana3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;

import java.util.LinkedHashMap;
import java.util.Map;

public class HelloController {
    private final Map<String, Producto> inventario = new LinkedHashMap<>();

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblMensaje;

    @FXML
    private Label lblResultado;

    @FXML
    private TextArea txtInventario;

    @FXML
    private void guardarProducto(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String cantidadTexto = txtCantidad.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || precioTexto.isEmpty() || cantidadTexto.isEmpty()) {
            mostrarMensaje("Complete todos los campos para registrar el producto.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            int cantidad = Integer.parseInt(cantidadTexto);

            if (precio < 0 || cantidad < 0) {
                mostrarMensaje("Precio y cantidad no pueden ser negativos.");
                return;
            }

            Producto producto = new Producto(codigo, nombre, precio, cantidad);
            inventario.put(codigo, producto);
            lblResultado.setText("Producto guardado: " + producto.nombre());
            mostrarMensaje("Registro completado correctamente.");
            limpiarCampos();
            actualizarListado();
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese datos numericos validos para precio y cantidad.");
        }
    }

    @FXML
    private void buscarProducto(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) {
            return;
        }

        String codigo = txtBuscar.getText().trim();
        if (codigo.isEmpty()) {
            mostrarMensaje("Ingrese un codigo para buscar.");
            return;
        }

        Producto producto = inventario.get(codigo);
        if (producto == null) {
            lblResultado.setText("No existe un producto con el codigo " + codigo + ".");
            mostrarMensaje("Busqueda finalizada sin resultados.");
            return;
        }

        lblResultado.setText(String.format(
                "Codigo: %s | Nombre: %s | Precio: L %.2f | Cantidad: %d",
                producto.codigo(),
                producto.nombre(),
                producto.precio(),
                producto.cantidad()
        ));
        mostrarMensaje("Producto encontrado.");
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtCantidad.clear();
        txtCodigo.requestFocus();
    }

    private void actualizarListado() {
        if (inventario.isEmpty()) {
            txtInventario.setText("Sin productos registrados.");
            return;
        }

        StringBuilder listado = new StringBuilder();
        for (Producto producto : inventario.values()) {
            listado.append(String.format(
                    "%s - %s | L %.2f | Cantidad: %d%n",
                    producto.codigo(),
                    producto.nombre(),
                    producto.precio(),
                    producto.cantidad()
            ));
        }
        txtInventario.setText(listado.toString());
    }

    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    private record Producto(String codigo, String nombre, double precio, int cantidad) {
    }
}
