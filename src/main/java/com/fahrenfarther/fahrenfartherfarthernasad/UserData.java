package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class UserData {

    public static void insertUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, date_of_birth, contact_no, license_no) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setDate(2, Date.valueOf(user.getDateOfBirth()));
            pstmt.setString(3, user.getContactNo());
            pstmt.setString(4, user.getLicenseNo());

            pstmt.executeUpdate();
        }
    }

    public static ObservableList<User> getAllUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM users ORDER BY id DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getString("name"),
                        rs.getDate("date_of_birth").toString(),
                        rs.getString("contact_no"),
                        rs.getString("license_no")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void deleteUser(String licenseNo) throws SQLException {
        String sql = "DELETE FROM users WHERE license_no = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, licenseNo);
            pstmt.executeUpdate();
        }
    }
}