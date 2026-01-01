package org.example.universitymanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.universitymanagementsystem.database.StudentDatabase;
import org.example.universitymanagementsystem.models.Student;

public class RegisterStudentController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private javafx.scene.control.ChoiceBox<String> departmentChoiceBox;

    @FXML
    private TextField batchField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        departmentChoiceBox.getItems().addAll("CSE", "EEE", "Civil", "ME");
    }

    @FXML
    public void registerStudent(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String department = departmentChoiceBox.getValue();
        String batch = batchField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || department == null || department.isEmpty() || batch.isEmpty()
                || password.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Student student = new Student(name, email, department, batch, password);
        boolean success = StudentDatabase.addStudent(student);

        if (success) {
            statusLabel.setText("Student registered successfully!");
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
        batchField.clear();
        passwordField.clear();
    }
}
