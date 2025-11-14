package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class DBController {

    @FXML private Label lblTotalCars;
    @FXML private Label lblTotalUsers;
    @FXML private Label lblActiveRentals;
    @FXML private Label lblRevenue;


    @FXML
    public void handleCarsClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "CarDashboard.fxml");
    }

    @FXML
    public void handleUsersClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "UsersDashboard.fxml");
    }

    @FXML
    public void handleRentalsClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "RentalsDashboard.fxml");
    }
}