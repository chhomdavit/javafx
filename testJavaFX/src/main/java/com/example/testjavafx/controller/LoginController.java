package com.example.testjavafx.controller;

import com.example.testjavafx.App;
import com.example.testjavafx.db.DBConnection;
import com.example.testjavafx.model.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

public class LoginController {

    @FXML private Label lblError;

    @FXML private PasswordField txtPassword;

    @FXML private TextField txtUsername;

    @FXML
    void btnOnRegister(ActionEvent event) {
        try {
            App.setRoot("/fxml/Register.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnOnLogin(ActionEvent event) {

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        String query = "SELECT password, salt, role FROM login_users WHERE username = ?";

        try (Connection con = DBConnection.getCon();
             PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String storedSalt = rs.getString("salt");
                String roleString = rs.getString("role");

                byte[] saltBytes = Base64.getDecoder().decode(storedSalt);
                String hashedPassword = hashPassword(password, saltBytes);

                if (hashedPassword.equals(storedHashedPassword)) {
                    Role role = Role.valueOf(roleString.toUpperCase());
                    if (role == Role.USER) {
                        App.setRoot("/fxml/Students.fxml");
                    } else if (role == Role.ADMIN) {
                        App.setRoot("/fxml/Dashboard.fxml");
                    }
                } else {
                    lblError.setText("Invalid username or password!");
                }
            } else {
                lblError.setText("Invalid username or password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblError.setText("An unexpected error occurred.");
        }
    }

    private String hashPassword(String password, byte[] salt) throws Exception {
        int iterations = 65536;
        int keyLength = 256;

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        byte[] hashedPassword = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
