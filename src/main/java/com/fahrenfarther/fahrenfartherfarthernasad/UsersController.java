package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UsersController {

    @FXML private Button btnAddUser;
    @FXML private VBox addUserForm;
    @FXML private TextField txtName;
    @FXML private DatePicker dpDateOfBirth;
    @FXML private TextField txtContactNo;
    @FXML private TextField txtLicenseNumber;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> dateOfBirthColumn;
    @FXML private TableColumn<User, String> contactNoColumn;
    @FXML private TableColumn<User, String> licenseNoColumn;

    private ObservableList<User> usersList;

    public void initialize() {
        // Load sample user data
        usersList = FXCollections.observableArrayList(
                new User("Juan Dela Cruz", "1990-05-15", "09171234567", "N01-12-345678"),
                new User("Maria Santos", "1985-08-22", "09181234567", "N02-13-456789"),
                new User("Pedro Reyes", "1992-11-30", "09191234567", "N03-14-567890")
        );

        usersTable.setItems(usersList);
    }

    @FXML
    private void handleAddUserClick() {
        System.out.println("Add User button clicked!"); // Debug
        addUserForm.setVisible(true);
        addUserForm.setManaged(true);
    }

    @FXML
    private void handleSaveUserClick() {
        String name = txtName.getText().trim();
        LocalDate dobDate = dpDateOfBirth.getValue();
        String contactNo = txtContactNo.getText().trim();
        String licenseNo = txtLicenseNumber.getText().trim();

        if (name.isEmpty() || dobDate == null || contactNo.isEmpty() || licenseNo.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateOfBirth = dobDate.format(formatter);

        User newUser = new User(name, dateOfBirth, contactNo, licenseNo);
        usersList.add(newUser);

        clearForm();
        addUserForm.setVisible(false);
        addUserForm.setManaged(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("User Added");
        alert.setContentText("The user has been added successfully!");
        alert.showAndWait();
    }

    @FXML
    private void handleCancelClick() {
        clearForm();
        addUserForm.setVisible(false);
        addUserForm.setManaged(false);
    }

    private void clearForm() {
        txtName.clear();
        dpDateOfBirth.setValue(null);
        txtContactNo.clear();
        txtLicenseNumber.clear();
    }

    @FXML
    public void handleDashboardClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "Dashboard.fxml");
    }

    @FXML
    public void handleCarsClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "CarDashboard.fxml");
    }

    @FXML
    public void handleRentalsClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "RentalsDashboard.fxml");
    }
}