package org.example.universitymanagementsystem.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.universitymanagementsystem.database.StudentDatabase;
import org.example.universitymanagementsystem.models.Student;

import java.util.List;

public class StudentListController {

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private javafx.scene.control.ChoiceBox<String> departmentFilter;

    @FXML
    private javafx.scene.control.ChoiceBox<String> batchFilter;

    @FXML
    public void initialize() {
        setupTable();

        departmentFilter.getItems().addAll("All", "CSE", "EEE", "Civil", "ME");
        departmentFilter.setValue("All");

        List<String> batches = StudentDatabase.getDistinctBatches();
        batchFilter.getItems().add("All");
        batchFilter.getItems().addAll(batches);
        batchFilter.setValue("All");

        departmentFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadStudents();
        });

        batchFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadStudents();
        });

        loadStudents();
    }

    private void setupTable() {
        TableColumn<Student, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Student, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartment()));

        TableColumn<Student, String> batchCol = new TableColumn<>("Batch");
        batchCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBatch()));

        // Fix generic array warning by listing columns explicitly or safe unchecked
        // cast (but explicit add is cleaner)
        studentsTable.getColumns().clear();
        studentsTable.getColumns().add(nameCol);
        studentsTable.getColumns().add(idCol);
        studentsTable.getColumns().add(emailCol);
        studentsTable.getColumns().add(deptCol);
        studentsTable.getColumns().add(batchCol);

        // CONSTRAINED_RESIZE_POLICY is deprecated in Java 20/21? It might be available
        // as static field in TableView still for some versions,
        // or replaced by TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN or
        // similar.
        // Actually, JavaFX 20+ deprecated it for
        // CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN or
        // CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS.
        // Let's use CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN if available, or just
        // leave it if it works but deprecated.
        // To be safe and "perfect", let's use the replacement if we are sure of the
        // version.
        // If unsure, removing it is safer than breaking, but resizing is nice.
        studentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void loadStudents() {
        String department = departmentFilter.getValue();
        String batch = batchFilter.getValue();

        List<Student> students;

        boolean isDeptAll = department == null || department.equals("All");
        boolean isBatchAll = batch == null || batch.equals("All");

        if (isDeptAll && isBatchAll) {
            students = StudentDatabase.getAllStudents();
        } else if (!isDeptAll && isBatchAll) {
            students = StudentDatabase.getStudentsByDepartment(department);
        } else if (isDeptAll && !isBatchAll) {
            students = StudentDatabase.getStudentsByBatch(batch);
        } else {
            students = StudentDatabase.getStudentsByDepartmentAndBatch(department, batch);
        }

        ObservableList<Student> data = FXCollections.observableArrayList(students);
        studentsTable.setItems(data);
    }
}
