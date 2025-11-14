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

public class CarController {

    @FXML private Button btnAddCar;
    @FXML private VBox addCarForm;
    @FXML private TextField txtBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtLicensePlate;
    @FXML private TextField txtDailyRate;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private TableView<Car> tblCars;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, Integer> colYear;
    @FXML private TableColumn<Car, String> colLicensePlate;
    @FXML private TableColumn<Car, String> colDailyRate;

    private ObservableList<Car> carsList;

    public void initialize() {
        // Set up ComboBox items
        cmbStatus.setItems(FXCollections.observableArrayList(
                "Available", "Rented", "Maintenance"
        ));
        cmbStatus.setValue("Available");

        // Load cars from database
        loadCarsFromDatabase();
    }

    private void loadCarsFromDatabase() {
        carsList = CarData.getAllCars();
        tblCars.setItems(carsList);
    }

    @FXML
    private void handleAddCarClick() {
        // Show the form
        addCarForm.setVisible(true);
        addCarForm.setManaged(true);
    }

    @FXML
    private void handleSaveCarClick() {
        String brand = txtBrand.getText().trim();
        String model = txtModel.getText().trim();
        String yearStr = txtYear.getText().trim();
        String licensePlate = txtLicensePlate.getText().trim();
        String dailyRate = txtDailyRate.getText().trim();

        if (brand.isEmpty() || model.isEmpty() || yearStr.isEmpty() ||
                licensePlate.isEmpty() || dailyRate.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            String fullModel = brand + " " + model;
            String formattedRate = "₱" + dailyRate;

            Car newCar = new Car(fullModel, year, licensePlate, formattedRate);


            CarData.insertCar(newCar);


            loadCarsFromDatabase();

            clearForm();
            addCarForm.setVisible(false);
            addCarForm.setManaged(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Car Added");
            alert.setContentText("The car has been saved to the database!");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Year");
            alert.setContentText("Please enter a valid year.");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to save car");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelClick() {
        clearForm();
        addCarForm.setVisible(false);
        addCarForm.setManaged(false);
    }

    private void clearForm() {
        txtBrand.clear();
        txtModel.clear();
        txtYear.clear();
        txtLicensePlate.clear();
        txtDailyRate.clear();
        cmbStatus.setValue("Available");
    }

    @FXML
    public void handleDashboardClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "Dashboard.fxml");
    }

    @FXML
    public void handleUsersClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "UsersDashboard.fxml");
    }

    @FXML
    public void handleRentalsClick(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneManager.switchScene(stage, "RentalsDashboard.fxml");
    }
}