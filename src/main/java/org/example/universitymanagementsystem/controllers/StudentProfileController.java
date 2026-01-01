package org.example.universitymanagementsystem.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.universitymanagementsystem.models.Student;
import org.example.universitymanagementsystem.util.Session;

public class StudentProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label cgpaLabel;

    @FXML
    public void initialize() {
        if (Session.currentStudent != null) {
            Student s = Session.currentStudent;
            nameLabel.setText(s.getName());
            emailLabel.setText(s.getEmail());
            departmentLabel.setText(s.getDepartment());
            idLabel.setText(String.valueOf(s.getId()));
            cgpaLabel.setText(String.format("%.2f", s.getCgpa()));
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
