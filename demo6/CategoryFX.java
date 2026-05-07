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

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoryFX {
    public static ObservableList<Category> categoryList = FXCollections.observableArrayList();
    public static TableView<Category> categoryTable = new TableView<>(categoryList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    CategoryFX() {
    }

    public BorderPane createAddCategoryPane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));

        Label headerLabel = new Label("Add New Category");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        GridPane formGrid = createCategoryForm();
        VBox cardContainer = new VBox(header, formGrid);
        cardContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        cardContainer.setPadding(new Insets(30));
        cardContainer.setMaxWidth(600);
        mainPane.setCenter(cardContainer);

        return mainPane;
    }

    private GridPane createCategoryForm() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        String[] labelTexts = {"Category Name:", "Description:", "Image:"};
        Label[] labels = new Label[labelTexts.length];
        TextField[] textFields = new TextField[2];
        Button selectImageButton = new Button("Select Image");
        ImageView selectedImageView = new ImageView("noProd.png");

        initializeFormComponents(labels, labelTexts, textFields, selectImageButton, selectedImageView);
        addComponentsToGrid(grid, labels, textFields, selectImageButton, selectedImageView);

        Button submitButton = createSubmitButton(textFields, selectedImageView);
        grid.add(submitButton, 1, 4);
        GridPane.setHalignment(submitButton, HPos.RIGHT);

        return grid;
    }

    private void initializeFormComponents(Label[] labels, String[] labelTexts, TextField[] textFields,
                                          Button selectImageButton, ImageView selectedImageView) {
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new Label(labelTexts[i]);
            labels[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }

        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new TextField();
            textFields[i].setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
            textFields[i].setPrefWidth(300);
        }

        textFields[1].setPrefHeight(80);

        selectImageButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;");
        selectImageButton.setPrefWidth(300);
        selectImageButton.setOnMouseEntered(e ->
                selectImageButton.setStyle("-fx-background-color: #219a52; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        selectImageButton.setOnMouseExited(e ->
                selectImageButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));

        selectedImageView.setFitHeight(100);
        selectedImageView.setFitWidth(100);
        selectedImageView.setPreserveRatio(true);
        selectedImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2); -fx-background-color: white; -fx-background-radius: 8;");

        selectImageButton.setOnAction(e -> handleImageSelection(selectedImageView));
    }

    private void handleImageSelection(ImageView imageView) {
        File file = fileChooserMethod("Choose Category Image");
        if (file != null) {
            try {
                Image m = new Image(file.toURI().toURL().toExternalForm());
                imageView.setImage(m);
                imageView.setUserData(file.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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

    private void addComponentsToGrid(GridPane grid, Label[] labels, TextField[] textFields,
                                     Button selectImageButton, ImageView selectedImageView) {
        for (int i = 0; i < textFields.length; i++) {
            grid.add(labels[i], 0, i);
            grid.add(textFields[i], 1, i);
        }

        VBox imageBox = new VBox(10);
        imageBox.getChildren().addAll(selectImageButton, selectedImageView);
        imageBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(imageBox, 1, 3);
    }

    private Button createSubmitButton(TextField[] textFields, ImageView selectedImageView) {
        Button submitButton = new Button("Add Category");
        submitButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;");
        submitButton.setPrefWidth(200);
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8;"));
        submitButton.setOnAction(e -> addCategory(textFields, selectedImageView));
        return submitButton;
    }

    public HBox createCategoryTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox sidebar = createSidebar();
        createCategoryTable();
        mainPane.getChildren().addAll(sidebar, categoryTable);
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
        Button addCategoryButton = createAddCategoryButton();

        sidebar.getChildren().addAll(actionLabel, searchField, addCategoryButton);
        return sidebar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search categories... (Press Enter)");
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        searchField.setPrefWidth(230);

        searchField.setOnAction(e -> performSearch(searchField.getText()));

        return searchField;
    }

    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            categoryTable.setItems(categoryList);
            categoryTable.refresh();
        } else {
            ObservableList<Category> filteredList = FXCollections.observableArrayList();
            String lowerSearchText = searchText.toLowerCase().trim();
            for (Category category : categoryList) {
                if (category.getCatName().toLowerCase().contains(lowerSearchText) ||
                        category.getDescription().toLowerCase().contains(lowerSearchText)) {
                    filteredList.add(category);
                }
            }
            categoryTable.setItems(filteredList);
            categoryTable.refresh();
        }
    }

    private Button createAddCategoryButton() {
        ImageView addIcon = new ImageView("addCategory.png");
        addIcon.setFitHeight(24);
        addIcon.setFitWidth(24);
        addIcon.setPreserveRatio(true);

        Button addButton = new Button("Add Category", addIcon);
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
            stage.setScene(new Scene(createAddCategoryPane(), 700, 600));
            stage.setResizable(false);
            stage.setTitle("Add New Category");
            stage.show();
        });

        return addButton;
    }

    private void createCategoryTable() {
        String[] columnNames = {"Category ID", "Category Name", "Category Description", "Total Products"};
        String[] propertyNames = {"catID", "catName", "description", "total_products"};
        TableColumn<Category, ?>[] tc = new TableColumn[columnNames.length];

        double minColWidth = screenWidth * 0.12;
        double colWidth = screenWidth * 0.18;
        double maxColWidth = screenWidth * 0.25;

        for (int i = 0; i < columnNames.length; i++) {
            tc[i] = new TableColumn<>(columnNames[i]);
            if (i == 0 || i == 3) {
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

        tc[2].setMinWidth(screenWidth * 0.18);
        tc[2].setPrefWidth(screenWidth * 0.25);
        tc[2].setMaxWidth(screenWidth * 0.35);

        TableColumn<Category, Void> actionColumn = createActionColumn();
        actionColumn.setMinWidth(220);
        actionColumn.setPrefWidth(250);
        actionColumn.setMaxWidth(280);

        categoryTable.getColumns().clear();
        categoryTable.getColumns().addAll(tc);
        categoryTable.getColumns().add(actionColumn);
        categoryTable.setItems(categoryList);
        categoryTable.setEditable(true);
        categoryTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        categoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        categoryTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindTableDimensions();
            }
        });

        setupTableEditing(tc);
    }

    private TableColumn<Category, Void> createActionColumn() {
        TableColumn<Category, Void> actionColumn = new TableColumn<>("Actions");
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

                b.setOnAction(event -> handleDeleteCategory());
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

            private void handleDeleteCategory() {
                Category category = getTableView().getItems().get(getIndex());
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Delete");
                confirmAlert.setHeaderText("Delete Category");
                confirmAlert.setContentText("Are you sure you want to delete " + category.getCatName() + "?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteCategoryFromDatabase(category);
                    }
                });
            }

            private void handleUpdateImage() {
                Category category = getTableView().getItems().get(getIndex());
                File file = fileChooserMethod("Choose New Category Image");
                if (file != null) {
                    try {
                        String url = file.toURI().toURL().toExternalForm();
                        String imageName = url.substring(url.lastIndexOf('/') + 1);

                        Connection con = MainApplication.dbConn.connectDB();
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate("UPDATE Category SET photoPath = '" + imageName + "' WHERE catID = " + category.getCatID());
                        stmt.close();
                        con.close();

                        MainApplication.readDB();
                        showAlert("Success", "Category image updated successfully!", Alert.AlertType.INFORMATION);
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

    private void bindTableDimensions() {
        categoryTable.prefWidthProperty().bind(categoryTable.getScene().widthProperty().subtract(280));
        categoryTable.prefHeightProperty().bind(categoryTable.getScene().heightProperty().subtract(100));
    }

    private void deleteCategoryFromDatabase(Category category) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            String sql1 = "UPDATE Category SET isActive = FALSE WHERE catID = " + category.getCatID();
            String sql2 = "UPDATE Product SET isActive = FALSE WHERE catID = " + category.getCatID();
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            MainApplication.readDB();
            showAlert("Success", category.getCatName() + " deactivated successfully!", Alert.AlertType.INFORMATION);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to deactivate category: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void setupTableEditing(TableColumn<Category, ?>[] tc) {
        ((TableColumn<Category, String>) tc[1]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Category, String>) tc[1]).setOnEditCommit(event -> {
            Category cat = event.getRowValue();
            if (isValidName(event.getNewValue())) {
                cat.setCatName(event.getNewValue());
                updateCategory(cat);
            } else {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setCatName(event.getOldValue());
                showAlert("Invalid Name", "Category name must contain only letters, numbers, and spaces", Alert.AlertType.WARNING);
            }
            event.getTableView().refresh();
        });

        ((TableColumn<Category, String>) tc[2]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Category, String>) tc[2]).setOnEditCommit(event -> {
            Category cat = event.getRowValue();
            String newDescription = event.getNewValue();
            if (newDescription != null && newDescription.length() <= 512) {
                cat.setDescription(newDescription);
                updateCategory(cat);
            } else {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setDescription(event.getOldValue());
                showAlert("Invalid Description", "Description must be less than 512 characters", Alert.AlertType.WARNING);
            }
            event.getTableView().refresh();
        });
    }

    private void updateCategory(Category cat) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String SQL = "UPDATE category SET " +
                    "catName = '" + cat.getCatName() + "', " +
                    "description = '" + cat.getDescription().replace("'", "''") + "' " +
                    "WHERE catID = " + cat.getCatID();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(SQL);
            showAlert("Success", "Category updated successfully!", Alert.AlertType.INFORMATION);
            MainApplication.readDB();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update category: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addCategory(TextField[] tf, ImageView selectedImageView) {
        if (!checkClearFields(tf)) {
            String categoryName = tf[0].getText();
            String description = tf[1].getText();
            String imagePath = (String) selectedImageView.getUserData();

            if (!isValidName(categoryName)) {
                showAlert("Validation Error", "Category name must contain only letters, numbers, and spaces", Alert.AlertType.WARNING);
            } else if (description.length() > 512) {
                showAlert("Validation Error", "Description must be less than 512 characters", Alert.AlertType.WARNING);
            } else {
                try {
                    Connection con = MainApplication.dbConn.connectDB();
                    Statement stmt = con.createStatement();

                    String sqlCategory;
                    if (imagePath != null && !imagePath.isEmpty()) {
                        sqlCategory = "INSERT INTO Category (catName, description, photoPath) VALUES ('"
                                + categoryName + "', '" + description.replace("'", "''") + "', '" + imagePath + "')";
                    } else {
                        sqlCategory = "INSERT INTO Category (catName, description) VALUES ('"
                                + categoryName + "', '" + description.replace("'", "''") + "')";
                    }

                    stmt.executeUpdate(sqlCategory);
                    stmt.close();
                    MainApplication.readDB();

                    for (TextField field : tf) {
                        field.clear();
                    }
                    selectedImageView.setImage(new Image("noProd.png"));
                    selectedImageView.setUserData(null);

                    showAlert("Success", "Category added to database!", Alert.AlertType.INFORMATION);
                } catch (SQLException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                    showAlert("Error", "Database error: " + e1.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Validation Error", "Please fill all required fields", Alert.AlertType.WARNING);
        }
    }

    private boolean checkClearFields(TextField[] tf) {
        for (TextField field : tf) {
            if (field.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane addPane() {
        return createAddCategoryPane();
    }

    public HBox categoryTablePane() {
        return createCategoryTablePane();
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z0-9\\s]+");
    }
}