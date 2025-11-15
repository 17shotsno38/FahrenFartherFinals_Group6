package com.fahrenfarther.fahrenfartherfarthernasad;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.sql.*;

public class DBController {

    @FXML private Label lblTotalCars;
    @FXML private Label lblTotalUsers;
    @FXML private Label lblActiveRentals;
    @FXML private Label lblRevenue;

    @FXML
    public void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet carsResult = stmt.executeQuery("SELECT COUNT(*) as total FROM cars");
            if (carsResult.next()) {
                lblTotalCars.setText(String.valueOf(carsResult.getInt("total")));
            }

            ResultSet usersResult = stmt.executeQuery("SELECT COUNT(*) as total FROM users");
            if (usersResult.next()) {
                lblTotalUsers.setText(String.valueOf(usersResult.getInt("total")));
            }

            ResultSet rentalsResult = stmt.executeQuery("SELECT COUNT(*) as total FROM rentals WHERE status = 'Active'");
            if (rentalsResult.next()) {
                lblActiveRentals.setText(String.valueOf(rentalsResult.getInt("total")));
            }

            ResultSet revenueResult = stmt.executeQuery("SELECT total_cost FROM rentals");
            double totalRevenue = 0;
            while (revenueResult.next()) {
                String cost = revenueResult.getString("total_cost");
                if (cost != null && !cost.isEmpty()) {
                    // Remove ₱ symbol and convert to number
                    cost = cost.replace("₱", "").replace(",", "").trim();
                    try {
                        totalRevenue += Double.parseDouble(cost);
                    } catch (NumberFormatException e) {
                        // Skip invalid values
                    }
                }
            }
            lblRevenue.setText("₱" + String.format("%.2f", totalRevenue));

        } catch (SQLException e) {
            e.printStackTrace();
            // Set default values if database fails
            lblTotalCars.setText("0");
            lblTotalUsers.setText("0");
            lblActiveRentals.setText("0");
            lblRevenue.setText("₱0");
        }
    }

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
