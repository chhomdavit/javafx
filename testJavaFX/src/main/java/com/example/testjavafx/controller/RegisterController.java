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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

public class RegisterController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Label lblError;

    @FXML
    void btnOnBackToLogin(ActionEvent event) {
        try {
            App.setRoot("/fxml/Login.fxml");
        } catch (Exception e) {
            lblError.setText("An error occurred while navigating to login.");
            e.printStackTrace();
        }
    }

    @FXML
    void btnOnRegister(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (!password.equals(confirmPassword)) {
            lblError.setText("Passwords do not match.");
            return;
        }

        try {
            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);
            String query = "INSERT INTO login_users (username, password, salt, role) VALUES (?, ?, ?, ?)";
            try (Connection con = DBConnection.getCon();
                 PreparedStatement st = con.prepareStatement(query)) {

                st.setString(1, username);
                st.setString(2, hashedPassword);
                st.setString(3, Base64.getEncoder().encodeToString(salt));
                st.setString(4, Role.USER.name());

                int result = st.executeUpdate();

                if (result > 0) {
                    lblError.setText("Registration successful! Please login.");
                    try {
                        App.setRoot("/fxml/Login.fxml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    lblError.setText("Registration failed! Username already exists.");
                }
            } catch (SQLException e) {
                lblError.setText("Registration failed! Username already exists.");
            } catch (Exception e) {
                lblError.setText("An unexpected error occurred.");
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            lblError.setText("An error occurred during password hashing.");
        }
    }

    private byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 65536;
        int keyLength = 256;

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        byte[] hashedPassword = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
