package ni.edu.uam.ejercicio2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;

public class LoteController {

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtProductor;
    @FXML
    private ComboBox<String> cbVariedad;
    @FXML
    private TextField txtPeso;
    @FXML
    private DatePicker dpFechaEntrega;
    @FXML
    private ComboBox<String> cbEstado;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Button btnRegistrar;

    @FXML
    private Label lblLotesHoy;
    @FXML
    private Label lblQuintales;

    @FXML
    private TableView<Lote> tablaLotes;
    @FXML
    private TableColumn<Lote, String> colCodigo;
    @FXML
    private TableColumn<Lote, String> colProductor;
    @FXML
    private TableColumn<Lote, String> colVariedad;
    @FXML
    private TableColumn<Lote, Double> colPeso;
    @FXML
    private TableColumn<Lote, String> colEstado;

    @FXML
    private Label lblDetalleCodigo;
    @FXML
    private Label lblDetalleProductor;
    @FXML
    private Label lblDetalleVariedad;
    @FXML
    private Label lblDetallePeso;

    private ObservableList<Lote> listaLotes = FXCollections.observableArrayList();
    private Lote loteEditando = null;

    @FXML
    public void initialize() {
        configurarTabla();
        configurarMenu();
        limpiarCampos();
        actualizarTotales();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colProductor.setCellValueFactory(new PropertyValueFactory<>("productor"));
        colVariedad.setCellValueFactory(new PropertyValueFactory<>("variedad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaLotes.setItems(listaLotes);
    }

    private void configurarMenu() {
        MenuItem editar = new MenuItem("Editar");
        MenuItem eliminar = new MenuItem("Eliminar");

        editar.setOnAction(event -> editarLote());
        eliminar.setOnAction(event -> eliminarLote());

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(editar);
        menu.getItems().add(eliminar);

        tablaLotes.setContextMenu(menu);
    }

    @FXML
    private void registrarLote() {
        if (!validarCampos()) {
            return;
        }

        if (loteEditando == null) {
            agregarLote();
        } else {
            guardarCambios();
        }

        limpiarCampos();
        limpiarDetalles();
        actualizarTotales();
    }

    private void agregarLote() {
        Lote lote = crearLote();
        listaLotes.add(lote);
    }

    private void guardarCambios() {
        loteEditando.setCodigo(txtCodigo.getText());
        loteEditando.setProductor(txtProductor.getText());
        loteEditando.setVariedad(cbVariedad.getValue());
        loteEditando.setPeso(Double.parseDouble(txtPeso.getText()));
        loteEditando.setFechaEntrega(dpFechaEntrega.getValue().toString());
        loteEditando.setEstado(cbEstado.getValue());
        loteEditando.setObservaciones(txtObservaciones.getText());

        tablaLotes.refresh();
        loteEditando = null;
        btnRegistrar.setText("Registrar lote");
    }

    private Lote crearLote() {
        String codigo = txtCodigo.getText();
        String productor = txtProductor.getText();
        String variedad = cbVariedad.getValue();
        double peso = Double.parseDouble(txtPeso.getText());
        String fecha = dpFechaEntrega.getValue().toString();
        String estado = cbEstado.getValue();
        String observaciones = txtObservaciones.getText();

        return new Lote(codigo, productor, variedad, peso, fecha, estado, observaciones);
    }

    private boolean validarCampos() {
        if (txtCodigo.getText().isEmpty()) {
            mostrarAlerta("Falta codigo", "Escriba el codigo del lote.");
            return false;
        }

        if (txtProductor.getText().isEmpty()) {
            mostrarAlerta("Falta productor", "Escriba el nombre del productor.");
            return false;
        }

        if (cbVariedad.getValue() == null) {
            mostrarAlerta("Falta variedad", "Seleccione una variedad de cafe.");
            return false;
        }

        if (txtPeso.getText().isEmpty()) {
            mostrarAlerta("Falta peso", "Escriba el peso del lote.");
            return false;
        }

        try {
            Double.parseDouble(txtPeso.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Peso incorrecto", "El peso debe ser un numero.");
            return false;
        }

        if (dpFechaEntrega.getValue() == null) {
            mostrarAlerta("Falta fecha", "Seleccione la fecha de entrega.");
            return false;
        }

        if (cbEstado.getValue() == null) {
            mostrarAlerta("Falta estado", "Seleccione el estado del lote.");
            return false;
        }

        return true;
    }

    @FXML
    private void mostrarDetalle(MouseEvent event) {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();

        if (lote != null) {
            mostrarDatos(lote);
        }
    }

    private void mostrarDatos(Lote lote) {
        lblDetalleCodigo.setText(lote.getCodigo());
        lblDetalleProductor.setText(lote.getProductor());
        lblDetalleVariedad.setText(lote.getVariedad());
        lblDetallePeso.setText(lote.getPeso() + " qq");
    }

    @FXML
    private void editarLote() {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();

        if (lote == null) {
            mostrarAlerta("Editar", "Seleccione un lote para editar.");
            return;
        }

        loteEditando = lote;
        cargarLoteEnFormulario(lote);
        btnRegistrar.setText("Guardar cambios");
    }

    private void cargarLoteEnFormulario(Lote lote) {
        txtCodigo.setText(lote.getCodigo());
        txtProductor.setText(lote.getProductor());
        cbVariedad.setValue(lote.getVariedad());
        txtPeso.setText(String.valueOf(lote.getPeso()));
        dpFechaEntrega.setValue(LocalDate.parse(lote.getFechaEntrega()));
        cbEstado.setValue(lote.getEstado());
        txtObservaciones.setText(lote.getObservaciones());
    }

    @FXML
    private void eliminarLote() {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();

        if (lote == null) {
            mostrarAlerta("Eliminar", "Seleccione un lote para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar eliminacion");
        alerta.setContentText("Desea eliminar el lote " + lote.getCodigo() + "?");

        ButtonType respuesta = alerta.showAndWait().orElse(ButtonType.CANCEL);

        if (respuesta == ButtonType.OK) {
            listaLotes.remove(lote);
            limpiarDetalles();
            actualizarTotales();
        }
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtProductor.clear();
        cbVariedad.setValue(null);
        txtPeso.clear();
        dpFechaEntrega.setValue(LocalDate.now());
        cbEstado.setValue("Recibido");
        txtObservaciones.clear();
    }

    private void limpiarDetalles() {
        lblDetalleCodigo.setText("--");
        lblDetalleProductor.setText("--");
        lblDetalleVariedad.setText("--");
        lblDetallePeso.setText("--");
    }

    private void actualizarTotales() {
        double total = 0;

        for (Lote lote : listaLotes) {
            total = total + lote.getPeso();
        }

        lblLotesHoy.setText(String.valueOf(listaLotes.size()));
        lblQuintales.setText(String.format("%.2f", total));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
