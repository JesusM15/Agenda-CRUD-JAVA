module com.example.agendacrud {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires mysql.connector.j;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.example.agendacrud to javafx.fxml;
    exports com.example.agendacrud;

    exports com.example.agendacrud.models;
    opens com.example.agendacrud.controller to javafx.fxml;
}