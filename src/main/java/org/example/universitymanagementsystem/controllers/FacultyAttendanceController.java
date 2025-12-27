package org.example.universitymanagementsystem.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.universitymanagementsystem.database.AttendanceDatabase;
import org.example.universitymanagementsystem.models.Attendance;

import java.time.format.DateTimeFormatter;

public class FacultyAttendanceController {

    @FXML
    private TextField studentIdField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    public void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("Present", "Absent", "Late"));
    }

    @FXML
    private void markAttendance(ActionEvent event) {
        String studentIdStr = studentIdField.getText();
        String status = statusComboBox.getValue();

        if (studentIdStr.isEmpty() || datePicker.getValue() == null || status == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);
            String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Attendance attendance = new Attendance();
            attendance.setStudentId(studentId);
            attendance.setDate(date);
            attendance.setStatus(status);

            if (AttendanceDatabase.markAttendance(attendance)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Attendance marked successfully.");
                studentIdField.clear();
                statusComboBox.getSelectionModel().clearSelection();
                datePicker.setValue(null);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to mark attendance.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Student ID.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
