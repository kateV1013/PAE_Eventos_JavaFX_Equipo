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
import java.util.Optional;

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

    private final ObservableList<Lote> lotes = FXCollections.observableArrayList();
    private Lote loteEnEdicion;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colProductor.setCellValueFactory(new PropertyValueFactory<>("productor"));
        colVariedad.setCellValueFactory(new PropertyValueFactory<>("variedad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaLotes.setItems(lotes);
        tablaLotes.setContextMenu(crearMenuContextual());
        dpFechaEntrega.setValue(LocalDate.now());
        cbEstado.setValue("Recibido");

        actualizarResumen();
    }

    @FXML
    private void registrarLote() {
        if (!formularioValido()) {
            return;
        }

        if (loteEnEdicion == null) {
            lotes.add(crearLoteDesdeFormulario());
        } else {
            actualizarLote(loteEnEdicion);
            tablaLotes.refresh();
            loteEnEdicion = null;
            btnRegistrar.setText("Registrar lote");
        }

        limpiarFormulario();
        actualizarResumen();
        limpiarDetalles();
    }

    @FXML
    private void mostrarDetalle(MouseEvent event) {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();
        if (lote == null) {
            limpiarDetalles();
            return;
        }

        lblDetalleCodigo.setText(lote.getCodigo());
        lblDetalleProductor.setText(lote.getProductor());
        lblDetalleVariedad.setText(lote.getVariedad());
        lblDetallePeso.setText(lote.getPeso() + " qq");
    }

    @FXML
    private void editarLote() {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();
        if (lote == null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Seleccione un lote", "Seleccione un lote de la tabla para editarlo.");
            return;
        }

        loteEnEdicion = lote;
        txtCodigo.setText(lote.getCodigo());
        txtProductor.setText(lote.getProductor());
        cbVariedad.setValue(lote.getVariedad());
        txtPeso.setText(String.valueOf(lote.getPeso()));
        cbEstado.setValue(lote.getEstado());
        txtObservaciones.setText(lote.getObservaciones());
        dpFechaEntrega.setValue(LocalDate.parse(lote.getFechaEntrega()));
        btnRegistrar.setText("Guardar cambios");
    }

    @FXML
    private void eliminarLote() {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();
        if (lote == null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Seleccione un lote", "Seleccione un lote de la tabla para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setHeaderText("Eliminar lote " + lote.getCodigo());
        confirmacion.setContentText("Esta accion eliminara el lote registrado para " + lote.getProductor() + ".");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            lotes.remove(lote);
            actualizarResumen();
            limpiarDetalles();
            if (lote == loteEnEdicion) {
                loteEnEdicion = null;
                btnRegistrar.setText("Registrar lote");
                limpiarFormulario();
            }
        }
    }

    private ContextMenu crearMenuContextual() {
        MenuItem editar = new MenuItem("Editar");
        editar.setOnAction(event -> editarLote());

        MenuItem eliminar = new MenuItem("Eliminar");
        eliminar.setOnAction(event -> eliminarLote());

        return new ContextMenu(editar, eliminar);
    }

    private Lote crearLoteDesdeFormulario() {
        return new Lote(
                txtCodigo.getText(),
                txtProductor.getText(),
                cbVariedad.getValue(),
                Double.parseDouble(txtPeso.getText()),
                dpFechaEntrega.getValue().toString(),
                cbEstado.getValue(),
                txtObservaciones.getText()
        );
    }

    private void actualizarLote(Lote lote) {
        lote.setCodigo(txtCodigo.getText());
        lote.setProductor(txtProductor.getText());
        lote.setVariedad(cbVariedad.getValue());
        lote.setPeso(Double.parseDouble(txtPeso.getText()));
        lote.setFechaEntrega(dpFechaEntrega.getValue().toString());
        lote.setEstado(cbEstado.getValue());
        lote.setObservaciones(txtObservaciones.getText());
    }

    private boolean formularioValido() {
        if (txtCodigo.getText().isBlank()
                || txtProductor.getText().isBlank()
                || cbVariedad.getValue() == null
                || txtPeso.getText().isBlank()
                || dpFechaEntrega.getValue() == null
                || cbEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Complete codigo, productor, variedad, peso, fecha y estado.");
            return false;
        }

        try {
            double peso = Double.parseDouble(txtPeso.getText());
            if (peso <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Peso invalido", "El peso debe ser mayor que cero.");
                return false;
            }
        } catch (NumberFormatException exception) {
            mostrarAlerta(Alert.AlertType.WARNING, "Peso invalido", "Ingrese el peso usando numeros.");
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
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

    private void actualizarResumen() {
        lblLotesHoy.setText(String.valueOf(lotes.size()));

        double total = 0;
        for (Lote lote : lotes) {
            total += lote.getPeso();
        }
        lblQuintales.setText(String.format("%.2f", total));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
