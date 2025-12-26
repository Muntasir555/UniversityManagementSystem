package org.example.universitymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("openingpage.fxml")
            );

            Scene scene = new Scene(loader.load(), 900, 600);

            stage.setTitle("University Management System");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        org.example.universitymanagementsystem.util.DBUtil.initializeDatabase();
        launch(args);
    }

}