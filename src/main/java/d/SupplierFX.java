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

public class SupplierFX {
    public static ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
    public static TableView<Supplier> suppliersTable = new TableView<>(suppliersList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    public SupplierFX() {
    }

    public BorderPane createAddSupplierPane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));

        Label headerLabel = new Label("Add New Supplier");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        GridPane formGrid = createSupplierForm();
        VBox cardContainer = new VBox(header, formGrid);
        cardContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        cardContainer.setPadding(new Insets(30));
        cardContainer.setMaxWidth(600);
        mainPane.setCenter(cardContainer);

        return mainPane;
    }

    private GridPane createSupplierForm() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        String[] labelTexts = {"Supplier Name:", "Phone Number:"};
        Label[] labels = new Label[labelTexts.length];
        TextField[] textFields = new TextField[2]; // Name and Phone Number fields

        initializeFormComponents(labels, labelTexts, textFields);
        addComponentsToGrid(grid, labels, textFields);

        Button submitButton = createSubmitButton(textFields);
        grid.add(submitButton, 1, 2);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        return grid;
    }

    private void initializeFormComponents(Label[] labels, String[] labelTexts, TextField[] textFields) {
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelTexts[i]);
            labels[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }

        // Two text fields: supplier name and phone number
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new TextField();
            textFields[i].setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
            textFields[i].setPrefWidth(300);
        }
    }

    private void addComponentsToGrid(GridPane grid, Label[] labels, TextField[] textFields) {
        grid.add(labels[0], 0, 0);
        grid.add(textFields[0], 1, 0); // Supplier Name
        grid.add(labels[1], 0, 1);
        grid.add(textFields[1], 1, 1); // Phone Number
    }

    private Button createSubmitButton(TextField[] textFields) {
        Button submitButton = new Button("Add Supplier");
        submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        submitButton.setPrefWidth(200);
        submitButton.setOnMouseEntered(e ->
                submitButton.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnMouseExited(e ->
                submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnAction(e -> addSupplier(textFields));
        return submitButton;
    }

    public HBox createSupplierTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox sidebar = createSidebar();
        createSupplierTable();
        mainPane.getChildren().addAll(sidebar, suppliersTable);
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
        Button addSupplierButton = createAddSupplierButton();
        sidebar.getChildren().addAll(actionLabel, addSupplierButton);
        return sidebar;
    }

    private Button createAddSupplierButton() {
        ImageView addIcon = new ImageView(getClass().getResource("/addEmployee.png").toExternalForm());
        addIcon.setFitHeight(24);
        addIcon.setFitWidth(24);
        addIcon.setPreserveRatio(true);

        Button addButton = new Button("Add Supplier", addIcon);
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        addButton.setPrefWidth(180);
        addButton.setContentDisplay(ContentDisplay.LEFT);

        addButton.setOnMouseEntered(e ->
                addButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        addButton.setOnMouseExited(e ->
                addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        addButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(createAddSupplierPane(), 700, 450));
            stage.setResizable(false);
            stage.setTitle("Add New Supplier");
            stage.show();
        });

        return addButton;
    }

    private void createSupplierTable() {
        String[] columnNames = {"ID", "Supplier Name", "Phone Number"};
        String[] propertyNames = {"supID", "supplierName", "phoneNumber"};
        TableColumn<Supplier, ?>[] tc = new TableColumn[columnNames.length];
        double colWidth = Math.max(120, screenWidth * 0.22);
        double minColWidth = screenWidth * 0.16;
        double maxColWidth = screenWidth * 0.26;

        for (int i = 0; i < columnNames.length; i++) {
            tc[i] = new TableColumn<>(columnNames[i]);
            if (i == 0) {
                tc[i].setPrefWidth(120);
                tc[i].setMinWidth(100);
                tc[i].setMaxWidth(140);
            } else {
                tc[i].setMinWidth(minColWidth);
                tc[i].setPrefWidth(colWidth);
                tc[i].setMaxWidth(maxColWidth);
            }
            tc[i].setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
            tc[i].setStyle("-fx-alignment: CENTER; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        }

        TableColumn<Supplier, Void> deleteColumn = createDeleteColumn();
        deleteColumn.setMinWidth(250);
        deleteColumn.setPrefWidth(270);
        deleteColumn.setMaxWidth(300);

        suppliersTable.getColumns().clear();
        suppliersTable.getColumns().addAll(tc);
        suppliersTable.getColumns().add(deleteColumn);
        suppliersTable.setItems(suppliersList);
        suppliersTable.setEditable(true);
        suppliersTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        suppliersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        suppliersTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindTableDimensions();
            }
        });

        setupTableEditing(tc);
    }



    private void bindTableDimensions() {
        suppliersTable.prefWidthProperty().bind(suppliersTable.getScene().widthProperty().subtract(250));
        suppliersTable.prefHeightProperty().bind(suppliersTable.getScene().heightProperty().subtract(100));
    }

    private TableColumn<Supplier, Void> createDeleteColumn() {
        TableColumn<Supplier, Void> deleteColumn = new TableColumn<>("Actions");
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

                btn.setOnAction(event -> handleDeleteSupplier());
                return btn;
            }

            private void handleDeleteSupplier() {
                Supplier supplier = getTableView().getItems().get(getIndex());
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Delete Supplier");
                confirmAlert.setContentText("Are you sure you want to delete Supplier ID: " + supplier.getSupID() + "?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteSupplierFromDatabase(supplier);
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

    private void setupTableEditing(TableColumn<Supplier, ?>[] tc) {
        // Supplier Name column editing (index 1)
        ((TableColumn<Supplier, String>) tc[1]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Supplier, String>) tc[1]).setOnEditCommit(event -> {
            Supplier supplier = event.getRowValue();
            String newName = event.getNewValue().trim();
            if (!newName.isEmpty()) {
                supplier.setSupplierName(newName);
                updateSupplier(supplier);
            } else {
                revertNameEdit(event, event.getOldValue());
                showAlert("Invalid Name", "Supplier name cannot be empty.", Alert.AlertType.WARNING);
            }
        });

        // Phone Number column editing (index 2)
        ((TableColumn<Supplier, String>) tc[2]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Supplier, String>) tc[2]).setOnEditCommit(event -> {
            Supplier supplier = event.getRowValue();
            String newPhone = event.getNewValue().trim();
            if (!newPhone.isEmpty()) {
                supplier.setPhoneNumber(newPhone);
                updateSupplier(supplier);
            } else {
                revertPhoneEdit(event, event.getOldValue());
                showAlert("Invalid Phone Number", "Phone number cannot be empty.", Alert.AlertType.WARNING);
            }
        });
    }

    private void revertNameEdit(TableColumn.CellEditEvent<Supplier, String> event, String oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setSupplierName(oldValue);
        event.getTableView().refresh();
    }

    private void revertPhoneEdit(TableColumn.CellEditEvent<Supplier, String> event, String oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setPhoneNumber(oldValue);
        event.getTableView().refresh();
    }

    private void updateSupplier(Supplier supplier) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String updateSupplierSQL = "UPDATE supplier SET " +
                    "sName = '" + supplier.getSupplierName() + "', " +
                    "sPhoneNumber = '" + supplier.getPhoneNumber() + "' " +
                    "WHERE sID = " + supplier.getSupID();

            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSupplierSQL);

            showAlert("Success", "Supplier updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update supplier: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteSupplierFromDatabase(Supplier supplier) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE supplier SET isActive = false WHERE sID = " + supplier.getSupID());

            MainApplication.readDB();
            showAlert("Success", "Supplier ID " + supplier.getSupID() + " deactivated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to deactivate supplier: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addSupplier(TextField[] textFields) {
        if (validateFormFields(textFields)) {
            try {
                String supplierName = textFields[0].getText().trim();
                String phoneNumber = textFields[1].getText().trim();

                addSupplier(supplierName, phoneNumber);

                clearForm(textFields);
                showAlert("Success", "Supplier added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add supplier: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validateFormFields(TextField[] textFields) {
        if (textFields[0].getText().trim().isEmpty() || textFields[1].getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.", Alert.AlertType.WARNING);
            return false;
        }

        String phoneNumber = textFields[1].getText().trim();
        if (!phoneNumber.matches("\\d+")) {
            showAlert("Validation Error", "Phone number must contain only digits.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void addSupplier(String supplierName, String phoneNumber) throws SQLException, ClassNotFoundException {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            String insertSupplierSQL = "INSERT INTO supplier (sName, sPhoneNumber) " +
                    "VALUES ('" + supplierName + "', '" + phoneNumber + "')";
            stmt.executeUpdate(insertSupplierSQL);

            MainApplication.readDB();
        }
    }

    private void clearForm(TextField[] textFields) {
        textFields[0].clear();
        textFields[1].clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane addPane() {
        return createAddSupplierPane();
    }

    public HBox supplierTablePane() {
        return createSupplierTablePane();
    }
}