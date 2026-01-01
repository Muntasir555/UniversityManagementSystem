package org.example.universitymanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.universitymanagementsystem.database.FacultyDatabase;
import org.example.universitymanagementsystem.models.Faculty;

public class RegisterFacultyController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private javafx.scene.control.ChoiceBox<String> departmentChoiceBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        departmentChoiceBox.getItems().addAll("CSE", "EEE", "Civil", "ME", "Physics", "Math", "Chemistry");
    }

    @FXML
    public void registerFaculty(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String department = departmentChoiceBox.getValue();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || department == null || department.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Faculty faculty = new Faculty(name, email, department, password);
        boolean success = FacultyDatabase.addFaculty(faculty);

        if (success) {
            statusLabel.setText("Faculty registered successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } else {
            statusLabel.setText("Registration failed. Email might be duplicate.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        departmentChoiceBox.setValue(null);
        passwordField.clear();
    }
}
