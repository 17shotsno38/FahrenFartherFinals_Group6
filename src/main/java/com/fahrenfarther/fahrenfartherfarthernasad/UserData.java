package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.ObservableList;

public class UserData {

    public static void insertUser(User user) {
        MockDatabase.insertUser(user);
    }

    public static ObservableList<User> getAllUsers() {
        return MockDatabase.getAllUsers();
    }

    public static void deleteUser(String licenseNo) {
        MockDatabase.deleteUser(licenseNo);
    }
}
