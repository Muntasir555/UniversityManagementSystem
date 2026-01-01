package org.example.universitymanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.example.universitymanagementsystem.database.AdminDatabase;
import org.example.universitymanagementsystem.database.FacultyDatabase;
import org.example.universitymanagementsystem.database.StudentDatabase;
import org.example.universitymanagementsystem.util.Session;

public class ChangePasswordController {

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label statusLabel;

    @FXML
    private void handleSave(ActionEvent event) {
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            statusLabel.setText("Please fill all fields.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            statusLabel.setText("New passwords do not match.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean success = false;
        String currentPassword = "";

        if (Session.currentAdmin != null) {
            currentPassword = Session.currentAdmin.getPassword();
            if (!currentPassword.equals(oldPass)) {
                statusLabel.setText("Incorrect old password.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            success = AdminDatabase.updatePassword(Session.currentAdmin.getUsername(), newPass);
            if (success)
                Session.currentAdmin.setPassword(newPass);

        } else if (Session.currentStudent != null) {
            currentPassword = Session.currentStudent.getPassword();
            if (!currentPassword.equals(oldPass)) {
                statusLabel.setText("Incorrect old password.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            success = StudentDatabase.updatePassword(Session.currentStudent.getId(), newPass);
            if (success)
                Session.currentStudent.setPassword(newPass);

        } else if (Session.currentFaculty != null) {
            currentPassword = Session.currentFaculty.getPassword();
            if (!currentPassword.equals(oldPass)) {
                statusLabel.setText("Incorrect old password.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            success = FacultyDatabase.updatePassword(Session.currentFaculty.getId(), newPass);
            if (success)
                Session.currentFaculty.setPassword(newPass);
        }

        if (success) {
            statusLabel.setText("Password changed successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            // Optional: Close window after delay or let user close
        } else {
            statusLabel.setText("Database error occurred.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
