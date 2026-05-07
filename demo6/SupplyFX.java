package d.demo6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Screen;

import java.sql.*;
import java.time.LocalDate;

public class SupplyFX {
    public static ObservableList<Supply> supplyList = FXCollections.observableArrayList();
    public static TableView<Supply> supplyTable = new TableView<>(supplyList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    public SupplyFX() {
    }

    public BorderPane createAddSupplyPane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));

        Label headerLabel = new Label("Add New Supply");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        GridPane formGrid = createSupplyForm();
        VBox cardContainer = new VBox(header, formGrid);
        cardContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        cardContainer.setPadding(new Insets(30));
        cardContainer.setMaxWidth(600);
        mainPane.setCenter(cardContainer);

        return mainPane;
    }

    private GridPane createSupplyForm() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        String[] labelTexts = {"Date:", "Quantity:", "Unit Cost:", "Product:", "Supplier:"};
        Label[] labels = new Label[labelTexts.length];
        TextField[] textFields = new TextField[2];
        DatePicker datePicker = new DatePicker();
        ComboBox<String> productComboBox = new ComboBox<>();
        ComboBox<String> supplierComboBox = new ComboBox<>();

        initializeFormComponents(labels, labelTexts, textFields, datePicker, productComboBox, supplierComboBox);
        addComponentsToGrid(grid, labels, textFields, datePicker, productComboBox, supplierComboBox);

        Button submitButton = createSubmitButton(textFields, datePicker, productComboBox, supplierComboBox);
        grid.add(submitButton, 1, 5);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        return grid;
    }

    private void initializeFormComponents(Label[] labels, String[] labelTexts, TextField[] textFields,
                                          DatePicker datePicker, ComboBox<String> productComboBox, ComboBox<String> supplierComboBox) {
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelTexts[i]);
            labels[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new TextField();
            textFields[i].setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
            textFields[i].setPrefWidth(300);
        }
        datePicker.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        datePicker.setPrefWidth(300);
        datePicker.setEditable(false);
        loadProducts(productComboBox);
        productComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        productComboBox.setPrefWidth(300);
        loadSuppliers(supplierComboBox);
        supplierComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        supplierComboBox.setPrefWidth(300);
    }

    private void loadProducts(ComboBox<String> productComboBox) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT prodID, prodName FROM Product WHERE isActive = TRUE;");
            while (rs.next()) {
                productComboBox.getItems().add(rs.getInt("prodID") + " - " + rs.getString("prodName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSuppliers(ComboBox<String> supplierComboBox) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT sID, sName FROM Supplier WHERE isActive = TRUE;");
            while (rs.next()) {
                supplierComboBox.getItems().add(rs.getInt("sID") + " - " + rs.getString("sName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addComponentsToGrid(GridPane grid, Label[] labels, TextField[] textFields,
                                     DatePicker datePicker, ComboBox<String> productComboBox, ComboBox<String> supplierComboBox) {
        grid.add(labels[0], 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(labels[1], 0, 1);
        grid.add(textFields[0], 1, 1);
        grid.add(labels[2], 0, 2);
        grid.add(textFields[1], 1, 2);
        grid.add(labels[3], 0, 3);
        grid.add(productComboBox, 1, 3);
        grid.add(labels[4], 0, 4);
        grid.add(supplierComboBox, 1, 4);
    }

    private Button createSubmitButton(TextField[] textFields, DatePicker datePicker,
                                      ComboBox<String> productComboBox, ComboBox<String> supplierComboBox) {
        Button submitButton = new Button("Add Supply");
        submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        submitButton.setPrefWidth(200);
        submitButton.setOnMouseEntered(e ->
                submitButton.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnMouseExited(e ->
                submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnAction(e -> addSupply(textFields, datePicker, productComboBox, supplierComboBox));
        return submitButton;
    }

    public HBox createSupplyTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox sidebar = createSidebar();
        createSupplyTable();
        mainPane.getChildren().addAll(sidebar, supplyTable);
        mainPane.setAlignment(Pos.CENTER);
        return mainPane;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");
        sidebar.setPrefWidth(500);
        sidebar.setPadding(new Insets(30));
        Label actionLabel = new Label("Actions");
        actionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Button addSupplyButton = createAddSupplyButton();
        sidebar.getChildren().addAll(actionLabel, addSupplyButton);
        return sidebar;
    }

    private Button createAddSupplyButton() {
        ImageView addIcon = new ImageView(getClass().getResource("/addEmployee.png").toExternalForm());
        addIcon.setFitHeight(24);
        addIcon.setFitWidth(24);
        addIcon.setPreserveRatio(true);

        Button addButton = new Button("Add Supply", addIcon);
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        addButton.setPrefWidth(180);
        addButton.setContentDisplay(ContentDisplay.LEFT);

        addButton.setOnMouseEntered(e ->
                addButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        addButton.setOnMouseExited(e ->
                addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        addButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(createAddSupplyPane(), 700, 550));
            stage.setResizable(false);
            stage.setTitle("Add New Supply");
            stage.show();
        });

        return addButton;
    }

    private void createSupplyTable() {
        String[] columnNames = {"ID", "Date", "Quantity", "Unit Cost", "Product", "Supplier"};
        String[] propertyNames = {"supID", "supDate", "quantity", "unitCost", "productName", "supplierName"};
        TableColumn<Supply, ?>[] tc = new TableColumn[columnNames.length];
        double colWidth = Math.max(80, screenWidth * 0.08);
        double minColWidth = screenWidth * 0.06;
        double maxColWidth = screenWidth * 0.12;

        for (int i = 0; i < columnNames.length; i++) {
            tc[i] = new TableColumn<>(columnNames[i]);
            if (i == 0) {
                tc[i].setPrefWidth(80);
                tc[i].setMinWidth(60);
                tc[i].setMaxWidth(100);
            } else {
                tc[i].setMinWidth(minColWidth);
                tc[i].setPrefWidth(colWidth);
                tc[i].setMaxWidth(maxColWidth);
            }
            tc[i].setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
            tc[i].setStyle("-fx-alignment: CENTER; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        }

        TableColumn<Supply, Void> deleteColumn = createDeleteColumn();
        deleteColumn.setMinWidth(200);
        deleteColumn.setPrefWidth(230);
        deleteColumn.setMaxWidth(250);

        supplyTable.getColumns().clear();
        supplyTable.getColumns().addAll(tc);
        supplyTable.getColumns().add(deleteColumn);
        supplyTable.setItems(supplyList);
        supplyTable.setEditable(true);
        supplyTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        supplyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        supplyTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindTableDimensions();
            }
        });

        setupTableEditing(tc);
    }

    private void bindTableDimensions() {
        supplyTable.prefWidthProperty().bind(supplyTable.getScene().widthProperty().subtract(250));
        supplyTable.prefHeightProperty().bind(supplyTable.getScene().heightProperty().subtract(100));
    }

    private TableColumn<Supply, Void> createDeleteColumn() {
        TableColumn<Supply, Void> deleteColumn = new TableColumn<>("Actions");
        deleteColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createDeleteButton();

            private Button createDeleteButton() {
                ImageView deleteIcon = new ImageView(getClass().getResource("/rmEmployee.png").toExternalForm());
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteIcon.setPreserveRatio(true);

                Button btn = new Button("Delete", deleteIcon);
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
                btn.setPrefWidth(120);
                btn.setContentDisplay(ContentDisplay.LEFT);

                btn.setOnMouseEntered(e ->
                        btn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

                btn.setOnMouseExited(e ->
                        btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

                btn.setOnAction(event -> handleDeleteSupply());
                return btn;
            }

            private void handleDeleteSupply() {
                Supply supply = getTableView().getItems().get(getIndex());
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Delete Supply");
                confirmAlert.setContentText("Are you sure you want to delete Supply ID: " + supply.getSupID() + "?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteSupplyFromDatabase(supply);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        return deleteColumn;
    }

    private void setupTableEditing(TableColumn<Supply, ?>[] tc) {
        // Quantity column editing (index 2)
        ((TableColumn<Supply, Integer>) tc[2]).setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        ((TableColumn<Supply, Integer>) tc[2]).setOnEditCommit(event -> {
            Supply supply = event.getRowValue();
            if (event.getNewValue() > 0) {
                supply.setQuantity(event.getNewValue());
                updateSupply(supply);
            } else {
                revertQuantityEdit(event, event.getOldValue());
                showAlert("Invalid Quantity", "Quantity must be greater than 0.", Alert.AlertType.WARNING);
            }
        });

        // Unit Cost column editing (index 3)
        ((TableColumn<Supply, Double>) tc[3]).setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        ((TableColumn<Supply, Double>) tc[3]).setOnEditCommit(event -> {
            Supply supply = event.getRowValue();
            if (event.getNewValue() > 0) {
                supply.setUnitCost(event.getNewValue());
                updateSupply(supply);
            } else {
                revertCostEdit(event, event.getOldValue());
                showAlert("Invalid Cost", "Unit cost must be greater than 0.", Alert.AlertType.WARNING);
            }
        });
    }

    private void revertQuantityEdit(TableColumn.CellEditEvent<Supply, Integer> event, Integer oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(oldValue);
        event.getTableView().refresh();
    }

    private void revertCostEdit(TableColumn.CellEditEvent<Supply, Double> event, Double oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setUnitCost(oldValue);
        event.getTableView().refresh();
    }

    private void updateSupply(Supply supply) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String updateSupplySQL = "UPDATE supply SET " +
                    "quantity = " + supply.getQuantity() + ", " +
                    "supDate = '" + Date.valueOf(supply.getSupDate()) + "', " +
                    "unitCost = " + supply.getUnitCost() + " " +
                    "WHERE supID = " + supply.getSupID();

            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSupplySQL);

            showAlert("Success", "Supply updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update supply: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteSupplyFromDatabase(Supply supply) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM supply WHERE supID = " + supply.getSupID());

            MainApplication.readDB();
            showAlert("Success", "Supply ID " + supply.getSupID() + " deleted successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to delete supply: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addSupply(TextField[] textFields, DatePicker datePicker,
                           ComboBox<String> productComboBox, ComboBox<String> supplierComboBox) {

        if (validateFormFields(textFields, datePicker, productComboBox, supplierComboBox)) {
            try {
                LocalDate date = datePicker.getValue();
                int quantity = Integer.parseInt(textFields[0].getText().trim());
                double unitCost = Double.parseDouble(textFields[1].getText().trim());
                int productId = extractIdFromComboBox(productComboBox.getValue());
                int supplierId = extractIdFromComboBox(supplierComboBox.getValue());

                addSupply(date, quantity, unitCost, productId, supplierId);

                clearForm(textFields, datePicker, productComboBox, supplierComboBox);
                showAlert("Success", "Supply added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add supply: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private int extractIdFromComboBox(String comboBoxValue) {
        return Integer.parseInt(comboBoxValue.split(" - ")[0]);
    }

    private boolean validateFormFields(TextField[] textFields, DatePicker datePicker,
                                       ComboBox<String> productComboBox, ComboBox<String> supplierComboBox) {

        if (textFields[0].getText().trim().isEmpty() || textFields[1].getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.", Alert.AlertType.WARNING);
            return false;
        }

        if (datePicker.getValue() == null || productComboBox.getValue() == null || supplierComboBox.getValue() == null) {
            showAlert("Validation Error", "Please fill in all required fields.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            int quantity = Integer.parseInt(textFields[0].getText());
            if (quantity <= 0) {
                showAlert("Validation Error", "Quantity must be a positive number.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid quantity.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            double unitCost = Double.parseDouble(textFields[1].getText());
            if (unitCost <= 0) {
                showAlert("Validation Error", "Unit cost must be a positive number.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid unit cost.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void addSupply(LocalDate date, int quantity, double unitCost, int productId, int supplierId) throws SQLException, ClassNotFoundException {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            String insertSupplySQL = "INSERT INTO supply (quantity, supDate, unitCost, prodID, supplierID) " +
                    "VALUES (" + quantity + ", '" + Date.valueOf(date) + "', " + unitCost + ", " + productId + ", " + supplierId + ")";
            stmt.executeUpdate(insertSupplySQL);

            MainApplication.readDB();
        }
    }

    private void clearForm(TextField[] textFields, DatePicker datePicker,
                           ComboBox<String> productComboBox, ComboBox<String> supplierComboBox) {
        textFields[0].clear();
        textFields[1].clear();
        datePicker.setValue(null);
        productComboBox.getSelectionModel().clearSelection();
        supplierComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane addPane() {
        return createAddSupplyPane();
    }

    public HBox supplyTablePane() {
        return createSupplyTablePane();
    }
}