package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class RentalData {

    public static void insertRental(Rental rental, String customer) throws SQLException {
        String sql = "INSERT INTO rentals (car, customer, start_date, end_date, total_cost, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rental.getCar());
            pstmt.setString(2, "Customer"); // You can add customer field to Rental class
            pstmt.setDate(3, Date.valueOf(rental.getStartDate()));
            pstmt.setDate(4, Date.valueOf(rental.getEndDate()));
            pstmt.setString(5, rental.getTotalCost());
            pstmt.setString(6, "Active");

            pstmt.executeUpdate();
        }
    }

    public static ObservableList<Rental> getAllRentals() {
        ObservableList<Rental> rentals = FXCollections.observableArrayList();
        String sql = "SELECT * FROM rentals ORDER BY id DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rentals.add(new Rental(
                        rs.getString("car"),
                        rs.getDate("start_date").toString(),
                        rs.getDate("end_date").toString(),
                        rs.getString("total_cost")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    public static void deleteRental(int id) throws SQLException {
        String sql = "DELETE FROM rentals WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}