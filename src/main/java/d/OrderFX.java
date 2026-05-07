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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class OrderFX {
    public static ObservableList<Orders> ordersList = FXCollections.observableArrayList();
    public static TableView<Orders> ordersTable = new TableView<>(ordersList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    public BorderPane createAddOrderPane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));
        Label headerLabel = new Label("Add New Order");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));
        GridPane formGrid = createOrderForm();
        VBox cardContainer = new VBox(header, formGrid);
        cardContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        cardContainer.setPadding(new Insets(30));
        cardContainer.setMaxWidth(600);
        mainPane.setCenter(cardContainer);
        return mainPane;
    }

    private GridPane createOrderForm() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        String[] labelTexts = {"Order Date:", "Status:", "Customer Name:", "Delivery Address:"};
        Label[] labels = new Label[labelTexts.length];
        DatePicker datePicker = new DatePicker();
        ComboBox<CustomerComboBoxItem> customerComboBox = new ComboBox<>();
        TextField deliveryAddressField = new TextField();
        initializeFormComponents(labels, labelTexts, datePicker, customerComboBox, deliveryAddressField);
        addComponentsToGrid(grid, labels, datePicker, customerComboBox, deliveryAddressField);
        Button submitButton = createSubmitButton(datePicker, customerComboBox, deliveryAddressField);
        grid.add(submitButton, 1, 4);
        GridPane.setHalignment(submitButton, HPos.RIGHT);
        return grid;
    }

    private void initializeFormComponents(Label[] labels, String[] labelTexts, DatePicker datePicker, ComboBox<CustomerComboBoxItem> customerComboBox, TextField deliveryAddressField) {
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelTexts[i]);
            labels[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }
        datePicker.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        datePicker.setPrefWidth(300);
        datePicker.setEditable(false);
        customerComboBox.setPromptText("Select Customer");
        customerComboBox.setPrefWidth(300);
        customerComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        try {
            customerComboBox.setItems(loadActiveCustomers());
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Database Error", "Failed to load customers: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        deliveryAddressField.setPrefWidth(300);
        deliveryAddressField.setPromptText("Delivery Address");
        deliveryAddressField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1;");
    }

    private void addComponentsToGrid(GridPane grid, Label[] labels, DatePicker datePicker, ComboBox<CustomerComboBoxItem> customerComboBox, TextField deliveryAddressField) {
        grid.add(labels[0], 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(labels[2], 0, 1);
        grid.add(customerComboBox, 1, 1);
        grid.add(labels[3], 0, 3);
        grid.add(deliveryAddressField, 1, 3);
    }

    private Button createSubmitButton(DatePicker datePicker, ComboBox<CustomerComboBoxItem> customerComboBox, TextField deliveryAddressField) {
        Button submitButton = new Button("Add Order");
        submitButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;");
        submitButton.setPrefWidth(200);
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        submitButton.setOnAction(e -> addOrder(datePicker, customerComboBox, deliveryAddressField));
        return submitButton;
    }

    public HBox createOrderTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox sidebar = createSidebar();
        createOrderTable();
        mainPane.getChildren().addAll(sidebar, ordersTable);
        mainPane.setAlignment(Pos.CENTER);
        return mainPane;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");
        sidebar.setPrefWidth(550);
        sidebar.setPadding(new Insets(30));
        Label actionLabel = new Label("Actions");
        actionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField searchField = createSearchField();
        Button addOrderButton = createAddOrderButton();
        sidebar.getChildren().addAll(actionLabel, searchField, addOrderButton);
        return sidebar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search orders... (Press Enter)");
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        searchField.setPrefWidth(230);
        searchField.setOnAction(e -> performSearch(searchField.getText()));
        return searchField;
    }

    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            ordersTable.setItems(ordersList);
            ordersTable.refresh();
        } else {
            ObservableList<Orders> filteredList = FXCollections.observableArrayList();
            String lowerSearchText = searchText.toLowerCase().trim();
            for (Orders orders : ordersList) {
                if (String.valueOf(orders.getoID()).toLowerCase().contains(lowerSearchText) ||
                        orders.getCustomerName().toLowerCase().contains(lowerSearchText) ||
                        orders.getDeliveryLocation().toLowerCase().contains(lowerSearchText)) {
                    filteredList.add(orders);
                }
            }
            ordersTable.setItems(filteredList);
            ordersTable.refresh();
        }
    }

    private Button createAddOrderButton() {
        ImageView addIcon = new ImageView("addOrder.png");
        addIcon.setFitHeight(24);
        addIcon.setFitWidth(24);
        addIcon.setPreserveRatio(true);
        Button addButton = new Button("Add Order", addIcon);
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;");
        addButton.setPrefWidth(230);
        addButton.setContentDisplay(ContentDisplay.LEFT);
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        addButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(createAddOrderPane(), 700, 500));
            stage.setResizable(false);
            stage.setTitle("Add New Order");
            stage.show();
        });
        return addButton;
    }

    private void createOrderTable() {
        String[] columnNames = {"Order ID", "Order Date", "Status", "Customer Name", "Delivery Address"};
        String[] propertyNames = {"oID", "oDate", "status", "customerName", "deliveryLocation"};
        TableColumn<Orders, ?>[] tc = new TableColumn[columnNames.length];
        double minColWidth = screenWidth * 0.12;
        double colWidth = screenWidth * 0.18;
        double maxColWidth = screenWidth * 0.25;
        for (int i = 0; i < columnNames.length; i++) {
            tc[i] = new TableColumn<>(columnNames[i]);
            if (i == 0) {
                tc[i].setPrefWidth(120);
                tc[i].setMinWidth(100);
                tc[i].setMaxWidth(180);
            } else {
                tc[i].setMinWidth(minColWidth);
                tc[i].setPrefWidth(colWidth);
                tc[i].setMaxWidth(maxColWidth);
            }
            tc[i].setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
            tc[i].setStyle("-fx-alignment: CENTER; -fx-font-size: 14px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        }
        ((TableColumn<Orders, Boolean>) tc[2]).setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item) {
                        setText("Completed");
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setText("Pending");
                        setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                    }
                }
            }
        });
        TableColumn<Orders, Void> deleteColumn = createDeleteColumn();
        deleteColumn.setMinWidth(180);
        deleteColumn.setPrefWidth(200);
        deleteColumn.setMaxWidth(250);
        ordersTable.getColumns().clear();
        ordersTable.getColumns().addAll(tc);
        ordersTable.getColumns().add(deleteColumn);
        ordersTable.setItems(ordersList);
        ordersTable.setEditable(true);
        ordersTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        ordersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ordersTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindTableDimensions();
            }
        });
        setupTableEditing(tc);
    }

    private void bindTableDimensions() {
        ordersTable.prefWidthProperty().bind(ordersTable.getScene().widthProperty().subtract(280));
        ordersTable.prefHeightProperty().bind(ordersTable.getScene().heightProperty().subtract(100));
    }

    private TableColumn<Orders, Void> createDeleteColumn() {
        TableColumn<Orders, Void> deleteColumn = new TableColumn<>("Actions");
        deleteColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createDeleteButton();

            private Button createDeleteButton() {
                ImageView deleteIcon = new ImageView("rmOrder.png");
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteIcon.setPreserveRatio(true);
                Button btn = new Button("Delete", deleteIcon);
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;");
                btn.setPrefWidth(150);
                btn.setContentDisplay(ContentDisplay.LEFT);
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;"));
                btn.setOnAction(event -> handleDeleteOrder());
                return btn;
            }

            private void handleDeleteOrder() {
                Orders orders = getTableView().getItems().get(getIndex());
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Delete Order");
                confirmAlert.setContentText("Are you sure you want to delete order #" + orders.getoID() + "?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteOrderFromDatabase(orders);
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

    private void deleteOrderFromDatabase(Orders orders) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE orders SET isActive = false WHERE oID = " + orders.getoID());
            MainApplication.readDB();
            showAlert("Success", "Order #" + orders.getoID() + " marked as inactive successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to mark order inactive: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupTableEditing(TableColumn<Orders, ?>[] tc) {
        StringConverter<LocalDate> localDateConverter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return date.toString();
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string);
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        };
        ((TableColumn<Orders, LocalDate>) tc[1]).setCellFactory(TextFieldTableCell.forTableColumn(localDateConverter));
        ((TableColumn<Orders, LocalDate>) tc[1]).setOnEditCommit(event -> {
            Orders orders = event.getRowValue();
            LocalDate newDate = event.getNewValue();
            if (newDate != null) {
                orders.setoDate(newDate);
                updateOrder(orders);
            } else {
                orders.setoDate(event.getOldValue());
                showAlert("Invalid Date", "Please enter a valid date (YYYY-MM-DD)", Alert.AlertType.WARNING);
            }
            event.getTableView().refresh();
        });

        // Create ComboBox items for status
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Pending", "Completed");

        // Set up ComboBox cell factory for status column
        ((TableColumn<Orders, Boolean>) tc[2]).setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<Boolean>() {
                    @Override
                    public String toString(Boolean status) {
                        return status != null && status ? "Completed" : "Pending";
                    }

                    @Override
                    public Boolean fromString(String string) {
                        return "Completed".equals(string);
                    }
                },
                statusOptions.stream().map(s -> "Completed".equals(s)).toArray(Boolean[]::new)
        ));

        // Handle status edit commit
        ((TableColumn<Orders, Boolean>) tc[2]).setOnEditCommit(event -> {
            Orders orders = event.getRowValue();
            Boolean newStatus = event.getNewValue();
            orders.setStatus(newStatus);
            updateOrder(orders);
            event.getTableView().refresh();
        });
    }

    private void updateOrder(Orders orders) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String SQL = "UPDATE Orders SET " +
                    "oDate = '" + orders.getoDate() + "', " +
                    "status = " + orders.isStatus() + ", " +
                    "cID = " + orders.getcID() + ", " +
                    "dID = " + orders.getdID() + " " +
                    "WHERE oID = " + orders.getoID();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
            showAlert("Success", "Order updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update order: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addOrder(DatePicker datePicker, ComboBox<CustomerComboBoxItem> customerComboBox, TextField deliveryAddressField) {
        LocalDate orderDate = datePicker.getValue();
        CustomerComboBoxItem selectedCustomer = customerComboBox.getSelectionModel().getSelectedItem();
        String deliveryAddress = deliveryAddressField.getText();
        if (orderDate == null || selectedCustomer == null || deliveryAddress.isEmpty()) {
            showAlert("Validation Error", "Please fill all required fields", Alert.AlertType.WARNING);
            return;
        }
        int cID = selectedCustomer.getCID();
        boolean status = false;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement()) {
            int dID = -1;
            ResultSet rsDel = stmt.executeQuery(
                    "SELECT dID FROM Delivery WHERE Address = '" + deliveryAddress + "' AND isActive = TRUE"
            );
            if (rsDel.next()) {
                dID = rsDel.getInt("dID");
            } else {
                LocalDate deliveryDate = orderDate.plusDays(3);
                String insertSQL = "INSERT INTO Delivery (dDate, dStatus, Address) VALUES ('" +
                        deliveryDate + "', 0, '" + deliveryAddress + "')";
                stmt.executeUpdate(insertSQL);
                ResultSet rsMax = stmt.executeQuery("SELECT MAX(dID) AS dID FROM Delivery");
                if (rsMax.next()) dID = rsMax.getInt("dID");
                rsMax.close();
            }
            rsDel.close();
            int statusValue = status ? 1 : 0;
            String sqlOrder = "INSERT INTO Orders (oDate, status, cID, dID, isActive) VALUES ('" +
                    orderDate + "', " + statusValue + ", " + cID + ", " + dID + ", 1)";
            stmt.executeUpdate(sqlOrder);
            MainApplication.readDB();
            datePicker.setValue(null);
            customerComboBox.getSelectionModel().clearSelection();
            deliveryAddressField.clear();
            showAlert("Success", "Order added successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException | ClassNotFoundException e1) {
            e1.printStackTrace();
            showAlert("Error", "Database error: " + e1.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public HBox orderTablePane() {
        return createOrderTablePane();
    }

    private static class CustomerComboBoxItem {
        private final int cID;
        private final String firstName;
        private final String lastName;

        public CustomerComboBoxItem(int cID, String firstName, String lastName) {
            this.cID = cID;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public int getCID() {
            return cID;
        }

        @Override
        public String toString() {
            return firstName + " " + lastName;
        }
    }

    private ObservableList<CustomerComboBoxItem> loadActiveCustomers() throws SQLException, ClassNotFoundException {
        ObservableList<CustomerComboBoxItem> customers = FXCollections.observableArrayList();
        String query = "SELECT p.pID, p.pFirstName, p.pLastName FROM Person p " +
                "JOIN Customer c ON p.pID = c.cID WHERE c.isActive = TRUE";
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int cID = rs.getInt("pID");
                String firstName = rs.getString("pFirstName");
                String lastName = rs.getString("pLastName");
                customers.add(new CustomerComboBoxItem(cID, firstName, lastName));
            }
        }
        return customers;
    }
}