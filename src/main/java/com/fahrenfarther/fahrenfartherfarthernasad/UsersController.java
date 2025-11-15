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

import java.sql.SQLException;
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
        // Add delete button to Actions column
        addDeleteButtonToTable();

        // Load users from database
        loadUsersFromDatabase();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<>() {
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
                return cell;
            }
        };

        actionsColumn.setCellFactory(cellFactory);
    }

    private void handleDeleteUser(User user) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete User");
        confirmAlert.setContentText("Are you sure you want to delete: " + user.getName() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    UserData.deleteUser(user.getLicenseNo());
                    loadUsersFromDatabase();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("User Deleted");
                    successAlert.setContentText("The user has been deleted successfully!");
                    successAlert.showAndWait();

                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Database Error");
                    errorAlert.setHeaderText("Failed to delete user");
                    errorAlert.setContentText("Error: " + e.getMessage());
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadUsersFromDatabase() {
        try {
            usersList = UserData.getAllUsers();
            usersTable.setItems(usersList);
        } catch (Exception e) {
            e.printStackTrace();
            usersList = FXCollections.observableArrayList();
            usersTable.setItems(usersList);
        }
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateOfBirth = dobDate.format(formatter);
        User newUser = new User(name, dateOfBirth, contactNo, licenseNo);

        try {
            UserData.insertUser(newUser);
            loadUsersFromDatabase();

            clearForm();
            addUserForm.setVisible(false);
            addUserForm.setManaged(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("User Added");
            alert.setContentText("User saved to database!");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
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
