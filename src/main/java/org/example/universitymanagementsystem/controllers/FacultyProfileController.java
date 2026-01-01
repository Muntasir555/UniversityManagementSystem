package org.example.universitymanagementsystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.universitymanagementsystem.models.Faculty;
import org.example.universitymanagementsystem.util.Session;

public class FacultyProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Label idLabel;

    @FXML
    public void initialize() {
        if (Session.currentFaculty != null) {
            Faculty f = Session.currentFaculty;
            nameLabel.setText(f.getName());
            emailLabel.setText(f.getEmail());
            departmentLabel.setText(f.getDepartment());
            idLabel.setText(String.valueOf(f.getId()));
        }
    }

    @FXML
    private void handleChangePassword(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/change_password.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Change Password");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
