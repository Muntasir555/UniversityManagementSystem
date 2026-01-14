package org.example.universitymanagementsystem.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.universitymanagementsystem.database.NoticeDatabase;
import org.example.universitymanagementsystem.models.Notice;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminNoticeBoardController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Notice> noticeTable;
    @FXML
    private TableColumn<Notice, Integer> idColumn;
    @FXML
    private TableColumn<Notice, String> titleColumn;
    @FXML
    private TableColumn<Notice, String> contentColumn;
    @FXML
    private TableColumn<Notice, String> dateColumn;

    private ObservableList<Notice> noticeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadNotices();
    }

    private void loadNotices() {
        noticeList.clear();
        List<Notice> notices = NoticeDatabase.getAllNotices();
        noticeList.addAll(notices);
        noticeTable.setItems(noticeList);
    }

    @FXML
    private void handleAddNotice(ActionEvent event) {
        String title = titleField.getText();
        String content = contentArea.getText();
        LocalDate selectedDate = datePicker.getValue();

        if (title.isEmpty() || content.isEmpty() || selectedDate == null) {
            showAlert("Error", "Please fill in all fields including the date");
            return;
        }

        String date = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        NoticeDatabase.addNotice(title, content, date);

        titleField.clear();
        contentArea.clear();
        datePicker.setValue(null);
        loadNotices();
        showAlert("Success", "Notice posted successfully");
    }

    @FXML
    private void handleDeleteNotice(ActionEvent event) {
        Notice selectedNotice = noticeTable.getSelectionModel().getSelectedItem();
        if (selectedNotice == null) {
            showAlert("Error", "Please select a notice to delete");
            return;
        }

        NoticeDatabase.deleteNotice(selectedNotice.getId());
        loadNotices();
        showAlert("Success", "Notice deleted successfully");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/admin_dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
