package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.ObservableList;

public class RentalData {

    public static void insertRental(Rental rental, String customer) {
        MockDatabase.insertRental(rental, customer);
    }

    public static ObservableList<Rental> getAllRentals() {
        return MockDatabase.getAllRentals();
    }

    public static void deleteRental(int id) {
        MockDatabase.deleteRental(id);
    }
}
