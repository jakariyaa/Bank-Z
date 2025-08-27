package com.bankz;

import java.sql.SQLException;

import com.bankz.util.DatabaseManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    
    public static final int DASHBOARD_WIDTH = 1280;
    public static final int DASHBOARD_HEIGHT = 720;
    public static final int LOGIN_WIDTH = 900;
    public static final int LOGIN_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseManager.initializeDatabase();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankz/fxml/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT);

            scene.getStylesheets().add(getClass().getResource("/com/bankz/styles/style.css").toExternalForm());
            primaryStage.setTitle("Bank-Z");
            primaryStage.setScene(scene);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                try {
                    DatabaseManager.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}