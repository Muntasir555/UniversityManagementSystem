package org.example.universitymanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.universitymanagementsystem.database.StudentDatabase;
import org.example.universitymanagementsystem.models.Student;
import org.example.universitymanagementsystem.util.Session;

import java.io.IOException;

public class StudentLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private javafx.scene.control.ChoiceBox<String> departmentChoiceBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        departmentChoiceBox.getItems().addAll("CSE", "EEE", "Civil", "ME");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String department = departmentChoiceBox.getValue();

        if (email.isEmpty() || password.isEmpty() || department == null || department.isEmpty()) {
            errorLabel.setText("Please enter all details including department.");
            return;
        }

        Student student = StudentDatabase.validateLogin(email, password, department);

        if (student != null) {
            Session.clear();
            Session.currentStudent = student;
            navigateToDashboard(event);
        } else {
            errorLabel.setText("Invalid credentials.");
        }
    }

    private void navigateToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/student_dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/openingpage.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
