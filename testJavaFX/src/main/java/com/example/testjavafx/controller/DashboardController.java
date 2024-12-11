package com.example.testjavafx.controller;

import com.example.testjavafx.App;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DashboardController {

    @FXML
    private VBox sidebar;

    @FXML
    private Pane contentArea;

    @FXML
    public void initialize() {
        hamburgerIcon.setOnMouseClicked(event -> toggleSidebar());
        loadHomePage();
        loadUserPage();
        loadStudenPage();
    }

    @FXML
    private ImageView hamburgerIcon;

    @FXML
    private Button homeButton;

    @FXML
    private Button userButton;

    @FXML
    private Button productButton;

    @FXML
    private Button studentButton;

    @FXML
    void OnClickHome(ActionEvent event) {
        loadHomePage();
    }

    private void loadHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Home.fxml"));
            Parent homePage = loader.load();
            contentArea.getChildren().setAll(homePage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickStudent(ActionEvent event) {
        loadStudenPage();
    }

    private void loadStudenPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Students.fxml"));
            Parent homePage = loader.load();
            contentArea.getChildren().setAll(homePage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OnClickProduct(ActionEvent event) {

    }

    @FXML
    void OnClickUser(ActionEvent event) {
        loadUserPage();
    }
    private void loadUserPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/User.fxml"));
            Parent homePage = loader.load();
            contentArea.getChildren().setAll(homePage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSidebarVisible = true;

    private void toggleSidebar() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);
        if (isSidebarVisible) {
            transition.setToX(-135);
            hideButtonText();
        } else {
            transition.setToX(0);
            showButtonText();
        }
        isSidebarVisible = !isSidebarVisible;
        transition.play();
    }

    private void hideButtonText() {
        homeButton.setText("");
        userButton.setText("");
        productButton.setText("");
        studentButton.setText("");
    }

    private void showButtonText() {
        homeButton.setText("Home");
        userButton.setText("User");
        productButton.setText("Product");
        studentButton.setText("Student");
    }
}


