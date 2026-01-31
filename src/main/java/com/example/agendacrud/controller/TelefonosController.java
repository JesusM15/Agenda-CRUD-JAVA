package com.example.agendacrud.controller;

import com.example.agendacrud.models.Persona;
import com.example.agendacrud.models.Telefono; // Asumiendo que tienes este modelo
import com.example.agendacrud.models.TelefonoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TelefonosController {

    @FXML private Label lblNombrePersona;
    @FXML private TextField txtTelefono;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    @FXML private TableView<Telefono> tablaTelefonos;
    @FXML private TableColumn<Telefono, Integer> colId;
    @FXML private TableColumn<Telefono, String> colNumero;
    @FXML private TableColumn<Telefono, Void> colAcciones;

    private Persona persona;
    private Telefono telefonoEnEdicion = null;
    private ObservableList<Telefono> listaTelefonos = FXCollections.observableArrayList();

    public void initData(Persona p) {
        this.persona = p;
        lblNombrePersona.setText(persona.getNombre());

        configurarTabla();
        cargarTelefonos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idTelefono"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("✏️");
            private final Button btnDelete = new Button("🗑");
            private final HBox container = new HBox(8, btnEdit, btnDelete); // 8px de separación

            {
                String baseStyle = "-fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5; -fx-padding: 5 8 5 8;";
                btnEdit.setStyle("-fx-background-color: #f39c12; " + baseStyle);
                btnDelete.setStyle("-fx-background-color: #e74c3c; " + baseStyle);

                container.setAlignment(Pos.CENTER);
                container.setPadding(new javafx.geometry.Insets(2));

                btnEdit.setOnAction(event -> {
                    Telefono t = getTableView().getItems().get(getIndex());
                    prepararEdicion(t);
                });

                btnDelete.setOnAction(event -> {
                    Telefono t = getTableView().getItems().get(getIndex());
                    onEliminarTelefono(t);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        txtTelefono.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*") || newValue.length() > 10){
                txtTelefono.setText(oldValue);
            }
        });

        tablaTelefonos.setItems(listaTelefonos);
    }

    private void prepararEdicion(Telefono t) {
        telefonoEnEdicion = t;
        txtTelefono.setText(t.getNumero());
        btnGuardar.setText("Actualizar");
        btnGuardar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");

        btnCancelar.setVisible(true);
        btnCancelar.setManaged(true);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    protected void onGuardarTelefonoClick() {
        String numero = txtTelefono.getText();

        if (!Telefono.esValido(numero)) {
            mostrarAlerta("Dato Inválido", "Asegúrate de ingresar solo números (máx. 10).");
            return;
        }
        if (numero.isEmpty()) return;

        TelefonoDAO dao = new TelefonoDAO();
        try {
            if (telefonoEnEdicion == null) {
                Telefono nuevo = new Telefono(-1, numero, persona.getId());
                dao.registrarTelefono(nuevo);
                listaTelefonos.add(nuevo);
            } else {
                String numeroAnterior = telefonoEnEdicion.getNumero();
                Telefono tmp = new Telefono(telefonoEnEdicion.getIdTelefono(), numero, persona.getId());
                dao.modificarTelefono(tmp);

                telefonoEnEdicion.setNumero(numero);

                tablaTelefonos.refresh();
                resetearFormulario();
            }
            txtTelefono.clear();
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error de Validación", e.getMessage());
        }
    }

    private void onEliminarTelefono(Telefono t) {
        TelefonoDAO dao = new TelefonoDAO();
        dao.eliminarTelefono(t);
         listaTelefonos.remove(t);
    }

    private void resetearFormulario() {
        telefonoEnEdicion = null;
        btnGuardar.setText("Añadir");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        txtTelefono.clear();

        btnCancelar.setVisible(false);
        btnCancelar.setManaged(false);
    }

    private void cargarTelefonos() {
        TelefonoDAO dao = new TelefonoDAO();
        listaTelefonos = FXCollections.observableArrayList(dao.obtenerTelefonosPorUsuario(persona.getId()));

        tablaTelefonos.setItems(listaTelefonos);
    }

    @FXML
    protected void onCancelarEdicionClick() {
        resetearFormulario();
    }

    @FXML
    protected void onCerrarClick() {
        Stage stage = (Stage) lblNombrePersona.getScene().getWindow();
        stage.close();
    }
}