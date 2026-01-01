package org.example.universitymanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.universitymanagementsystem.util.Session;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private void openRegisterStudent(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/register_student.fxml");
    }

    @FXML
    private void openRegisterFaculty(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/register_faculty.fxml");
    }

    @FXML
    private void openAssignCourse(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/admin_assign_course.fxml");
    }

    @FXML
    private void viewStudents(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/student_list.fxml");
    }

    @FXML
    private void viewFaculty(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/faculty_list.fxml");
    }

    @FXML
    private void openChangePassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/change_password.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        Session.clear();
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

    private void loadPage(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            Scene currentScene = ((Node) event.getSource()).getScene();
            if (currentScene.getRoot() instanceof BorderPane) {
                BorderPane borderPane = (BorderPane) currentScene.getRoot();
                Parent view = loader.load();
                borderPane.setCenter(view);
            } else {

                Scene scene = new Scene(loader.load(), 900, 600);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
