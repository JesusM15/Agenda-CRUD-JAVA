package com.example.agendacrud.controller;

import com.example.agendacrud.models.Persona;
import com.example.agendacrud.models.PersonaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.paint.Color;


public class HelloController {
    private Persona personaEnEdicion = null;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, Integer> colId;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private TableColumn<Persona, Void> colAcciones;

    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;

    private ObservableList<Persona> listaPersonas = FXCollections.observableArrayList();

    @FXML
    protected void onHelloButtonClick(){
        PersonaDAO dao = new PersonaDAO();
        dao.consultarPersonas();
    }

    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnTelefonos = new Button("");
            private final Button btnEliminar = new Button("");
            private final Button btnEditar = new Button("");
            private final Button btnDirecciones = new Button("");

            private final FontIcon iconoEliminar = new FontIcon(FontAwesomeSolid.TRASH_ALT);
            private final FontIcon iconoEditar = new FontIcon(FontAwesomeSolid.PENCIL_ALT);
            private final FontIcon iconoTelefonos = new FontIcon(FontAwesomeSolid.PHONE_ALT);
            private final FontIcon iconoDirecciones = new FontIcon(FontAwesomeSolid.MAP_PIN);


            private final HBox contenedorBotones = new HBox(10, btnDirecciones, btnTelefonos, btnEliminar, btnEditar);

            {
                btnEditar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
                btnTelefonos.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                btnDirecciones.setStyle("-fx-background-color: #00FFE3; -fx-text-fill: white; -fx-cursor: hand;");


                iconoEliminar.setIconSize(16);
                iconoEliminar.setIconColor(Color.WHITE);
                btnEliminar.setGraphic(iconoEliminar);

                iconoEditar.setIconSize(16);
                iconoEditar.setIconColor(Color.WHITE);
                btnEditar.setGraphic(iconoEditar);

                iconoTelefonos.setIconSize(16);
                iconoTelefonos.setIconColor(Color.WHITE);
                btnTelefonos.setGraphic(iconoTelefonos);

                iconoDirecciones.setIconSize(16);
                iconoDirecciones.setIconColor(Color.WHITE);
                btnDirecciones.setGraphic(iconoDirecciones);

                contenedorBotones.setAlignment(javafx.geometry.Pos.CENTER);

                btnDirecciones.setOnAction(event -> {
                    Persona p = getTableView().getItems().get(getIndex());
                    abrirModalDirecciones(p);
                });

                btnTelefonos.setOnAction(event -> {
                    Persona p = getTableView().getItems().get(getIndex());
                    abrirModalTelefonos(p);
                });

                btnEditar.setOnAction(event -> {
                    Persona p = getTableView().getItems().get(getIndex());
                    prepararEdicion(p);
                });

                btnEliminar.setOnAction(event -> {
                    Persona p = getTableView().getItems().get(getIndex());
                    eliminarPersonaDeTablaYBase(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedorBotones);
                }
            }
        });

        PersonaDAO dao = new PersonaDAO();
        listaPersonas = FXCollections.observableArrayList(dao.consultarPersonas());

        tablaPersonas.setItems(listaPersonas);
    }

    private void abrirModalDirecciones(Persona p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/agendacrud/direcciones-view.fxml"));
            Parent root = loader.load();

            DireccionesController controller = loader.getController();
            controller.initData(p);

            Stage stage = new Stage();
            stage.setTitle("Direcciones de " + p.getNombre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirModalTelefonos(Persona p) {

        try {
            System.out.println("Abriendo agenda de: " + p.getNombre());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/agendacrud/telefonos-view.fxml"));
            Parent root = loader.load();

            TelefonosController controller = loader.getController();
            controller.initData(p);

            Stage stage = new Stage();
            stage.setTitle("Teléfonos de " + p.getNombre());
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarPersonaDeTablaYBase(Persona p) {
        PersonaDAO dao = new PersonaDAO();
        dao.eliminarPersona(p.getId());
        listaPersonas.remove(p);
    }

    private void prepararEdicion(Persona p) {
        personaEnEdicion = p;
        txtNombre.setText(p.getNombre());
//        txtDireccion.setText(p.getDireccion());
        btnGuardar.setText("Actualizar");
        btnGuardar.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");

        btnCancelar.setVisible(true);
        btnCancelar.setManaged(true);
    }

    @FXML
    protected void onGuardarClick() {
        String nom = txtNombre.getText();
        String dir = txtDireccion.getText();

        if (nom.isEmpty()) return;

        PersonaDAO dao = new PersonaDAO();

        if (personaEnEdicion == null) {
            Persona nueva = new Persona(-1, nom);
            dao.crearPersona(nueva);
            listaPersonas.add(nueva);
        } else {
            personaEnEdicion.setNombre(nom);
/*
            personaEnEdicion.setDireccion(dir);
*/

            dao.editarPersona(personaEnEdicion);
            tablaPersonas.refresh();

            personaEnEdicion = null;

            btnGuardar.setText("Crear Contacto");
            btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        }
        txtNombre.clear();
        txtDireccion.clear();
    }

    @FXML
    protected void onCancelarEdicionClick() {
        resetearFormulario();
    }

    private void resetearFormulario() {
        personaEnEdicion = null;

        txtNombre.clear();
        txtDireccion.clear();

        btnGuardar.setText("Crear Contacto");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        btnCancelar.setVisible(false);
        btnCancelar.setManaged(false);

    }

}
