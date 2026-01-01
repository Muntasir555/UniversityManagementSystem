package org.example.universitymanagementsystem.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.universitymanagementsystem.database.AttendanceDatabase;
import org.example.universitymanagementsystem.models.Attendance;
import org.example.universitymanagementsystem.models.Student;
import org.example.universitymanagementsystem.util.Session;

import java.util.List;

public class StudentAttendanceController {

    @FXML
    private TableView<Attendance> attendanceTable;

    @FXML
    public void initialize() {
        if (Session.currentStudent == null)
            return;

        setupTable();
        loadAttendance();
    }

    private void setupTable() {
        TableColumn<Attendance, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));

        TableColumn<Attendance, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(data -> {
            int subjectId = data.getValue().getSubjectId();
            if (subjectId == 0)
                return new SimpleStringProperty("General"); // Legacy support
            org.example.universitymanagementsystem.models.Subject subject = org.example.universitymanagementsystem.database.SubjectDatabase
                    .getSubjectById(subjectId);
            return new SimpleStringProperty(subject != null ? subject.getName() : "Unknown");
        });

        TableColumn<Attendance, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        attendanceTable.getColumns().setAll(dateCol, subjectCol, statusCol);
        attendanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadAttendance() {
        Student student = Session.currentStudent;
        List<Attendance> attendanceList = AttendanceDatabase.getAttendanceByStudentId(student.getId());
        ObservableList<Attendance> data = FXCollections.observableArrayList(attendanceList);
        attendanceTable.setItems(data);
    }
}
