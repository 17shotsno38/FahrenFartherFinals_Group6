package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Mock Database - Stores all data in memory instead of MySQL
 * Data will be lost when app closes, but no MySQL setup needed!
 */
public class MockDatabase {

    // In-memory storage
    private static final ObservableList<Car> cars = FXCollections.observableArrayList();
    private static final ObservableList<User> users = FXCollections.observableArrayList();
    private static final ObservableList<Rental> rentals = FXCollections.observableArrayList();

    // Auto-increment IDs
    private static int nextCarId = 1;
    private static int nextUserId = 1;
    private static int nextRentalId = 1;

    /**
     * Initialize with sample data
     */
    public static void initialize() {
        // Add sample cars
        if (cars.isEmpty()) {
            cars.add(new Car("Toyota Camry", 2023, "ABC-1234", "₱2500"));
            cars.add(new Car("Honda Civic", 2022, "XYZ-5678", "₱2200"));
            cars.add(new Car("Ford Mustang", 2024, "DEF-9012", "₱5000"));
        }

        // Add sample users
        if (users.isEmpty()) {
            users.add(new User("Juan Dela Cruz", "1990-05-15", "09171234567", "N01-12-345678"));
            users.add(new User("Maria Santos", "1985-08-22", "09181234567", "N02-13-456789"));
            users.add(new User("Pedro Reyes", "1992-11-30", "09191234567", "N03-14-567890"));
        }

        // Add sample rentals
        if (rentals.isEmpty()) {
            Rental rental1 = new Rental("Toyota Camry", "2024-11-01", "2024-11-05", "₱10000");
            rental1.setId(nextRentalId++);
            rentals.add(rental1);

            Rental rental2 = new Rental("Honda Civic", "2024-11-10", "2024-11-15", "₱11000");
            rental2.setId(nextRentalId++);
            rentals.add(rental2);
        }

        System.out.println("✅ Mock database initialized with sample data!");
    }

    // ==================== CAR OPERATIONS ====================

    public static ObservableList<Car> getAllCars() {
        return cars;
    }

    public static void insertCar(Car car) {
        cars.add(car);
        System.out.println("✅ Car added: " + car.getModel());
    }

    public static void deleteCar(String licensePlate) {
        cars.removeIf(car -> car.getLicensePlate().equals(licensePlate));
        System.out.println("✅ Car deleted: " + licensePlate);
    }

    // ==================== USER OPERATIONS ====================

    public static ObservableList<User> getAllUsers() {
        return users;
    }

    public static void insertUser(User user) {
        users.add(user);
        System.out.println("✅ User added: " + user.getName());
    }

    public static void deleteUser(String licenseNo) {
        users.removeIf(user -> user.getLicenseNo().equals(licenseNo));
        System.out.println("✅ User deleted: " + licenseNo);
    }

    // ==================== RENTAL OPERATIONS ====================

    public static ObservableList<Rental> getAllRentals() {
        return rentals;
    }

    public static void insertRental(Rental rental, String customer) {
        rental.setId(nextRentalId++);
        rentals.add(rental);
        System.out.println("✅ Rental added: " + rental.getCar() + " for " + customer);
    }

    public static void deleteRental(int id) {
        rentals.removeIf(rental -> rental.getId() == id);
        System.out.println("✅ Rental deleted: ID " + id);
    }

    // ==================== DASHBOARD STATS ====================

    public static int getTotalCars() {
        return cars.size();
    }

    public static int getTotalUsers() {
        return users.size();
    }

    public static int getActiveRentals() {
        return rentals.size(); // All rentals considered active in mock
    }

    public static double getTotalRevenue() {
        double total = 0;
        for (Rental rental : rentals) {
            String cost = rental.getTotalCost();
            cost = cost.replace("₱", "").replace(",", "").trim();
            try {
                total += Double.parseDouble(cost);
            } catch (NumberFormatException e) {
                // Skip invalid values
            }
        }
        return total;
    }
}
