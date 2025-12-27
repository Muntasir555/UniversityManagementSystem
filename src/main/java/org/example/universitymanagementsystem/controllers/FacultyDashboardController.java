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

public class FacultyDashboardController {

    @FXML
    private void viewProfile(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/faculty_profile.fxml");
    }

    @FXML
    private void viewCourses(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/faculty_courses.fxml");
    }

    @FXML
    private void markAttendance(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/faculty_attendance.fxml");
    }

    @FXML
    private void addResult(ActionEvent event) {
        loadPage(event, "/org/example/universitymanagementsystem/faculty_add_result.fxml");
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
