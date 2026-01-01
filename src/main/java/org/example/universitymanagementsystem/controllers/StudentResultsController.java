package org.example.universitymanagementsystem.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.universitymanagementsystem.database.ResultDatabase;
import org.example.universitymanagementsystem.database.SubjectDatabase;
import org.example.universitymanagementsystem.models.Result;
import org.example.universitymanagementsystem.models.Student;
import org.example.universitymanagementsystem.models.Subject;
import org.example.universitymanagementsystem.util.CGPAUtility;
import org.example.universitymanagementsystem.util.Session;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentResultsController {

    @FXML
    private TableView<ResultRow> resultsTable;
    @FXML
    private TableColumn<ResultRow, String> semesterCol;
    @FXML
    private TableColumn<ResultRow, String> subjectCodeCol;
    @FXML
    private TableColumn<ResultRow, String> subjectNameCol;
    @FXML
    private TableColumn<ResultRow, Integer> creditCol;
    @FXML
    private TableColumn<ResultRow, Double> marksCol;
    @FXML
    private TableColumn<ResultRow, String> gradeCol;

    @FXML
    private Label cgpaLabel;
    @FXML
    private Label semesterGpaLabel; // To show GPA of selected or latest sem? Or just summary text area?

    @FXML
    public void initialize() {
        if (Session.currentStudent == null)
            return;

        setupTable();
        loadResults();
    }

    private void setupTable() {
        semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSemester()));

        subjectCodeCol = new TableColumn<>("Code");
        subjectCodeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubjectCode()));

        subjectNameCol = new TableColumn<>("Subject");
        subjectNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubjectName()));

        creditCol = new TableColumn<>("Credit");
        creditCol.setCellValueFactory(
                data -> new SimpleDoubleProperty(data.getValue().getCredit()).asObject().map(Double::intValue));

        marksCol = new TableColumn<>("Marks");
        marksCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getMarks()).asObject());

        gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGrade()));

        resultsTable.getColumns().setAll(semesterCol, subjectCodeCol, subjectNameCol, creditCol, marksCol, gradeCol);
        resultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadResults() {
        Student student = Session.currentStudent;
        List<Result> results = ResultDatabase.getResultsByStudentId(student.getId());

        ObservableList<ResultRow> rowData = FXCollections.observableArrayList();



        for (Result result : results) {
            Subject subject = SubjectDatabase.getSubjectById(result.getSubjectId());
            String subjectName = (subject != null) ? subject.getName() : "Unknown";
            String subjectCode = (subject != null) ? subject.getCode() : "N/A";
            int credit = (subject != null) ? subject.getCredit() : 0;
            String semester = result.getSemester();
            if (semester == null)
                semester = "N/A";

            rowData.add(
                    new ResultRow(semester, subjectName, subjectCode, credit, result.getMarks(), result.getGrade()));
        }

        resultsTable.setItems(rowData);

        // Show CGPA
        cgpaLabel.setText(String.format("CGPA: %.2f", student.getCgpa()));


        calculateSemesterGPAs(results);
    }

    private void calculateSemesterGPAs(List<Result> results) {
        // Group by Semester
        Map<String, List<Result>> bySemester = results.stream()
                .filter(r -> r.getSemester() != null)
                .collect(Collectors.groupingBy(Result::getSemester));

        StringBuilder sb = new StringBuilder();
        bySemester.forEach((sem, resList) -> {
            // Need subject credits for each result
            // Optimization: We could fetch map of all subjects first.
            // But doing it cleanly:
            double totalPoints = 0;
            int totalCredits = 0;
            for (Result r : resList) {
                Subject s = SubjectDatabase.getSubjectById(r.getSubjectId());
                if (s != null) {
                    double gp = CGPAUtility.calculateGradePoint(r.getGrade());
                    totalPoints += gp * s.getCredit();
                    totalCredits += s.getCredit();
                }
            }
            double gpa = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;
            sb.append(String.format("Sem %s: %.2f | ", sem, gpa));
        });

        semesterGpaLabel.setText(sb.toString());
    }

    public static class ResultRow {
        private final String semester;
        private final String subjectName;
        private final String subjectCode;
        private final int credit;
        private final double marks;
        private final String grade;

        public ResultRow(String semester, String subjectName, String subjectCode, int credit, double marks,
                String grade) {
            this.semester = semester;
            this.subjectName = subjectName;
            this.subjectCode = subjectCode;
            this.credit = credit;
            this.marks = marks;
            this.grade = grade;
        }

        public String getSemester() {
            return semester;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public int getCredit() {
            return credit;
        }

        public double getMarks() {
            return marks;
        }

        public String getGrade() {
            return grade;
        }
    }
}
