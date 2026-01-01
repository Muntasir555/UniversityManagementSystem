package org.example.universitymanagementsystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.universitymanagementsystem.database.CourseAssignmentDatabase;
import org.example.universitymanagementsystem.database.ResultDatabase;
import org.example.universitymanagementsystem.database.SubjectDatabase;
import org.example.universitymanagementsystem.models.CourseAssignment;
import org.example.universitymanagementsystem.models.Result;
import org.example.universitymanagementsystem.models.Subject;
import org.example.universitymanagementsystem.util.CGPAUtility;
import org.example.universitymanagementsystem.util.Session;

import java.util.List;

public class FacultyAddResultController {

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private TableView<StudentRow> studentsTable;
    @FXML
    private TableColumn<StudentRow, String> idColumn;
    @FXML
    private TableColumn<StudentRow, String> nameColumn;

    @FXML
    private TextField marksField;

    @FXML
    private Label statusLabel;

    private List<CourseAssignment> myAssignments;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        loadCourses();
    }

    private void loadCourses() {
        if (Session.currentFaculty == null) {
            statusLabel.setText("Error: Not logged in.");
            return;
        }

        int facultyId = Session.currentFaculty.getId();
        myAssignments = CourseAssignmentDatabase.getAssignmentsByFaculty(facultyId);


        ObservableList<String> subjects = FXCollections.observableArrayList();

        for (CourseAssignment ca : myAssignments) {
            Subject subject = SubjectDatabase.getSubjectById(ca.getSubjectId());
            if (subject != null) {
                String item = subject.getCode() + " - " + ca.getSemester();
                if (!subjects.contains(item)) {
                    subjects.add(item);
                }
            }
        }
        courseComboBox.setItems(subjects);
    }

    @FXML
    private void handleCourseSelection(ActionEvent event) {
        String selected = courseComboBox.getValue();
        if (selected == null)
            return;

        String[] parts = selected.split(" - ");
        String subjectCode = parts[0];
        String semester = parts[1];


        Subject subject = SubjectDatabase.getSubjectByCode(subjectCode);
        if (subject == null)
            return;

        ObservableList<StudentRow> rows = FXCollections.observableArrayList();

        // This is inefficient (iterating all assignments), but safe for now.
        for (CourseAssignment ca : myAssignments) {
            if (ca.getSubjectId() == subject.getId() && ca.getSemester().equals(semester)) {

                rows.add(new StudentRow(ca.getStudentId(), "Student " + ca.getStudentId()));
            }
        }
        studentsTable.setItems(rows);
    }

    @FXML
    private void publishResult(ActionEvent event) {
        String marksStr = marksField.getText();
        StudentRow selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
        String selectedCourse = courseComboBox.getValue(); // "Code - Sem"

        if (selectedStudent == null || selectedCourse == null || marksStr.isEmpty()) {
            statusLabel.setText("Select Student, Course and enter Marks.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            double marks = Double.parseDouble(marksStr);
            String grade = CGPAUtility.calculateGrade(marks);

            String[] parts = selectedCourse.split(" - ");
            Subject subject = SubjectDatabase.getSubjectByCode(parts[0]);
            String semester = parts[1];

            Result result = new Result();
            result.setStudentId(selectedStudent.getId());
            result.setSubjectId(subject.getId());
            result.setMarks(marks);
            result.setGrade(grade);
            result.setSemester(semester);

            if (ResultDatabase.addResult(result)) {
                statusLabel.setText("Published: " + grade);
                statusLabel.setStyle("-fx-text-fill: green;");
                marksField.clear();
            } else {
                statusLabel.setText("Failed to publish.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid Marks.");
        }
    }

    public static class StudentRow {
        private String id;
        private String name;

        public StudentRow(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
