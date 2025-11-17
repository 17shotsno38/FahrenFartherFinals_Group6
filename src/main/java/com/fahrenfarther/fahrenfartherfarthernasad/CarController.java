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
    @FXML private TableColumn<Car, Void> colActions;

    private ObservableList<Car> carsList;

    public void initialize() {
        // Set up ComboBox items
        cmbStatus.setItems(FXCollections.observableArrayList(
                "Available", "Rented", "Maintenance"
        ));
        cmbStatus.setValue("Available");

        addDeleteButtonToTable();
        loadCarsFromDatabase();
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Car, Void>, TableCell<Car, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Car, Void> call(final TableColumn<Car, Void> param) {
                final TableCell<Car, Void> cell = new TableCell<>() {
                    private final Button btnDelete = new Button("Delete");

                    {
                        btnDelete.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 5 15; -fx-cursor: hand;");
                        btnDelete.setOnAction(event -> {
                            Car car = getTableView().getItems().get(getIndex());
                            handleDeleteCar(car);
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

        colActions.setCellFactory(cellFactory);
    }

    private void handleDeleteCar(Car car) {

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Car");
        confirmAlert.setContentText("Are you sure you want to delete: " + car.getModel() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                // DELETE without SQLException
                CarData.deleteCar(car.getLicensePlate());
                loadCarsFromDatabase();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Car Deleted");
                successAlert.setContentText("The car has been deleted successfully!");
                successAlert.showAndWait();
            }
        });
    }

    private void loadCarsFromDatabase() {
        carsList = CarData.getAllCars();
        tblCars.setItems(carsList);
    }

    @FXML
    private void handleAddCarClick() {
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

            // INSERT without SQLException
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
