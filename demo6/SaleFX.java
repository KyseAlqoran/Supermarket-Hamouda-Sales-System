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

public class SaleFX {
    public static ObservableList<Sale> saleList = FXCollections.observableArrayList();
    public static TableView<Sale> saleTable = new TableView<>(saleList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    public SaleFX() {
    }

    public BorderPane createAddSalePane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));

        Label headerLabel = new Label("Add New Sale");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        GridPane formGrid = createSaleForm();
        VBox cardContainer = new VBox(header, formGrid);
        cardContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        cardContainer.setPadding(new Insets(30));
        cardContainer.setMaxWidth(600);
        mainPane.setCenter(cardContainer);

        return mainPane;
    }

    private GridPane createSaleForm() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        String[] labelTexts = {"Date:", "Branch:", "Buyer:"};
        Label[] labels = new Label[labelTexts.length];
        DatePicker datePicker = new DatePicker();
        ComboBox<String> branchComboBox = new ComboBox<>();
        ComboBox<String> customerComboBox = new ComboBox<>();

        initializeFormComponents(labels, labelTexts, datePicker, branchComboBox, customerComboBox);
        addComponentsToGrid(grid, labels, datePicker, branchComboBox, customerComboBox);

        Button submitButton = createSubmitButton(datePicker, branchComboBox, customerComboBox);
        grid.add(submitButton, 1, 4);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        return grid;
    }

    private void initializeFormComponents(Label[] labels, String[] labelTexts,
                                          DatePicker datePicker, ComboBox<String> branchComboBox, ComboBox<String> customerComboBox) {
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelTexts[i]);
            labels[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }
        datePicker.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        datePicker.setPrefWidth(300);
        datePicker.setEditable(false);
        loadBranches(branchComboBox);
        branchComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        branchComboBox.setPrefWidth(300);
        loadBuyers(customerComboBox);
        customerComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        customerComboBox.setPrefWidth(300);
    }

    private void loadBranches(ComboBox<String> branchComboBox) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT bID, bName FROM branch");
            while (rs.next()) {
                branchComboBox.getItems().add(rs.getInt("bID") + " - " + rs.getString("bName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBuyers(ComboBox<String> buyerComboBox) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.pID, p.pFirstName, p.pLastName FROM person p;");
            while (rs.next()) {
                buyerComboBox.getItems().add(rs.getInt("pID") + " - " + rs.getString("pFirstName") + " " + rs.getString("pLastName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addComponentsToGrid(GridPane grid, Label[] labels,
                                     DatePicker datePicker, ComboBox<String> branchComboBox, ComboBox<String> buyerComboBox) {
        grid.add(labels[0], 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(labels[1], 0, 1);
        grid.add(branchComboBox, 1, 1);
        grid.add(labels[2], 0, 2);
        grid.add(buyerComboBox, 1, 2);
    }

    private Button createSubmitButton(DatePicker datePicker,
                                      ComboBox<String> branchComboBox, ComboBox<String> buyerIDComboBox) {
        Button submitButton = new Button("Add Sale");
        submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        submitButton.setPrefWidth(200);
        submitButton.setOnMouseEntered(e ->
                submitButton.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnMouseExited(e ->
                submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnAction(e -> addSale(datePicker, branchComboBox, buyerIDComboBox));
        return submitButton;
    }

    public HBox createSaleTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox sidebar = createSidebar();
        createSaleTable();
        mainPane.getChildren().addAll(sidebar, saleTable);
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
        Button addSaleButton = createAddSaleButton();
        sidebar.getChildren().addAll(actionLabel, addSaleButton);
        return sidebar;
    }

    private Button createAddSaleButton() {
        ImageView addIcon = new ImageView(getClass().getResource("/addEmployee.png").toExternalForm());
        addIcon.setFitHeight(24);
        addIcon.setFitWidth(24);
        addIcon.setPreserveRatio(true);

        Button addButton = new Button("Add Sale", addIcon);
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        addButton.setPrefWidth(180);
        addButton.setContentDisplay(ContentDisplay.LEFT);

        addButton.setOnMouseEntered(e ->
                addButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        addButton.setOnMouseExited(e ->
                addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        addButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(createAddSalePane(), 700, 500));
            stage.setResizable(false);
            stage.setTitle("Add New Sale");
            stage.show();
        });

        return addButton;
    }

    private void createSaleTable() {
        String[] columnNames = {"ID", "Date", "Total Price", "Branch Name", "Person Name"};
        String[] propertyNames = {"saleID", "date", "total_price", "branchName", "personName"};
        TableColumn<Sale, ?>[] tc = new TableColumn[columnNames.length];
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

        TableColumn<Sale, Void> deleteColumn = createDeleteColumn();
        deleteColumn.setMinWidth(200);
        deleteColumn.setPrefWidth(230);
        deleteColumn.setMaxWidth(250);

        saleTable.getColumns().clear();
        saleTable.getColumns().addAll(tc);
        saleTable.getColumns().add(deleteColumn);
        saleTable.setItems(saleList);
        saleTable.setEditable(true);
        saleTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        saleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        saleTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindTableDimensions();
            }
        });
    }

    private void bindTableDimensions() {
        saleTable.prefWidthProperty().bind(saleTable.getScene().widthProperty().subtract(250));
        saleTable.prefHeightProperty().bind(saleTable.getScene().heightProperty().subtract(100));
    }

    private TableColumn<Sale, Void> createDeleteColumn() {
        TableColumn<Sale, Void> deleteColumn = new TableColumn<>("Actions");
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

                btn.setOnAction(event -> handleDeleteSale());
                return btn;
            }

            private void handleDeleteSale() {
                Sale sale = getTableView().getItems().get(getIndex());
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Delete Sale");
                confirmAlert.setContentText("Are you sure you want to delete Sale ID: " + sale.getSaleID() + "?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteSaleFromDatabase(sale);
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

    private void updateSale(Sale sale) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String updateSaleSQL = "UPDATE Sale SET " +
                    "sale_date = '" + sale.getDate() + "', " +
                    "bID = " + sale.getbID() + ", " +
                    "pID = " + sale.getpID() + " " +
                    "WHERE saleID = " + sale.getSaleID();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSaleSQL);
            showAlert("Success", "Sale updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update sale: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void deleteSaleFromDatabase(Sale sale) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM sale WHERE saleID = " + sale.getSaleID());

            MainApplication.readDB();
            showAlert("Success", "Sale ID " + sale.getSaleID() + " deleted successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to delete sale: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addSale(DatePicker datePicker,
                         ComboBox<String> branchComboBox, ComboBox<String> buyerComboBox) {

        if (validateFormFields(datePicker, branchComboBox, buyerComboBox)) {
            try {
                LocalDate date = datePicker.getValue();
                int branchId = extractIdFromComboBox(branchComboBox.getValue());
                int buyerID = extractIdFromComboBox(buyerComboBox.getValue());

                addSale(date, branchId, buyerID);

                clearForm(datePicker, branchComboBox, buyerComboBox);
                showAlert("Success", "Sale added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add sale: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private int extractIdFromComboBox(String comboBoxValue) {
        return Integer.parseInt(comboBoxValue.split(" - ")[0]);
    }

    private boolean validateFormFields(DatePicker datePicker,
                                       ComboBox<String> branchComboBox, ComboBox<String> customerComboBox) {
        if (datePicker.getValue() == null || branchComboBox.getValue() == null || customerComboBox.getValue() == null) {
            showAlert("Validation Error", "Please fill in all required fields.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void addSale(LocalDate date, int branchId, int personId) throws SQLException, ClassNotFoundException {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            String insertSaleSQL = "INSERT INTO Sale (sale_date, bID, pID) " +
                    "VALUES ('" + Date.valueOf(date) + "', " + branchId + ", " + personId + ")";
            stmt.executeUpdate(insertSaleSQL);
            MainApplication.readDB();
        }
    }


    private void clearForm( DatePicker datePicker,
                           ComboBox<String> branchComboBox, ComboBox<String> customerComboBox) {
        datePicker.setValue(null);
        branchComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane addPane() {
        return createAddSalePane();
    }

    public HBox saleTablePane() {
        return createSaleTablePane();
    }
}