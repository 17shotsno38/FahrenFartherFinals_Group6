package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Callback;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class RentalsController {

    @FXML private Button btnNewRental;
    @FXML private VBox newRentalForm;
    @FXML private ComboBox<String> cmbCustomer;
    @FXML private ComboBox<String> cmbCar;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;
    @FXML private TextField txtTotalCost;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private TableView<Rental> tblRentals;
    @FXML private TableColumn<Rental, String> colCar;
    @FXML private TableColumn<Rental, String> colStartDate;
    @FXML private TableColumn<Rental, String> colEndDate;
    @FXML private TableColumn<Rental, String> colTotalCost;
    @FXML private TableColumn<Rental, Void> colAction;

    private ObservableList<Rental> rentalsList;

    public void initialize() {
        loadCustomersFromDatabase();
        loadCarsFromDatabase();

        cmbStatus.setItems(FXCollections.observableArrayList(
                "Active", "Completed", "Cancelled"
        ));
        cmbStatus.setValue("Active");

        loadRentalsFromDatabase();
        addDeleteButtonToTable();

        dpStartDate.setOnAction(e -> calculateTotalCost());
        dpEndDate.setOnAction(e -> calculateTotalCost());
        cmbCar.setOnAction(e -> calculateTotalCost());
    }

    private void loadCustomersFromDatabase() {
        ObservableList<User> users = UserData.getAllUsers();
        ObservableList<String> customerNames = FXCollections.observableArrayList();

        for (User user : users) {
            customerNames.add(user.getName());
        }

        cmbCustomer.setItems(customerNames);
    }

    private void loadCarsFromDatabase() {
        ObservableList<Car> cars = CarData.getAllCars();
        ObservableList<String> carOptions = FXCollections.observableArrayList();

        for (Car car : cars) {
            String rateStr = car.getDailyRate().replace("₱", "").replace(",", "");
            carOptions.add(car.getModel() + " - ₱" + rateStr + "/day");
        }

        cmbCar.setItems(carOptions);
    }

    private void loadRentalsFromDatabase() {
        rentalsList = RentalData.getAllRentals();
        tblRentals.setItems(rentalsList);
    }

    private void calculateTotalCost() {
        if (dpStartDate.getValue() != null && dpEndDate.getValue() != null && cmbCar.getValue() != null) {
            LocalDate start = dpStartDate.getValue();
            LocalDate end = dpEndDate.getValue();
            long days = ChronoUnit.DAYS.between(start, end);

            if (days > 0) {
                String carSelection = cmbCar.getValue();
                String rateStr = carSelection.split("₱")[1].split("/")[0].replace(",", "");
                double dailyRate = Double.parseDouble(rateStr);
                double total = dailyRate * days;

                txtTotalCost.setText(String.format("₱%.0f", total));
            }
        }
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Rental, Void>, TableCell<Rental, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Rental, Void> call(final TableColumn<Rental, Void> param) {
                final TableCell<Rental, Void> cell = new TableCell<>() {
                    private final Button btnDelete = new Button("Delete");

                    {
                        btnDelete.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 5 15; -fx-cursor: hand;");
                        btnDelete.setOnAction(event -> {
                            Rental rental = getTableView().getItems().get(getIndex());
                            handleDeleteRental(rental);
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

        colAction.setCellFactory(cellFactory);
    }

    private void handleDeleteRental(Rental rental) {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Rental");
        confirmAlert.setContentText("Are you sure you want to delete this rental for " + rental.getCar() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Delete from database using rental ID
                    RentalData.deleteRental(rental.getId());

                    // Reload table from database
                    loadRentalsFromDatabase();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Rental Deleted");
                    successAlert.setContentText("The rental has been deleted successfully!");
                    successAlert.showAndWait();

                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Database Error");
                    errorAlert.setHeaderText("Failed to delete rental");
                    errorAlert.setContentText("Error: " + e.getMessage());
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleNewRentalClick() {
        newRentalForm.setVisible(true);
        newRentalForm.setManaged(true);
    }

    @FXML
    private void handleCreateRentalClick() {
        String customer = cmbCustomer.getValue();
        String car = cmbCar.getValue();
        LocalDate startDate = dpStartDate.getValue();
        LocalDate endDate = dpEndDate.getValue();
        String totalCost = txtTotalCost.getText();

        if (customer == null || car == null || startDate == null || endDate == null || totalCost.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Missing Information", "Please fill in all fields.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String carName = car.split(" - ")[0];

            Rental newRental = new Rental(
                    carName,
                    startDate.format(formatter),
                    endDate.format(formatter),
                    totalCost
            );

            RentalData.insertRental(newRental, customer);
            loadRentalsFromDatabase();

            clearForm();
            newRentalForm.setVisible(false);
            newRentalForm.setManaged(false);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Rental Created", "The rental has been saved to the database!");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to create rental", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelClick() {
        clearForm();
        newRentalForm.setVisible(false);
        newRentalForm.setManaged(false);
    }

    private void clearForm() {
        cmbCustomer.setValue(null);
        cmbCar.setValue(null);
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);
        txtTotalCost.clear();
        cmbStatus.setValue("Active");
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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
    public void handleUsersClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "UsersDashboard.fxml");
    }
}
