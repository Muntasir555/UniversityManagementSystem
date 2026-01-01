package org.example.universitymanagementsystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.universitymanagementsystem.database.AttendanceDatabase;
import org.example.universitymanagementsystem.database.CourseAssignmentDatabase;
import org.example.universitymanagementsystem.database.StudentDatabase;
import org.example.universitymanagementsystem.database.SubjectDatabase;
import org.example.universitymanagementsystem.models.Attendance;
import org.example.universitymanagementsystem.models.CourseAssignment;
import org.example.universitymanagementsystem.models.Student;
import org.example.universitymanagementsystem.models.Subject;
import org.example.universitymanagementsystem.util.Session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacultyAttendanceController {

    @FXML
    private ComboBox<Subject> courseComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<AttendanceEntry> attendanceTable;

    @FXML
    private TableColumn<AttendanceEntry, String> rollNoCol;

    @FXML
    private TableColumn<AttendanceEntry, String> nameCol;

    @FXML
    private TableColumn<AttendanceEntry, CheckBox> statusCol;

    @FXML
    private Label messageLabel;

    private ObservableList<AttendanceEntry> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (Session.currentFaculty == null) {
            return;
        }

        setupTable();
        loadCourses();
        datePicker.setValue(LocalDate.now());

        courseComboBox.setOnAction(event -> loadStudents());
        datePicker.setOnAction(event -> loadStudents());
    }

    private void setupTable() {
        rollNoCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentId()));
        nameCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentName()));
        statusCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPresentCheckBox()));

        attendanceTable.setItems(studentList);
        attendanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadCourses() {
        List<CourseAssignment> assignments = CourseAssignmentDatabase
                .getAssignmentsByFaculty(Session.currentFaculty.getId());
        ObservableList<Subject> subjects = FXCollections.observableArrayList();
        Set<Integer> addedSubjectIds = new HashSet<>();

        for (CourseAssignment ca : assignments) {
            if (!addedSubjectIds.contains(ca.getSubjectId())) {
                Subject subject = SubjectDatabase.getSubjectById(ca.getSubjectId());
                if (subject != null) {
                    subjects.add(subject);
                    addedSubjectIds.add(subject.getId());
                }
            }
        }
        courseComboBox.setItems(subjects);
        // Custom cell factory to show subject code/name
        courseComboBox.setCellFactory(param -> new ListCell<Subject>() {
            @Override
            protected void updateItem(Subject item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCode() + " - " + item.getName());
                }
            }
        });
        courseComboBox.setButtonCell(new ListCell<Subject>() {
            @Override
            protected void updateItem(Subject item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCode() + " - " + item.getName());
                }
            }
        });
    }

    private void loadStudents() {
        studentList.clear();
        Subject selectedSubject = courseComboBox.getValue();
        if (selectedSubject == null)
            return;

        List<String> studentIds = CourseAssignmentDatabase
                .getStudentIdsByFacultyAndSubject(Session.currentFaculty.getId(), selectedSubject.getId());

        for (String studentId : studentIds) {
            Student student = StudentDatabase.getStudentById(studentId);
            if (student != null) {
                studentList.add(new AttendanceEntry(student.getId(), student.getName()));
            }
        }
    }

    // Helper class for TableView
    public static class AttendanceEntry {
        private String studentId;
        private String studentName;
        private CheckBox presentCheckBox;

        public AttendanceEntry(String studentId, String studentName) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.presentCheckBox = new CheckBox();
            this.presentCheckBox.setSelected(true); // Default present
        }

        public String getStudentId() {
            return studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public CheckBox getPresentCheckBox() {
            return presentCheckBox;
        }
    }

    @FXML
    private void handleSave() {
        Subject selectedSubject = courseComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedSubject == null || selectedDate == null) {
            messageLabel.setText("Please select course and date.");
            return;
        }

        String dateStr = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        int successCount = 0;

        for (AttendanceEntry entry : studentList) {
            Attendance attendance = new Attendance();
            attendance.setStudentId(entry.getStudentId());
            attendance.setSubjectId(selectedSubject.getId());
            attendance.setDate(dateStr);
            attendance.setStatus(entry.getPresentCheckBox().isSelected() ? "Present" : "Absent");

            if (AttendanceDatabase.markAttendance(attendance)) {
                successCount++;
            }
        }

        if (successCount > 0) {
            messageLabel.setText("Attendance saved successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");
        } else {
            messageLabel.setText("Failed.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
