package com.fahrenfarther.fahrenfartherfarthernasad;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    public static void switchScene(Stage stage, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlFile));

            boolean wasMaximized = stage.isMaximized();

            Scene currentScene = stage.getScene();
            double width = currentScene.getWidth();
            double height = currentScene.getHeight();

            Scene newScene = new Scene(root, width, height);
            stage.setScene(newScene);

            if (wasMaximized) {
                stage.setMaximized(true);
            }

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
