package com.example.testjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxmlPath) throws IOException {
        Parent parent = FXMLLoader.load(App.class.getResource(fxmlPath));
        primaryStage.getScene().setRoot(parent);
    }

    public static void main(String[] args) {
        launch();
    }
}
