package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.ObservableList;

public class CarData {

    public static void insertCar(Car car) {
        MockDatabase.insertCar(car);
    }

    public static ObservableList<Car> getAllCars() {
        return MockDatabase.getAllCars();
    }

    public static void deleteCar(String licensePlate) {
        MockDatabase.deleteCar(licensePlate);
    }
}
