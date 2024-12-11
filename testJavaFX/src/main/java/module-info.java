module com.example.testjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;
    requires org.postgresql.jdbc;
    requires java.sql;

    opens com.example.testjavafx to javafx.fxml;
    exports com.example.testjavafx;
    exports com.example.testjavafx.model;
    opens com.example.testjavafx.model to javafx.fxml;
    exports com.example.testjavafx.controller;
    opens com.example.testjavafx.controller to javafx.fxml;
    exports com.example.testjavafx.db;
    opens com.example.testjavafx.db to javafx.fxml;
}