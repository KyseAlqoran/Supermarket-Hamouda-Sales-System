package d.demo6;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.beans.property.ReadOnlyDoubleProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Dashboard {
    public ScrollPane p() {
        VBox dashboardContent = new VBox(40);
        dashboardContent.setPadding(new Insets(40, 60, 40, 60));
        dashboardContent.setAlignment(Pos.TOP_CENTER);
        dashboardContent.setStyle("-fx-background-color: #f8f9fa;");

        Label dashboardTitle = new Label("🛒 Hamoda Supermarket Dashboard");
        dashboardTitle.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.BOLD, 32));
        dashboardTitle.setStyle("-fx-text-fill: #2c3e50; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 2, 0.5, 0, 1);");
        dashboardContent.getChildren().add(dashboardTitle);

        VBox statsSection = new VBox(20);
        Label statsTitle = new Label("📈 Key Performance Indicators");
        statsTitle.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.SEMI_BOLD, 20));
        statsTitle.setStyle("-fx-text-fill: #495057;");
        GridPane statsGrid = generalDetails();
        statsSection.getChildren().addAll(statsTitle, statsGrid);
        dashboardContent.getChildren().add(statsSection);

        VBox tablesSection = createTablesSection();
        dashboardContent.getChildren().add(tablesSection);

        ScrollPane scrollPane = new ScrollPane(dashboardContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPrefViewportWidth(1400);
        scrollPane.setPrefViewportHeight(900);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scrollPane;
    }

    private GridPane generalDetails() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(25);
        gridPane.setVgap(25);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 0, 20, 0));

        String[] boxTitles = {
                "📦 Total Products",
                "👨‍💼 Total Employees",
                "🏬 Products per Warehouse",
                "🧍 Active Customers",
                "📊 Total Active Users",
                "💰 Total Sales Today",
                "🛒 Total Orders Today",
                "🛍️ Purchases Today"
        };

        String[] boxColors = {
                "#4CAF50",
                "#2196F3",
                "#FF9800",
                "#9C27B0",
                "#FFC107",
                "#F44336",
                "#00BCD4",
                "#8BC34A",
        };

        int cols = 4;
        for (int i = 0; i < boxTitles.length; i++) {
            StackPane box = boxMaker(boxTitles[i], i, boxColors[i]);
            int col = i % cols;
            int row = i / cols;
            gridPane.add(box, col, row);
        }

        return gridPane;
    }

    public StackPane boxMaker(String title, int method, String accentColor) {
        StackPane stackPane = new StackPane();

        Rectangle card = new Rectangle(220, 120);
        card.setArcWidth(25);
        card.setArcHeight(25);
        card.setFill(Color.WHITE);
        card.setStroke(Color.web(accentColor));
        card.setStrokeWidth(2);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(15);
        shadow.setOffsetX(0);
        shadow.setOffsetY(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        card.setEffect(shadow);

        VBox contentVBox = new VBox(12);
        contentVBox.setAlignment(Pos.CENTER);
        contentVBox.setPadding(new Insets(15));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #495057; -fx-wrap-text: true; -fx-text-alignment: center;");
        titleLabel.setMaxWidth(180);

        Label valueLabel = new Label(String.valueOf(methodCaller(method)));
        valueLabel.setStyle("-fx-font-weight: 800; -fx-font-size: 28px; -fx-text-fill: " + accentColor + ";");

        contentVBox.getChildren().addAll(titleLabel, valueLabel);
        stackPane.getChildren().addAll(card, contentVBox);
        stackPane.setStyle("-fx-cursor: hand;");

        ScaleTransition hoverGrow = new ScaleTransition(Duration.millis(200), stackPane);
        ScaleTransition hoverShrink = new ScaleTransition(Duration.millis(200), stackPane);
        hoverGrow.setToX(1.05);
        hoverGrow.setToY(1.05);
        hoverShrink.setToX(1.0);
        hoverShrink.setToY(1.0);

        stackPane.setOnMouseEntered(e -> {
            hoverGrow.playFromStart();
            card.setFill(Color.web(accentColor + "15"));
            shadow.setRadius(20);
            shadow.setOffsetY(8);
        });

        stackPane.setOnMouseExited(e -> {
            hoverShrink.playFromStart();
            card.setFill(Color.WHITE);
            shadow.setRadius(15);
            shadow.setOffsetY(5);
        });

        return stackPane;
    }

    private VBox createTablesSection() {
        VBox tablesSection = new VBox(30);
        tablesSection.setPadding(new Insets(20, 0, 0, 0));

        Label tablesTitle = new Label("📋 Data Management Tables");
        tablesTitle.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.SEMI_BOLD, 24));
        tablesTitle.setStyle("-fx-text-fill: #495057;");
        tablesSection.getChildren().add(tablesTitle);

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1; -fx-border-radius: 10;");
        tabPane.setPrefHeight(400);

        Tab warehouseTab = new Tab("🏭 Warehouses");
        warehouseTab.setClosable(false);
        warehouseTab.setContent(createWarehouseTable());

        Tab discountTab = new Tab("🏷️ Discounts");
        discountTab.setClosable(false);
        discountTab.setContent(createDiscountTable());

        Tab branchTab = new Tab("🏢 Branches");
        branchTab.setClosable(false);
        branchTab.setContent(createBranchTable());

        Tab deliveryTab = new Tab("🚚 Deliveries");
        deliveryTab.setClosable(false);
        deliveryTab.setContent(createDeliveryTable());

        tabPane.getTabs().addAll(warehouseTab, discountTab, branchTab, deliveryTab);
        tablesSection.getChildren().add(tabPane);

        return tablesSection;
    }

    private TableView<Warehouse> createWarehouseTable() {
        TableView<Warehouse> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");

        ReadOnlyDoubleProperty tableWidth = table.widthProperty();

        TableColumn<Warehouse, Integer> idCol = new TableColumn<>("Warehouse ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.prefWidthProperty().bind(tableWidth.multiply(0.25));
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Warehouse, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacityCol.prefWidthProperty().bind(tableWidth.multiply(0.25));
        capacityCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Warehouse, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.prefWidthProperty().bind(tableWidth.multiply(0.50));
        locationCol.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(idCol, capacityCol, locationCol);
        table.setItems(MainWindow.warehouseList);
        table.setPlaceholder(new Label("No warehouse data available"));

        // Set column resize policy for better distribution
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private TableView<Discount> createDiscountTable() {
        TableView<Discount> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");

        ReadOnlyDoubleProperty tableWidth = table.widthProperty();

        TableColumn<Discount, Integer> idCol = new TableColumn<>("Discount ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("disID"));
        idCol.prefWidthProperty().bind(tableWidth.multiply(0.12));
        idCol.setEditable(false);
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Discount, LocalDate> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startCol.prefWidthProperty().bind(tableWidth.multiply(0.18));
        startCol.setEditable(true);
        startCol.setStyle("-fx-alignment: CENTER;");
        startCol.setCellFactory(col -> {
            TableCell<Discount, LocalDate> cell = new TableCell<>() {
                private DatePicker datePicker = new DatePicker();

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else setText(item.toString());
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    datePicker.setValue(getItem());
                    setGraphic(datePicker);
                    setText(null);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setText(getItem() != null ? getItem().toString() : null);
                    setGraphic(null);
                }

                @Override
                public void commitEdit(LocalDate newValue) {
                    super.commitEdit(newValue);
                    Discount discount = getTableRow().getItem();
                    discount.setStartDate(newValue);
                    updateDiscountInDB(discount);
                }
            };
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) cell.startEdit();
            });
            return cell;
        });

        TableColumn<Discount, LocalDate> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endCol.prefWidthProperty().bind(tableWidth.multiply(0.18));
        endCol.setEditable(true);
        endCol.setStyle("-fx-alignment: CENTER;");
        endCol.setCellFactory(col -> {
            TableCell<Discount, LocalDate> cell = new TableCell<>() {
                private DatePicker datePicker = new DatePicker();

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else setText(item.toString());
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    datePicker.setValue(getItem());
                    setGraphic(datePicker);
                    setText(null);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setText(getItem() != null ? getItem().toString() : null);
                    setGraphic(null);
                }

                @Override
                public void commitEdit(LocalDate newValue) {
                    super.commitEdit(newValue);
                    Discount discount = getTableRow().getItem();
                    discount.setEndDate(newValue);
                    updateDiscountInDB(discount);
                }
            };
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) cell.startEdit();
            });
            return cell;
        });

        TableColumn<Discount, Double> percentageCol = new TableColumn<>("Percentage (%)");
        percentageCol.setCellValueFactory(new PropertyValueFactory<>("disPercentage"));
        percentageCol.prefWidthProperty().bind(tableWidth.multiply(0.18));
        percentageCol.setEditable(true);
        percentageCol.setStyle("-fx-alignment: CENTER;");
        percentageCol.setCellFactory(col -> {
            TableCell<Discount, Double> cell = new TableCell<>() {
                private TextField textField = new TextField();

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) setText(null);
                    else setText(String.format("%.1f%%", item));
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    textField.setText(getItem().toString());
                    setGraphic(textField);
                    setText(null);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setText(String.format("%.1f%%", getItem()));
                    setGraphic(null);
                }

                @Override
                public void commitEdit(Double newValue) {
                    super.commitEdit(newValue);
                    Discount discount = getTableRow().getItem();
                    discount.setDisPercentage(newValue);
                    updateDiscountInDB(discount);
                }
            };
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) cell.startEdit();
            });
            return cell;
        });

        TableColumn<Discount, Integer> productCol = new TableColumn<>("Product ID");
        productCol.setCellValueFactory(new PropertyValueFactory<>("prodID"));
        productCol.prefWidthProperty().bind(tableWidth.multiply(0.14));
        productCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Discount, Void> deleteCol = new TableColumn<>("Actions");
        deleteCol.prefWidthProperty().bind(tableWidth.multiply(0.20));
        deleteCol.setStyle("-fx-alignment: CENTER;");
        deleteCol.setCellFactory(col -> {
            TableCell<Discount, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 15; -fx-cursor: hand;");
                    deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #ff3742; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 15; -fx-cursor: hand;"));
                    deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 15; -fx-cursor: hand;"));
                    deleteButton.setOnAction(e -> {
                        Discount discount = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Discount");
                        alert.setHeaderText("Are you sure you want to delete this discount?");
                        alert.setContentText("Discount ID: " + discount.getDisID() + "\nPercentage: " + discount.getDisPercentage() + "%");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) deleteDiscountInDB(discount.getDisID());
                        });
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : deleteButton);
                }
            };
            return cell;
        });

        table.getColumns().addAll(idCol, startCol, endCol, percentageCol, productCol, deleteCol);
        table.setEditable(true);
        table.setItems(MainWindow.discountList);
        table.setPlaceholder(new Label("No discount data available"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private TableView<Branch> createBranchTable() {
        TableView<Branch> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");

        ReadOnlyDoubleProperty tableWidth = table.widthProperty();

        TableColumn<Branch, Integer> idCol = new TableColumn<>("Branch ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.prefWidthProperty().bind(tableWidth.multiply(0.15));
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Branch, String> nameCol = new TableColumn<>("Branch Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.prefWidthProperty().bind(tableWidth.multiply(0.25));
        nameCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Branch, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.prefWidthProperty().bind(tableWidth.multiply(0.35));
        locationCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Branch, String> phoneCol = new TableColumn<>("Phone Numbers");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneCol.prefWidthProperty().bind(tableWidth.multiply(0.25));
        phoneCol.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(idCol, nameCol, locationCol, phoneCol);
        table.setItems(MainWindow.branchList);
        table.setPlaceholder(new Label("No branch data available"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private TableView<Delivery> createDeliveryTable() {
        TableView<Delivery> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");

        ReadOnlyDoubleProperty tableWidth = table.widthProperty();

        TableColumn<Delivery, Integer> idCol = new TableColumn<>("Delivery ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.prefWidthProperty().bind(tableWidth.multiply(0.12));
        idCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Delivery, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.prefWidthProperty().bind(tableWidth.multiply(0.18));
        dateCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Delivery, Boolean> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.prefWidthProperty().bind(tableWidth.multiply(0.15));
        statusCol.setStyle("-fx-alignment: CENTER;");
        statusCol.setEditable(true);

        ObservableList<Boolean> statusOptions = FXCollections.observableArrayList(true, false);
        statusCol.setCellFactory(ComboBoxTableCell.forTableColumn(statusOptions));

        statusCol.setCellFactory(col -> {
            ComboBoxTableCell<Delivery, Boolean> cell = new ComboBoxTableCell<>(statusOptions) {
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "True" : "False");
                        setStyle(item ? "-fx-text-fill: #28a745; -fx-font-weight: bold;" : "-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                    }
                }
            };
            return cell;
        });

        statusCol.setOnEditCommit(event -> {
            Delivery delivery = event.getRowValue();
            Boolean newStatus = event.getNewValue();
            delivery.setStatus(newStatus);
            updateDeliveryStatusInDB(delivery.getId(), newStatus);
        });

        TableColumn<Delivery, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.prefWidthProperty().bind(tableWidth.multiply(0.35));
        addressCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Delivery, Void> deleteCol = new TableColumn<>("Actions");
        deleteCol.prefWidthProperty().bind(tableWidth.multiply(0.20));
        deleteCol.setStyle("-fx-alignment: CENTER;");
        deleteCol.setCellFactory(col -> {
            TableCell<Delivery, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Deactivate");

                {
                    deleteButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 15; -fx-cursor: hand;");
                    deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #ff3742; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 15; -fx-cursor: hand;"));
                    deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 15; -fx-cursor: hand;"));
                    deleteButton.setOnAction(e -> {
                        Delivery delivery = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Deactivate Delivery");
                        alert.setHeaderText("Are you sure you want to deactivate this delivery?");
                        alert.setContentText("Delivery ID: " + delivery.getId() + "\nAddress: " + delivery.getAddress());
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                setDeliveryInactiveInDB(delivery.getId());
                                try {
                                    MainApplication.readDB();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                } catch (ClassNotFoundException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : deleteButton);
                }
            };
            return cell;
        });

        table.getColumns().addAll(idCol, dateCol, statusCol, addressCol, deleteCol);
        table.setEditable(true);
        table.setItems(MainWindow.deliveryList);
        table.setPlaceholder(new Label("No delivery data available"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    private void setDeliveryInactiveInDB(int id) {
        String sql = "UPDATE Delivery SET isActive = false WHERE dID = " + id;
        try (Connection conn = MainApplication.dbConn.connectDB();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private double methodCaller(int c) {
        LocalDate today = LocalDate.now();
        String todayString = today.toString();
        String[] q = {
                "SELECT COUNT(*) AS total_products FROM Product;",
                "SELECT COUNT(*) AS total_employees FROM Employee;",
                "SELECT wID, COUNT(*) AS product_count FROM Product GROUP BY wID;",
                "SELECT COUNT(*) AS active_customers FROM Customer WHERE isActive = TRUE;",
                "SELECT COUNT(*) AS active_users FROM User WHERE activeStatus = TRUE;",
                "SELECT SUM(quantity * unitPrice - discount) AS total_sales_today FROM Sale JOIN Sale_Details ON Sale.saleID = Sale_Details.saleID WHERE sale_date = '" + todayString + "';",
                "SELECT SUM(quantity * (SELECT price FROM Product WHERE prodID = Order_Detail.prodID)) AS total_orders_today FROM Orders JOIN Order_Detail ON Orders.oID = Order_Detail.oID WHERE oDate = '" + todayString + "';",
                "SELECT COUNT(*) AS purchases_today FROM Sale WHERE sale_date = '" + todayString + "';"
        };
        return queryExecute(q[c]);
    }

    private double queryExecute(String query) {
        double result = 0;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) result = rs.getDouble(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private void updateDiscountInDB(Discount discount) {
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement()) {
            String sql = "UPDATE Discount SET startDate = '" + discount.getStartDate() + "', endDate = '" + discount.getEndDate() + "', disPercentage = " + discount.getDisPercentage() + " WHERE disID = " + discount.getDisID() + ";";
            stmt.executeUpdate(sql);
            MainApplication.readDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteDiscountInDB(int disID) {
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement()) {
            String sql = "DELETE FROM Discount WHERE disID = " + disID + ";";
            stmt.executeUpdate(sql);
            MainApplication.readDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateDeliveryStatusInDB(int deliveryID, boolean status) {
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement()) {
            String sql = "UPDATE Delivery SET dStatus = " + (status ? 1 : 0) + " WHERE dID = " + deliveryID + ";";
            stmt.executeUpdate(sql);
            MainApplication.readDB();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to update delivery status");
            alert.setContentText("Error: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void deleteDeliveryInDB(int deliveryID) {
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement()) {
            String sql = "DELETE FROM Delivery WHERE dID = " + deliveryID + ";";
            stmt.executeUpdate(sql);
            MainApplication.readDB();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to delete delivery");
            alert.setContentText("Error: " + ex.getMessage());
            alert.showAndWait();
        }
    }
}