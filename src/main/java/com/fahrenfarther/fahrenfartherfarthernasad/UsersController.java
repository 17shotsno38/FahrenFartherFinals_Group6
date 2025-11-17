package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Callback;

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
    @FXML private TableColumn<User, Void> actionsColumn;

    private ObservableList<User> usersList;

    public void initialize() {
        // ===== Setup Table Columns =====
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        contactNoColumn.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        licenseNoColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNo"));

        // Add Delete button to Actions column
        addDeleteButtonToTable();

        // Load users from mock database
        loadUsersFromDatabase();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btnDelete = new Button("Delete");

            {
                btnDelete.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 5 15; -fx-cursor: hand;");
                btnDelete.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        };

        actionsColumn.setCellFactory(cellFactory);
    }

    private void handleDeleteUser(User user) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete User");
        confirmAlert.setContentText("Are you sure you want to delete: " + user.getName() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MockDatabase.deleteUser(user.getLicenseNo());
                loadUsersFromDatabase();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("User Deleted");
                successAlert.setContentText("The user has been deleted successfully!");
                successAlert.showAndWait();
            }
        });
    }

    private void loadUsersFromDatabase() {
        usersList = MockDatabase.getAllUsers();
        usersTable.setItems(usersList);
    }

    @FXML
    private void handleAddUserClick() {
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

        String dateOfBirth = dobDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        User newUser = new User(name, dateOfBirth, contactNo, licenseNo);

        MockDatabase.insertUser(newUser);
        loadUsersFromDatabase();

        clearForm();
        addUserForm.setVisible(false);
        addUserForm.setManaged(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("User Added");
        alert.setContentText("User saved successfully!");
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

    // ===== Navigation Buttons =====
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
