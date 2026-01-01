package org.example.universitymanagementsystem.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.universitymanagementsystem.database.FacultyDatabase;
import org.example.universitymanagementsystem.models.Faculty;

import java.util.List;

public class FacultyListController {

    @FXML
    private TableView<Faculty> facultyTable;

    @FXML
    private javafx.scene.control.ChoiceBox<String> departmentFilter;

    @FXML
    public void initialize() {
        setupTable();

        departmentFilter.getItems().addAll("All", "CSE", "EEE", "Civil", "ME");
        departmentFilter.setValue("All");

        departmentFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadFaculty(newVal);
        });

        loadFaculty("All");
    }

    private void setupTable() {
        TableColumn<Faculty, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Faculty, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Faculty, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Faculty, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDepartment()));

        facultyTable.getColumns().addAll(idCol, nameCol, emailCol, deptCol);
        facultyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadFaculty(String department) {
        List<Faculty> facultyList;
        if (department == null || department.equals("All")) {
            facultyList = FacultyDatabase.getAllFaculty();
        } else {
            facultyList = FacultyDatabase.getFacultyByDepartment(department);
        }
        ObservableList<Faculty> data = FXCollections.observableArrayList(facultyList);
        facultyTable.setItems(data);
    }
}
