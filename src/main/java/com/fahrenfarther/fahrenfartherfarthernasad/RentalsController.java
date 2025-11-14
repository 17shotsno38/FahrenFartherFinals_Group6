package com.fahrenfarther.fahrenfartherfarthernasad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
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
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5; -fx-padding: 5 10;");
                deleteButton.setOnAction(event -> {
                    Rental rental = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Delete");
                    alert.setHeaderText("Delete Rental");
                    alert.setContentText("Are you sure you want to delete this rental for " + rental.getCar() + "?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            rentalsList.remove(rental);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
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