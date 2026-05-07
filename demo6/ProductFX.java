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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ProductFX {
    public static ObservableList<Product> productList = FXCollections.observableArrayList();
    public static TableView<Product> productTable = new TableView<>(productList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    public BorderPane createAddProductPane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));

        Label headerLabel = new Label("Add New Product");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        GridPane gp = gp();
        VBox cardContainer = new VBox(header, gp);
        cardContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        cardContainer.setPadding(new Insets(30));
        cardContainer.setMaxWidth(600);
        mainPane.setCenter(cardContainer);

        return mainPane;
    }

    private GridPane gp() {
        GridPane gp = new GridPane();
        gp.setHgap(20);
        gp.setVgap(20);
        gp.setAlignment(Pos.CENTER);

        String[] lt = {"Product Name:", "Description:", "Price:", "Quantity:", "Expire Date:", "Category:", "Warehouse:", "Image:"};
        Label[] l = new Label[lt.length];
        TextField[] tf = new TextField[4];
        DatePicker dp = new DatePicker();
        ComboBox<String> cb1 = new ComboBox<>();
        ComboBox<String> cb2 = new ComboBox<>();
        Button icon = new Button("Select Image");
        ImageView selectedIcon = new ImageView("noProd.png");

        setStyles(l, lt, tf, dp, cb1, cb2, icon, selectedIcon);
        addToGrid(gp, l, tf, dp, cb1, cb2, icon, selectedIcon);

        Button submitButton = createSubmitButton(tf, dp, cb1, cb2, selectedIcon);
        gp.add(submitButton, 1, 8);
        GridPane.setHalignment(submitButton, HPos.RIGHT);

        return gp;
    }

    private void setStyles(Label[] l, String[] lt, TextField[] tf,
                           DatePicker dp, ComboBox<String> cb1,
                           ComboBox<String> cb2, Button b, ImageView selectionIcon) {
        for (int i = 0; i < l.length; i++) {
            l[i] = new Label(lt[i]);
            l[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }

        for (int i = 0; i < tf.length; i++) {
            tf[i] = new TextField();
            tf[i].setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
            tf[i].setPrefWidth(300);
        }

        dp.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        dp.setPrefWidth(300);
        dp.setEditable(false);

        try {
            Connection con = MainApplication.dbConn.connectDB();
            Statement stmt = con.createStatement();
            ResultSet rsCat = stmt.executeQuery("SELECT catName FROM Category WHERE isActive = TRUE;");
            while (rsCat.next()) {
                cb1.getItems().add(rsCat.getString("catName"));
            }
            rsCat.close();

            ResultSet rsWh = stmt.executeQuery("SELECT wLocation FROM Warehouse");
            while (rsWh.next()) {
                cb2.getItems().add(rsWh.getString("wLocation"));
            }
            rsWh.close();
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cb1.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        cb1.setPrefWidth(300);
        cb2.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        cb2.setPrefWidth(300);

        b.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;"));

        b.setOnAction(e -> {
            File file = fileChooserMethod("Choose Product Image");
            if (file != null) {
                try {
                    Image m = new Image(file.toURI().toURL().toExternalForm());
                    selectionIcon.setImage(m);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        selectionIcon.setPreserveRatio(true);
        selectionIcon.setFitHeight(100);
        selectionIcon.setFitWidth(100);
    }

    private void addToGrid(GridPane gp, Label[] l, TextField[] tf,
                           DatePicker dp, ComboBox<String> cb1,
                           ComboBox<String> cb2, Button b, ImageView selectedIcon) {
        for (int i = 0; i < 4; i++) {
            gp.add(l[i], 0, i);
            gp.add(tf[i], 1, i);
        }
        gp.add(l[4], 0, 4);
        gp.add(dp, 1, 4);
        gp.add(l[5], 0, 5);
        gp.add(cb1, 1, 5);
        gp.add(l[6], 0, 6);
        gp.add(cb2, 1, 6);
        gp.add(l[7], 0, 7);
        gp.add(b, 1, 7);
        gp.add(selectedIcon, 2, 7);
    }

    private Button createSubmitButton(TextField[] tf, DatePicker dp,
                                      ComboBox<String> cb1, ComboBox<String> cb2,
                                      ImageView selectedIcon) {
        Button b = new Button("Add Product");
        b.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;");
        b.setPrefWidth(200);
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        b.setOnAction(e -> addProduct(tf, dp, new ComboBox[]{cb1, cb2}, selectedIcon));
        return b;
    }

    public HBox createProductTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox vb = createSidebar();
        createProductTable();
        mainPane.getChildren().addAll(vb, productTable);
        mainPane.setAlignment(Pos.CENTER);
        return mainPane;
    }

    private VBox createSidebar() {
        VBox vb = new VBox(15);
        vb.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");
        vb.setPrefWidth(550);
        vb.setPadding(new Insets(30));

        Label l = new Label("Actions");
        l.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField searchField = createSearchField();
        Button addButton = addProduct();
        ComboBox<String> categoryFilter = createCategoryFilter();

        vb.getChildren().addAll(l, searchField, categoryFilter, addButton);
        return vb;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search products... (Press Enter)");
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        searchField.setPrefWidth(230);

        searchField.setOnAction(e -> performSearch(searchField.getText()));

        return searchField;
    }

    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            productTable.setItems(productList);
            productTable.refresh();
        } else {
            ObservableList<Product> filteredList = FXCollections.observableArrayList();
            String lowerSearchText = searchText.toLowerCase().trim();
            for (Product product : productList) {
                if (product.getProdName().toLowerCase().contains(lowerSearchText) ||
                        product.getDescription().toLowerCase().contains(lowerSearchText) ||
                        product.getCatName().toLowerCase().contains(lowerSearchText) ||
                        product.getLocation().toLowerCase().contains(lowerSearchText)) {
                    filteredList.add(product);
                }
            }
            productTable.setItems(filteredList);
            productTable.refresh();
        }
    }

    private ComboBox<String> createCategoryFilter() {
        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.setPromptText("Filter by Category");
        categoryFilter.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        categoryFilter.setPrefWidth(230);

        categoryFilter.getItems().add("All Products");
        try {
            Connection con = MainApplication.dbConn.connectDB();
            Statement stmt = con.createStatement();
            ResultSet rsCat = stmt.executeQuery("SELECT DISTINCT catName FROM Category WHERE isActive = TRUE;");
            while (rsCat.next()) {
                categoryFilter.getItems().add(rsCat.getString("catName"));
            }
            rsCat.close();
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        categoryFilter.setOnAction(e -> filterByCategory(categoryFilter.getValue()));

        return categoryFilter;
    }

    private void filterByCategory(String selectedCategory) {
        if (selectedCategory == null || selectedCategory.equals("All Products")) {
            productTable.setItems(productList);
            productTable.refresh();
        } else {
            ObservableList<Product> filteredList = FXCollections.observableArrayList();
            for (Product product : productList) {
                if (product.getCatName().equals(selectedCategory)) {
                    filteredList.add(product);
                }
            }
            productTable.setItems(filteredList);
            productTable.refresh();
        }
    }

    private void createProductTable() {
        String[] names = {"ID", "Name", "Description", "Price", "Quantity", "Expire Date", "Category", "Warehouse"};
        String[] getters = {"prodID", "prodName", "description", "price", "quantity", "expire_Date", "catName", "location"};
        TableColumn<Product, ?>[] tc = new TableColumn[names.length];
        double colWidth = Math.max(80, screenWidth * 0.08);
        double minColWidth = screenWidth * 0.06;
        double maxColWidth = screenWidth * 0.12;

        for (int i = 0; i < names.length; i++) {
            tc[i] = new TableColumn<>(names[i]);
            if (i == 0) {
                tc[i].setPrefWidth(80);
                tc[i].setMinWidth(60);
                tc[i].setMaxWidth(100);
            } else {
                tc[i].setMinWidth(minColWidth);
                tc[i].setPrefWidth(colWidth);
                tc[i].setMaxWidth(maxColWidth);
            }
            tc[i].setCellValueFactory(new PropertyValueFactory<>(getters[i]));
            tc[i].setStyle("-fx-alignment: CENTER; -fx-font-size: 14px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        }

        TableColumn<Product, Void> actionColumn = createActionColumn();
        actionColumn.setMinWidth(220);
        actionColumn.setPrefWidth(250);
        actionColumn.setMaxWidth(280);

        productTable.getColumns().clear();
        productTable.getColumns().addAll(tc);
        productTable.getColumns().add(actionColumn);
        productTable.setItems(productList);
        productTable.setEditable(true);
        productTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        productTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindTableDimensions();
            }
        });

        setupTableEditing(tc);
    }

    private TableColumn<Product, Void> createActionColumn() {
        TableColumn<Product, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final HBox buttonBox = new HBox(5);
            private final Button deleteButton = createDeleteButton();
            private final Button updateImageButton = createUpdateImageButton();

            {
                buttonBox.getChildren().addAll(deleteButton, updateImageButton);
                buttonBox.setAlignment(Pos.CENTER);
            }

            private Button createDeleteButton() {
                Button b = new Button("Delete");
                b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-border-radius: 6;");
                b.setPrefWidth(80);

                b.setOnMouseEntered(e ->
                        b.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-border-radius: 6;")
                );

                b.setOnMouseExited(e ->
                        b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-border-radius: 6;")
                );

                b.setOnAction(event -> handleDeleteProduct());
                return b;
            }

            private Button createUpdateImageButton() {
                Button b = new Button("Update Image");
                b.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-border-radius: 6;");
                b.setPrefWidth(120);

                b.setOnMouseEntered(e ->
                        b.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-border-radius: 6;")
                );

                b.setOnMouseExited(e ->
                        b.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 6 12; -fx-background-radius: 6; -fx-border-radius: 6;")
                );

                b.setOnAction(event -> handleUpdateImage());
                return b;
            }

            private void handleDeleteProduct() {
                Product p = getTableView().getItems().get(getIndex());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Delete Product");
                alert.setContentText("Are you sure you want to delete " + p.getProdName() + "?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteFromDatabase(p);
                    }
                });
            }

            private void handleUpdateImage() {
                Product p = getTableView().getItems().get(getIndex());
                File file = fileChooserMethod("Choose New Product Image");
                if (file != null) {
                    try {
                        String url = file.toURI().toURL().toExternalForm();
                        String imageName = url.substring(url.lastIndexOf('/') + 1);

                        Connection con = MainApplication.dbConn.connectDB();
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate("UPDATE Product SET photoPath = '" + imageName + "' WHERE prodID = " + p.getProdID());
                        stmt.close();
                        con.close();

                        MainApplication.readDB();
                        showAlert("Success", "Product image updated successfully!", Alert.AlertType.INFORMATION);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert("Error", "Failed to update image: " + ex.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
        return actionColumn;
    }

    private Button addProduct() {
        ImageView addIcon = new ImageView("addProduct.png");
        addIcon.setFitHeight(24);
        addIcon.setFitWidth(24);
        addIcon.setPreserveRatio(true);

        Button addButton = new Button("Add Product", addIcon);
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;");
        addButton.setPrefWidth(230);
        addButton.setContentDisplay(ContentDisplay.LEFT);

        addButton.setOnMouseEntered(e ->
                addButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;")
        );

        addButton.setOnMouseExited(e ->
                addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;")
        );

        addButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(createAddProductPane(), 700, 850));
            stage.setResizable(false);
            stage.setTitle("Add New Product");
            stage.show();
        });

        return addButton;
    }

    private void bindTableDimensions() {
        productTable.prefWidthProperty().bind(productTable.getScene().widthProperty().subtract(280));
        productTable.prefHeightProperty().bind(productTable.getScene().heightProperty().subtract(100));
    }

    private TableColumn<Product, Void> createDeleteColumn() {
        TableColumn<Product, Void> deleteColumn = new TableColumn<>("Actions");
        deleteColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createDeleteButton();

            private Button createDeleteButton() {
                ImageView deleteIcon = new ImageView("rmProduct.png");
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteIcon.setPreserveRatio(true);

                Button b = new Button("Delete", deleteIcon);
                b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;");
                b.setPrefWidth(120);
                b.setContentDisplay(ContentDisplay.LEFT);

                b.setOnMouseEntered(e ->
                        b.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;")
                );

                b.setOnMouseExited(e ->
                        b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 8; -fx-border-radius: 8;")
                );

                b.setOnAction(event -> handleDeleteProduct());
                return b;
            }

            private void handleDeleteProduct() {
                Product p = getTableView().getItems().get(getIndex());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Delete Product");
                alert.setContentText("Are you sure you want to delete " + p.getProdName() + "?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteFromDatabase(p);
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

    private void deleteFromDatabase(Product product) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String sql = "UPDATE Product SET isActive = false WHERE prodID = " + product.getProdID();
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(sql);
                MainApplication.readDB();
                showAlert("Success", product.getProdName() + " deactivated successfully!", Alert.AlertType.INFORMATION);
            } catch (SQLException ex) {
                showAlert("Error", "Failed to deactivate product: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            showAlert("Error", "Database connection error", Alert.AlertType.ERROR);
        }
    }

    private void setupTableEditing(TableColumn<Product, ?>[] tc) {
        ((TableColumn<Product, String>) tc[1]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Product, String>) tc[1]).setOnEditCommit(event -> {
            Product prod = event.getRowValue();
            if (Product.isValidName(event.getNewValue())) {
                prod.setProdName(event.getNewValue());
                updateProduct(prod);
            } else {
                revertTableEdit(event, event.getOldValue());
                showAlert("Invalid Name", "Product name must contain only letters and numbers", Alert.AlertType.WARNING);
            }
        });

        ((TableColumn<Product, String>) tc[2]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Product, String>) tc[2]).setOnEditCommit(event -> {
            Product prod = event.getRowValue();
            prod.setDescription(event.getNewValue());
            updateProduct(prod);
        });

        ((TableColumn<Product, Double>) tc[3]).setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ((TableColumn<Product, Double>) tc[3]).setOnEditCommit(event -> {
            Product prod = event.getRowValue();
            if (Product.isValidPrice(event.getNewValue())) {
                prod.setPrice(event.getNewValue());
                updateProduct(prod);
            } else {
                revertPriceEdit(event, event.getOldValue());
                showAlert("Invalid Price", "Price must be greater than 0", Alert.AlertType.WARNING);
            }
        });

        ((TableColumn<Product, Integer>) tc[4]).setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        ((TableColumn<Product, Integer>) tc[4]).setOnEditCommit(event -> {
            Product prod = event.getRowValue();
            if (Product.isValidQuantity(event.getNewValue())) {
                prod.setQuantity(event.getNewValue());
                updateProduct(prod);
            } else {
                revertQuantityEdit(event, event.getOldValue());
                showAlert("Invalid Quantity", "Quantity must be non-negative", Alert.AlertType.WARNING);
            }
        });

        StringConverter<LocalDate> localDateConverter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return (string != null && !string.isEmpty()) ? LocalDate.parse(string) : null;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        };

        ((TableColumn<Product, LocalDate>) tc[5]).setCellFactory(TextFieldTableCell.forTableColumn(localDateConverter));
        ((TableColumn<Product, LocalDate>) tc[5]).setOnEditCommit(event -> {
            Product prod = event.getRowValue();
            if (event.getNewValue() != null) {
                prod.setExpire_Date(event.getNewValue());
                updateProduct(prod);
            } else {
                prod.setExpire_Date(event.getOldValue());
                showAlert("Invalid Date", "Please enter a valid date (YYYY-MM-DD)", Alert.AlertType.WARNING);
            }
        });

        TableColumn<Product, String> categoryCol = (TableColumn<Product, String>) tc[6];
        categoryCol.setCellFactory(column -> new TableCell<Product, String>() {
            private ComboBox<String> comboBox;

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();

                if (comboBox == null) {
                    comboBox = new ComboBox<>(getCategories());
                    comboBox.setStyle("-fx-font-size: 14px;");
                    comboBox.setOnAction(e -> {
                        commitEdit(comboBox.getValue());
                        getTableView().edit(-1, null);
                    });
                }

                comboBox.setValue(getItem());
                setText(null);
                setGraphic(comboBox);
                comboBox.show();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }

            @Override
            public void commitEdit(String newValue) {
                super.commitEdit(newValue);
                Product prod = getTableView().getItems().get(getIndex());
                if (newValue != null && !newValue.isEmpty()) {
                    prod.setCatName(newValue);
                    updateProduct(prod);
                } else {
                    prod.setCatName(getItem());
                    showAlert("Invalid Category", "Category cannot be empty", Alert.AlertType.WARNING);
                }
                setText(newValue);
                setGraphic(null);
            }
        });

        ((TableColumn<Product, String>) tc[7]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Product, String>) tc[7]).setOnEditCommit(event -> {
            Product prod = event.getRowValue();
            if (event.getNewValue() != null && !event.getNewValue().isEmpty()) {
                prod.setLocation(event.getNewValue());
                updateProduct(prod);
            } else {
                prod.setLocation(event.getOldValue());
                showAlert("Invalid Location", "Location cannot be empty", Alert.AlertType.WARNING);
            }
        });
    }

    private void revertTableEdit(TableColumn.CellEditEvent<Product, String> event, String oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setProdName(oldValue);
        event.getTableView().refresh();
    }

    private void revertPriceEdit(TableColumn.CellEditEvent<Product, Double> event, Double oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setPrice(oldValue);
        event.getTableView().refresh();
    }

    private void revertQuantityEdit(TableColumn.CellEditEvent<Product, Integer> event, Integer oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(oldValue);
        event.getTableView().refresh();
    }

    private void updateProduct(Product prod) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            int catID = 0;
            try (Statement stmtCat = con.createStatement();
                 ResultSet rsCat = stmtCat.executeQuery("SELECT catID FROM Category WHERE catName = '" + prod.getCatName() + "' AND isActive = TRUE;")) {
                if (rsCat.next()) {
                    catID = rsCat.getInt("catID");
                }
            }
            String SQL = "UPDATE product SET " +
                    "prodName = '" + prod.getProdName() + "', " +
                    "description = '" + prod.getDescription() + "', " +
                    "price = " + prod.getPrice() + ", " +
                    "quantity = " + prod.getQuantity() + ", " +
                    "expire_Date = '" + prod.getExpire_Date() + "', " +
                    "catID = " + catID + ", " +
                    "wID = " + prod.getwID() + " " +
                    "WHERE prodID = " + prod.getProdID();
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(SQL);
                showAlert("Success", "Product updated successfully!", Alert.AlertType.INFORMATION);
                MainApplication.readDB();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update product: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addProduct(TextField[] tf, DatePicker dp, ComboBox<String>[] cb, ImageView selectedImage) {
        if (!checkClearFields(tf, dp)) {
            String productName = tf[0].getText();
            String description = tf[1].getText();
            double price = Double.parseDouble(tf[2].getText());
            int quantity = Integer.parseInt(tf[3].getText());
            String expireDate = dp.getValue().toString();
            String catName = cb[0].getValue();
            String warehouseLocation = cb[1].getValue();
            String url = selectedImage.getImage().getUrl();
            String imageName = url.substring(url.lastIndexOf('/') + 1);

            if (!Product.isValidName(productName)) {
                showAlert("Validation Error", "Product name must contain only letters and numbers", Alert.AlertType.WARNING);
            } else if (!Product.isValidPrice(price)) {
                showAlert("Validation Error", "Price must be positive", Alert.AlertType.WARNING);
            } else if (!Product.isValidQuantity(quantity)) {
                showAlert("Validation Error", "Quantity must be non-negative", Alert.AlertType.WARNING);
            } else if (!Product.isValidDate(expireDate)) {
                showAlert("Validation Error", "Enter a valid date", Alert.AlertType.WARNING);
            } else if (catName == null) {
                showAlert("Validation Error", "Please select a category", Alert.AlertType.WARNING);
            } else if (warehouseLocation == null) {
                showAlert("Validation Error", "Please select a warehouse", Alert.AlertType.WARNING);
            } else {
                try {
                    Connection con = MainApplication.dbConn.connectDB();
                    Statement stmt = con.createStatement();
                    ResultSet rsCat = stmt.executeQuery("SELECT catID FROM Category WHERE catName = '" + catName + "' AND isActive = TRUE;");
                    int catID = 0;
                    if (rsCat.next()) {
                        catID = rsCat.getInt("catID");
                    } else {
                        showAlert("Error", "Category not found", Alert.AlertType.ERROR);
                        return;
                    }
                    rsCat.close();

                    ResultSet rsWh = stmt.executeQuery("SELECT wID FROM Warehouse WHERE wLocation = '" + warehouseLocation + "'");
                    int wID = 0;
                    if (rsWh.next()) {
                        wID = rsWh.getInt("wID");
                    } else {
                        showAlert("Error", "Warehouse not found", Alert.AlertType.ERROR);
                        return;
                    }
                    rsWh.close();

                    String sqlProduct = "INSERT INTO Product (prodName, description, price, quantity, expire_Date, catID, wID, photoPath, isActive) VALUES ('"
                            + productName + "', '" + description + "', " + price + ", " + quantity + ", '"
                            + expireDate + "', " + catID + ", " + wID + ", '" + imageName + "', true)";
                    stmt.executeUpdate(sqlProduct);

                    stmt.close();
                    MainApplication.readDB();
                    for (TextField field : tf) {
                        field.clear();
                    }
                    dp.setValue(null);
                    cb[0].setValue(null);
                    cb[1].setValue(null);
                    selectedImage.setImage(new Image("noProd.png"));
                    showAlert("Success", "Product added to database!", Alert.AlertType.INFORMATION);

                } catch (SQLException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                    showAlert("Error", "Database error: " + e1.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Validation Error", "Please fill all required fields", Alert.AlertType.WARNING);
        }
    }


    private boolean checkClearFields(TextField[] tf, DatePicker dp) {
        for (TextField field : tf) {
            if (field.getText().isEmpty()) {
                return true;
            }
        }
        return dp.getValue() == null;
    }

    private File fileChooserMethod(String name) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select " + name);
            return fileChooser.showOpenDialog(null);
        } catch (Exception e) {
            showAlert("Error", "File selection error", Alert.AlertType.ERROR);
        }
        return null;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public HBox productTablePane() {
        return createProductTablePane();
    }

    private ObservableList<String> getCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        try {
            Connection con = MainApplication.dbConn.connectDB();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT catName FROM Category WHERE isActive = TRUE;");
            while (rs.next()) {
                categories.add(rs.getString("catName"));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return categories;
    }
}