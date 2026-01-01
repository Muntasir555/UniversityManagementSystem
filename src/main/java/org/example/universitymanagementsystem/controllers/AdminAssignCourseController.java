package org.example.universitymanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.universitymanagementsystem.database.CourseAssignmentDatabase;
import org.example.universitymanagementsystem.database.FacultyDatabase;
import org.example.universitymanagementsystem.database.StudentDatabase;
import org.example.universitymanagementsystem.database.SubjectDatabase;
import org.example.universitymanagementsystem.models.CourseAssignment;
import org.example.universitymanagementsystem.models.Faculty;
import org.example.universitymanagementsystem.models.Student;
import org.example.universitymanagementsystem.models.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminAssignCourseController {

    @FXML
    private ComboBox<String> departmentComboBox;

    @FXML
    private ComboBox<String> batchComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private VBox courseContainer;

    @FXML
    private Label statusLabel;

    private List<Faculty> allFaculty;
    private List<CourseRow> courseRows = new ArrayList<>();

    @FXML
    public void initialize() {

        departmentComboBox.getItems().addAll("CSE", "EEE", "Civil", "ME");


        List<String> batches = StudentDatabase.getDistinctBatches();
        batchComboBox.getItems().addAll(batches);


        semesterComboBox.getItems().addAll("1-1", "1-2", "2-1", "2-2", "3-1", "3-2", "4-1", "4-2");


        allFaculty = FacultyDatabase.getAllFaculty();


        addCourseRow();
    }

    @FXML
    public void addCourseRow() {
        CourseRow row = new CourseRow();
        courseRows.add(row);
        courseContainer.getChildren().add(row.container);
    }

    @FXML
    public void handleAssign(ActionEvent event) {
        String dept = departmentComboBox.getValue();
        String batch = batchComboBox.getValue();
        String semester = semesterComboBox.getValue();

        if (dept == null || batch == null || semester == null) {
            statusLabel.setText("Please select Department, Batch, and Semester.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        List<Student> students = StudentDatabase.getStudentsByDepartmentAndBatch(dept, batch);
        if (students.isEmpty()) {
            statusLabel.setText("No students found for " + dept + " Batch " + batch);
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        int totalAssigned = 0;
        int failedRows = 0;
        StringBuilder errorMsg = new StringBuilder();

        for (CourseRow row : courseRows) {
            if (!row.areFieldsFilled())
                continue;

            String code = row.codeField.getText().trim();
            String name = row.nameField.getText().trim();
            String creditStr = row.creditField.getText().trim();


            int facultyIndex = row.facultyComboBox.getSelectionModel().getSelectedIndex();
            if (facultyIndex < 0) {
                errorMsg.append("Faculty not selected for ").append(code).append("; ");
                failedRows++;
                continue;
            }
            Faculty faculty = allFaculty.get(facultyIndex);


            Subject subjectToAssign = null;
            try {
                int credit = Integer.parseInt(creditStr);
                Subject existing = SubjectDatabase.getSubjectByCode(code);

                if (existing != null) {
                    subjectToAssign = existing;
                } else {
                    Subject newSub = new Subject();
                    newSub.setCode(code);
                    newSub.setName(name);
                    newSub.setCredit(credit);
                    newSub.setDepartment(dept);
                    if (SubjectDatabase.addSubject(newSub)) {
                        subjectToAssign = SubjectDatabase.getSubjectByCode(code);
                    } else {
                        errorMsg.append("Failed to create subject ").append(code).append("; ");
                        failedRows++;
                        continue;
                    }
                }
            } catch (NumberFormatException e) {
                errorMsg.append("Invalid credit for ").append(code).append("; ");
                failedRows++;
                continue;
            }


            if (subjectToAssign != null) {
                int count = 0;
                for (Student student : students) {
                    CourseAssignment assignment = new CourseAssignment();
                    assignment.setStudentId(student.getId());
                    assignment.setSubjectId(subjectToAssign.getId());
                    assignment.setFacultyId(faculty.getId());
                    assignment.setSemester(semester);

                    if (CourseAssignmentDatabase.assignCourse(assignment)) {
                        count++;
                    }
                }
                totalAssigned += count;
            }
        }

        if (failedRows == 0 && totalAssigned > 0) {
            statusLabel.setText("Successfully assigned " + totalAssigned + " student-courses.");
            statusLabel.setStyle("-fx-text-fill: green;");

        } else if (failedRows > 0) {
            statusLabel.setText("Partial success. Errors: " + errorMsg.toString());
            statusLabel.setStyle("-fx-text-fill: orange;");
        } else {
            statusLabel.setText("No assignments made. Please check inputs.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }


    private class CourseRow {
        HBox container;
        TextField codeField;
        TextField nameField;
        TextField creditField;
        ComboBox<String> facultyComboBox;
        Button removeButton;

        public CourseRow() {
            container = new HBox(10);
            container.setAlignment(Pos.CENTER_LEFT);

            codeField = new TextField();
            codeField.setPromptText("Code (e.g. CSE101)");
            codeField.setPrefWidth(100);

            nameField = new TextField();
            nameField.setPromptText("Subject Name");
            nameField.setPrefWidth(150);

            creditField = new TextField();
            creditField.setPromptText("Cr");
            creditField.setPrefWidth(50);

            facultyComboBox = new ComboBox<>();
            facultyComboBox.setPromptText("Select Faculty");
            facultyComboBox.setPrefWidth(200);


            List<String> facultyNames = allFaculty.stream()
                    .map(f -> f.getName() + " (" + f.getDepartment() + ")")
                    .collect(Collectors.toList());
            facultyComboBox.getItems().addAll(facultyNames);

            removeButton = new Button("X");
            removeButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
            removeButton.setOnAction(e -> removeRow());


            codeField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    String code = codeField.getText().trim();
                    if (!code.isEmpty()) {
                        Subject existing = SubjectDatabase.getSubjectByCode(code);
                        if (existing != null) {
                            nameField.setText(existing.getName());
                            creditField.setText(String.valueOf(existing.getCredit()));
                        }
                    }
                }
            });

            container.getChildren().addAll(codeField, nameField, creditField, facultyComboBox, removeButton);
        }

        private void removeRow() {
            courseRows.remove(this);
            courseContainer.getChildren().remove(this.container);
        }

        public boolean areFieldsFilled() {

            return !codeField.getText().trim().isEmpty();
        }
    }
}
