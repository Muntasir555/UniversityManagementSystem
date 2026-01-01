package org.example.universitymanagementsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.example.universitymanagementsystem.database.NoticeDatabase;
import org.example.universitymanagementsystem.models.Notice;

import java.net.URL;
import java.util.ResourceBundle;

public class OpeningPageController implements Initializable {

    @FXML
    private ListView<Notice> noticeListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadNotices();
    }

    private void loadNotices() {
        noticeListView.getItems().addAll(NoticeDatabase.getAllNotices());
        noticeListView.setCellFactory(new Callback<ListView<Notice>, ListCell<Notice>>() {
            @Override
            public ListCell<Notice> call(ListView<Notice> param) {
                return new ListCell<Notice>() {
                    @Override
                    protected void updateItem(Notice item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getDate() + "\n" + item.getTitle());
                            setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");
                        }
                    }
                };
            }
        });

        noticeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showNoticeDetails(newValue);
                // Clear selection so the same item can be clicked again if needed (though
                // standard list behavior usually keeps it selected)
                // or just leave it. If we want to re-trigger on click, a mouse listener might
                // be better, but selection listener is standard for "view item".
                // Let's just keep it selected.
            }
        });
    }

    private void showNoticeDetails(Notice notice) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Notice Details");
        alert.setHeaderText(notice.getTitle());
        alert.setContentText(notice.getContent());
        alert.showAndWait();
    }

    @FXML
    private void openStudentLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/student_login.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openFacultyLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/faculty_login.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAdminLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/universitymanagementsystem/admin_login.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
