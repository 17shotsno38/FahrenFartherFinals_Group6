package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class CarData {

    public static void insertCar(Car car) throws SQLException {
        String sql = "INSERT INTO cars (model, year, license_plate, daily_rate, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getModel());
            pstmt.setInt(2, car.getYear());
            pstmt.setString(3, car.getLicensePlate());
            pstmt.setString(4, car.getDailyRate());
            pstmt.setString(5, "Available");

            pstmt.executeUpdate();
        }
    }

    public static ObservableList<Car> getAllCars() {
        ObservableList<Car> cars = FXCollections.observableArrayList();
        String sql = "SELECT * FROM cars ORDER BY id DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("license_plate"),
                        rs.getString("daily_rate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public static void deleteCar(String licensePlate) throws SQLException {
        String sql = "DELETE FROM cars WHERE license_plate = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, licensePlate);
            pstmt.executeUpdate();
        }
    }
}