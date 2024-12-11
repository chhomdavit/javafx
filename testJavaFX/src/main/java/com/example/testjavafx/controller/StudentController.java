package com.example.testjavafx.controller;

import com.example.testjavafx.App;
import com.example.testjavafx.db.DBConnection;
import com.example.testjavafx.model.Student;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StudentController {

    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Student,String> colCourse;

    @FXML
    private TableColumn<Student ,String> colFirstName;

    @FXML
    private TableColumn<Student, Integer> colId;

    @FXML
    private TableColumn<Student ,String> colLastName;

    @FXML
    private TableView<Student> tableStuden;
    int id = 0;

    @FXML
    private TextField txtCourse;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    void btnOnClear(ActionEvent event) {
        txtFirstName.clear();
        txtLastName.clear();
        txtCourse.clear();
        btnSave.setDisable(false);
    }

    void clear(){
        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtCourse.setText(null);
    }

    @FXML
    void btnOnDelete(ActionEvent event) {
        if (id == 0) {
            return;
        }
        if (showConfirmation("Confirm Delete", "Are you sure you want to delete this student?")) {
            String delete = "DELETE FROM test_javafx WHERE ID = ?";
            con = DBConnection.getCon();
            try {
                st = con.prepareStatement(delete);
                st.setInt(1, id);
                st.executeUpdate();
                showStudents();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnOnSave(ActionEvent event) {
        String insert = "INSERT INTO test_javafx (FirstName, LastName, COURSE) VALUES (?, ?, ?)";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(insert);
            st.setString(1, txtFirstName.getText());
            st.setString(2, txtLastName.getText());
            st.setString(3, txtCourse.getText());
            st.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student saved successfully.");
            showStudents();
            clear();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getData(MouseEvent event) {
        Student student = tableStuden.getSelectionModel().getSelectedItem();
        if (student == null) {
            return;
        }
        id = student.getId();
        txtFirstName.setText(student.getFirstName());
        txtLastName.setText(student.getLastName());
        txtCourse.setText(student.getCourse());
        btnSave.setDisable(true);
    }

    @FXML
    void btnOnUpdate(ActionEvent event) {
        String update = "UPDATE test_javafx SET FirstName = ?, LastName = ?, COURSE = ? WHERE ID = ?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(update);
            st.setString(1, txtFirstName.getText());
            st.setString(2, txtLastName.getText());
            st.setString(3, txtCourse.getText());
            st.setInt(4, id);
            st.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully.");
            showStudents();
            clear();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnOnLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click OK to log out.");

        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                try {
                    App.setRoot("/fxml/Login.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void initialize() {
        showStudents();
    }

    public void showStudents() {
        ObservableList<Student> list = getStudents();
        tableStuden.setItems(list);
        colId.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(tableStuden.getItems().indexOf(cellData.getValue()) + 1);
        });
        colFirstName.setCellValueFactory(new PropertyValueFactory<Student,String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Student,String>("lastName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<Student,String>("course"));
    }

    private ObservableList<Student> getStudents() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        String query =  "SELECT * FROM test_javafx";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                Student student = new Student();
                student.setId(rs.getInt("ID"));
                student.setFirstName(rs.getString("FirstName"));
                student.setLastName(rs.getString("LastName"));
                student.setCourse(rs.getString("COURSE"));
                studentList.add(student);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return studentList;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String content) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(content);
        return confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
