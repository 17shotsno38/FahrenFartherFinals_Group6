package com.fahrenfarther.fahrenfartherfarthernasad;
import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/fahrenfarther_db";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

   
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createCarsTable = """
                CREATE TABLE IF NOT EXISTS cars (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    model VARCHAR(255) NOT NULL,
                    year INT NOT NULL,
                    license_plate VARCHAR(50) UNIQUE NOT NULL,
                    daily_rate VARCHAR(50) NOT NULL,
                    status VARCHAR(50) DEFAULT 'Available',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createCarsTable);

            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    date_of_birth DATE NOT NULL,
                    contact_no VARCHAR(50) NOT NULL,
                    license_no VARCHAR(50) UNIQUE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createUsersTable);

            // Create rentals table
            String createRentalsTable = """
                CREATE TABLE IF NOT EXISTS rentals (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    car VARCHAR(255) NOT NULL,
                    customer VARCHAR(255) NOT NULL,
                    start_date DATE NOT NULL,
                    end_date DATE NOT NULL,
                    total_cost VARCHAR(50) NOT NULL,
                    status VARCHAR(50) DEFAULT 'Active',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createRentalsTable);

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
