package com.example.agendacrud.controller;

import com.example.agendacrud.AgendaService;
import com.example.agendacrud.models.Direccion;
import com.example.agendacrud.models.DireccionDAO;
import com.example.agendacrud.models.Persona;
import com.example.agendacrud.models.Telefono;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.swing.*;
import java.util.List;

public class DireccionesController {
    @FXML private Label lblNombrePersona;
    @FXML private Label lblModoEdicion;
    @FXML private ComboBox<String> comboDirecciones;
    @FXML private TextField txtDireccionManual;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    private final AgendaService agendaService = new AgendaService();

    @FXML private TableView<Direccion> tablaDirecciones;
    @FXML private TableColumn<Direccion, Integer> colId;
    @FXML private TableColumn<Direccion, String> colDetalle;
    @FXML private TableColumn<Direccion, Void> colAcciones;

    private Persona personaActual;
    private Direccion direccionEnEdicion = null;
    private ObservableList<Direccion> direccionesDePersona = FXCollections.observableArrayList();
    private List<Direccion> todasLasDireccionesDB;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        configurarColumnaAcciones();
        cargarComboGlobal();
    }

    private void configurarColumnaAcciones() {
        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnDesvincular = new Button();
            private final Button btnEditar = new Button();
            private final Button btnEliminar = new Button();
            private final HBox container = new HBox(10, btnEditar, btnDesvincular, btnEliminar);

            {
                FontIcon iconEdit = new FontIcon(FontAwesomeSolid.PENCIL_ALT);
                iconEdit.setIconColor(Color.WHITE);
                btnEditar.setGraphic(iconEdit);
                btnEditar.setStyle("-fx-background-color: #f39c12; -fx-cursor: hand;");

                FontIcon iconUnlink = new FontIcon(FontAwesomeSolid.UNLINK);
                iconUnlink.setIconColor(Color.WHITE);
                btnDesvincular.setGraphic(iconUnlink);
                btnDesvincular.setStyle("-fx-background-color: #3498db; -fx-cursor: hand;");

                FontIcon iconTrash = new FontIcon(FontAwesomeSolid.TRASH_ALT);
                iconTrash.setIconColor(Color.WHITE);
                btnEliminar.setGraphic(iconTrash);
                btnEliminar.setStyle("-fx-background-color: red ; -fx-cursor: hand;");

                btnEditar.setOnAction(e -> prepararEdicion(getTableView().getItems().get(getIndex())));
                btnDesvincular.setOnAction(e -> eliminarRelacion(getTableView().getItems().get(getIndex())));

                btnEliminar.setOnAction(e -> eliminarDireccion(getTableView().getItems().get(getIndex())));

                container.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    public void initData(Persona p) {
        this.personaActual = p;
        lblNombrePersona.setText(p.getNombre());
        refrescarTablaYPersona();
    }

    private void refrescarTablaYPersona() {
        List<Direccion> lista = agendaService.getDireccionesPorUsuario(personaActual.getId());
        personaActual.setDirecciones(lista);
        direccionesDePersona.setAll(personaActual.getDirecciones());
        tablaDirecciones.setItems(direccionesDePersona);
    }

    private void cargarComboGlobal() {
        todasLasDireccionesDB = agendaService.getTodasLasDirecciones();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Direccion d : todasLasDireccionesDB) items.add(d.getDireccion());
        comboDirecciones.setItems(items);
    }

    @FXML
    void onVincularExistenteClick() {
        String seleccion = comboDirecciones.getValue();
        if (seleccion == null) return;

        for (Direccion d : todasLasDireccionesDB) {
            if (d.getDireccion().equals(seleccion)) {
                boolean status = agendaService.vincularDireccion(d, personaActual);
                if(!status){
                    JOptionPane.showMessageDialog(null, "" +
                            "Esta dirección ya esta vinculada con el usuario", "Alerta", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            }
        }
        refrescarTablaYPersona();
        comboDirecciones.getSelectionModel().clearSelection();
    }

    @FXML
    void onGuardarDireccionClick() {
        String texto = txtDireccionManual.getText().trim();
        if (texto.isEmpty()) return;

        if (direccionEnEdicion == null) {
            Direccion nueva = new Direccion(-1, texto);
            boolean status = agendaService.crearDireccion(nueva);
            agendaService.vincularDireccion(nueva, personaActual);
        } else {
            Direccion tmp = new Direccion(direccionEnEdicion.getId(), texto);
            boolean status = agendaService.actualizarDireccion(tmp);
            if(!status){
                JOptionPane.showMessageDialog(null, "Esta direccion ya existe", "Alerta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            direccionEnEdicion.setDireccion(texto);
//            dao.editarDireccion(direccionEnEdicion);
        }

        resetearFormulario();
        cargarComboGlobal(); // Refrescar el combo por si cambió un texto
        refrescarTablaYPersona();
    }

    private void prepararEdicion(Direccion d) {
        direccionEnEdicion = d;
        txtDireccionManual.setText(d.getDireccion());
        lblModoEdicion.setText("Editando dirección ID: " + d.getId());
        btnGuardar.setText("Actualizar");
        btnCancelar.setVisible(true);
    }

    private void eliminarRelacion(Direccion d) {
        agendaService.desvincularDireccion(d, personaActual);
        refrescarTablaYPersona();
    }

    private void eliminarDireccion(Direccion d){
        agendaService.eliminarDireccion(d.getId());
        refrescarTablaYPersona();
    }

    @FXML
    void onCancelarEdicionClick() {
        resetearFormulario();
    }

    private void resetearFormulario() {
        direccionEnEdicion = null;
        txtDireccionManual.clear();
        lblModoEdicion.setText("Crear nueva dirección o editar seleccionada:");
        btnGuardar.setText("Guardar");
        btnCancelar.setVisible(false);
    }

    @FXML void onCerrarClick() {
        ((Stage)lblNombrePersona.getScene().getWindow()).close();
    }
}