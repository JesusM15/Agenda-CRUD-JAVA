module com.example.agendacrud {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.agendacrud to javafx.fxml;
    exports com.example.agendacrud;

    exports com.example.agendacrud.models;
    opens com.example.agendacrud.controller to javafx.fxml;
}