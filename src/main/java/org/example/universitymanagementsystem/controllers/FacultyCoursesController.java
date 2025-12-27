package org.example.universitymanagementsystem.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.universitymanagementsystem.database.SubjectDatabase;
import org.example.universitymanagementsystem.models.Faculty;
import org.example.universitymanagementsystem.models.Subject;
import org.example.universitymanagementsystem.util.Session;

import java.util.List;

public class FacultyCoursesController {

    @FXML
    private TableView<Subject> coursesTable;

    @FXML
    public void initialize() {
        if (Session.currentFaculty == null)
            return;

        setupTable();
        loadCourses();
    }

    private void setupTable() {
        TableColumn<Subject, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Subject, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));

        TableColumn<Subject, Integer> creditCol = new TableColumn<>("Credit");
        creditCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCredit()).asObject());

        coursesTable.getColumns().addAll(nameCol, codeCol, creditCol);
        coursesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadCourses() {
        Faculty faculty = Session.currentFaculty;
        List<Subject> subjects = SubjectDatabase.getSubjectsByDepartment(faculty.getDepartment());
        ObservableList<Subject> data = FXCollections.observableArrayList(subjects);
        coursesTable.setItems(data);
    }
}
