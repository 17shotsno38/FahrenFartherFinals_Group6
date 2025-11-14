package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DBLauncher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Initialize database
        DatabaseManager.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(DBLauncher.class.getResource("Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("FahrenFarther Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}