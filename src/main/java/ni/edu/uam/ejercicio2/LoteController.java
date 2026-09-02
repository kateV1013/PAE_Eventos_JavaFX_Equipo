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
    private Lote loteSeleccionado;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colProductor.setCellValueFactory(new PropertyValueFactory<>("productor"));
        colVariedad.setCellValueFactory(new PropertyValueFactory<>("variedad"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaLotes.setItems(listaLotes);

        MenuItem opcionEditar = new MenuItem("Editar");
        MenuItem opcionEliminar = new MenuItem("Eliminar");

        opcionEditar.setOnAction(event -> editarLote());
        opcionEliminar.setOnAction(event -> eliminarLote());

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(opcionEditar);
        menu.getItems().add(opcionEliminar);
        tablaLotes.setContextMenu(menu);

        dpFechaEntrega.setValue(LocalDate.now());
        cbEstado.setValue("Recibido");
        lblLotesHoy.setText("0");
        lblQuintales.setText("0.00");
    }

    @FXML
    private void registrarLote() {
        String codigo = txtCodigo.getText();
        String productor = txtProductor.getText();
        String variedad = cbVariedad.getValue();
        String pesoTexto = txtPeso.getText();

        if (codigo.isEmpty() || productor.isEmpty() || variedad == null || pesoTexto.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Datos incompletos");
            alerta.setContentText("Complete codigo, productor, variedad y peso.");
            alerta.showAndWait();
            return;
        }

        double peso;

        try {
            peso = Double.parseDouble(pesoTexto);
        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("El peso debe ser un numero.");
            alerta.showAndWait();
            return;
        }

        String fecha = dpFechaEntrega.getValue().toString();
        String estado = cbEstado.getValue();
        String observaciones = txtObservaciones.getText();

        if (loteSeleccionado == null) {
            Lote lote = new Lote(codigo, productor, variedad, peso, fecha, estado, observaciones);
            listaLotes.add(lote);
        } else {
            loteSeleccionado.setCodigo(codigo);
            loteSeleccionado.setProductor(productor);
            loteSeleccionado.setVariedad(variedad);
            loteSeleccionado.setPeso(peso);
            loteSeleccionado.setFechaEntrega(fecha);
            loteSeleccionado.setEstado(estado);
            loteSeleccionado.setObservaciones(observaciones);
            tablaLotes.refresh();
            loteSeleccionado = null;
            btnRegistrar.setText("Registrar lote");
        }

        txtCodigo.clear();
        txtProductor.clear();
        cbVariedad.setValue(null);
        txtPeso.clear();
        dpFechaEntrega.setValue(LocalDate.now());
        cbEstado.setValue("Recibido");
        txtObservaciones.clear();

        actualizarTotales();
    }

    @FXML
    private void mostrarDetalle(MouseEvent event) {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();

        if (lote != null) {
            lblDetalleCodigo.setText(lote.getCodigo());
            lblDetalleProductor.setText(lote.getProductor());
            lblDetalleVariedad.setText(lote.getVariedad());
            lblDetallePeso.setText(lote.getPeso() + " qq");
        }
    }

    @FXML
    private void editarLote() {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();

        if (lote == null) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Editar");
            alerta.setContentText("Seleccione un lote para editar.");
            alerta.showAndWait();
            return;
        }

        loteSeleccionado = lote;

        txtCodigo.setText(lote.getCodigo());
        txtProductor.setText(lote.getProductor());
        cbVariedad.setValue(lote.getVariedad());
        txtPeso.setText(String.valueOf(lote.getPeso()));
        dpFechaEntrega.setValue(LocalDate.parse(lote.getFechaEntrega()));
        cbEstado.setValue(lote.getEstado());
        txtObservaciones.setText(lote.getObservaciones());

        btnRegistrar.setText("Guardar cambios");
    }

    @FXML
    private void eliminarLote() {
        Lote lote = tablaLotes.getSelectionModel().getSelectedItem();

        if (lote == null) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Eliminar");
            alerta.setContentText("Seleccione un lote para eliminar.");
            alerta.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setContentText("Desea eliminar el lote " + lote.getCodigo() + "?");

        ButtonType respuesta = confirmacion.showAndWait().orElse(ButtonType.CANCEL);

        if (respuesta == ButtonType.OK) {
            listaLotes.remove(lote);
            actualizarTotales();

            lblDetalleCodigo.setText("--");
            lblDetalleProductor.setText("--");
            lblDetalleVariedad.setText("--");
            lblDetallePeso.setText("--");
        }
    }

    private void actualizarTotales() {
        lblLotesHoy.setText(String.valueOf(listaLotes.size()));

        double total = 0;

        for (Lote lote : listaLotes) {
            total = total + lote.getPeso();
        }

        lblQuintales.setText(String.format("%.2f", total));
    }
}
