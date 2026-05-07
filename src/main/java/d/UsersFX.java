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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Screen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersFX {
    public static ObservableList<User> userList = FXCollections.observableArrayList();
    public static TableView<User> userTable = new TableView<>(userList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    public BorderPane addUserPane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));
        Label l = new Label("Add New User");
        l.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox vb1 = new VBox(l);
        vb1.setAlignment(Pos.CENTER);
        vb1.setPadding(new Insets(0, 0, 20, 0));
        GridPane gp = setupAdd();
        VBox vb2 = new VBox(vb1, gp);
        vb2.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        vb2.setPadding(new Insets(30));
        vb2.setMaxWidth(600);
        mainPane.setCenter(vb2);
        return mainPane;
    }

    private GridPane setupAdd() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        String[] lt = {"User Name:", "Password:", "Account Type:", "Active Status:", "Person ID:"};
        Label[] l = new Label[lt.length];
        TextField[] tf = new TextField[2];
        ComboBox<String> cb1 = new ComboBox<>();
        CheckBox cb2 = new CheckBox();
        ComboBox<Integer> cb3 = new ComboBox<>();
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT pID FROM Person WHERE pID NOT IN (SELECT pID FROM User)")) {
            while (rs.next()) {
                cb3.getItems().add(rs.getInt("pID"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        setStyle(l, lt, tf, cb1, cb2, cb3);
        addToGrid(grid, l, tf, cb1, cb2, cb3);
        Button b = addButton(tf, cb1, cb2, cb3);
        grid.add(b, 1, 5);
        GridPane.setHalignment(b, HPos.CENTER);
        return grid;
    }

    private void setStyle(Label[] l, String[] lt, TextField[] tf, ComboBox<String> cb1, CheckBox cb2, ComboBox<Integer> cb3) {
        for (int i = 0; i < l.length; i++) {
            l[i] = new Label(lt[i]);
            l[i].setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        }
        for (int i = 0; i < tf.length; i++) {
            tf[i] = new TextField();
            tf[i].setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
            tf[i].setPrefWidth(300);
        }
        cb1.getItems().addAll("Admin", "Employee", "Customer");
        cb1.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        cb1.setPrefWidth(300);
        cb2.setStyle("-fx-font-size: 14px; -fx-padding: 12;");
        cb3.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        cb3.setPrefWidth(300);
    }

    private void addToGrid(GridPane gp, Label[] l, TextField[] tf, ComboBox<String> cb1, CheckBox cb2, ComboBox<Integer> cb3) {
        for (int i = 0; i < 2; i++) {
            gp.add(l[i], 0, i);
            gp.add(tf[i], 1, i);
        }
        gp.add(l[2], 0, 2);
        gp.add(cb1, 1, 2);
        gp.add(l[3], 0, 3);
        gp.add(cb2, 1, 3);
        gp.add(l[4], 0, 4);
        gp.add(cb3, 1, 4);
    }

    private Button addButton(TextField[] tf, ComboBox<String> cb1, CheckBox cb2, ComboBox<Integer> cb3) {
        Button b = new Button("Add User");
        b.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        b.setPrefWidth(200);
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        b.setOnAction(e -> addUser(tf, cb1, cb2, cb3));
        return b;
    }

    private HBox createUserTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox sidebar = createSidebar();
        createUserTable();
        mainPane.getChildren().addAll(sidebar, userTable);
        mainPane.setAlignment(Pos.CENTER);
        return mainPane;
    }

    private VBox createSidebar() {
        VBox vb = new VBox(15);
        vb.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");
        vb.setPrefWidth(400);
        vb.setPadding(new Insets(30));
        Label l = new Label("Actions");
        l.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField searchField = createSearchField();
        Button b = createAddUserButton();
        vb.getChildren().addAll(l, searchField, b);
        return vb;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search users... (Press Enter)");
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        searchField.setPrefWidth(230);
        searchField.setOnAction(e -> performSearch(searchField.getText()));
        return searchField;
    }

    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            userTable.setItems(userList);
            userTable.refresh();
        } else {
            ObservableList<User> filteredList = FXCollections.observableArrayList();
            String lowerSearchText = searchText.toLowerCase().trim();
            for (User user : userList) {
                if (user.getName().toLowerCase().contains(lowerSearchText) ||
                        user.getAccountType().toLowerCase().contains(lowerSearchText) ||
                        (user.isActiveStatus() ? "Active" : "Inactive").toLowerCase().contains(lowerSearchText)) {
                    filteredList.add(user);
                }
            }
            userTable.setItems(filteredList);
            userTable.refresh();
        }
    }

    private Button createAddUserButton() {
        ImageView icon = new ImageView(getClass().getResource("/addUser.png").toExternalForm());
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        icon.setPreserveRatio(true);
        Button addButton = new Button("Add User", icon);
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        addButton.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        addButton.setPrefWidth(180);
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        addButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(addUserPane(), 650, 600));
            stage.setResizable(false);
            stage.setTitle("Add New User");
            stage.show();
        });
        return addButton;
    }

    private void createUserTable() {
        String[] names = {"User ID", "Username", "Password", "Account Type", "Active Status"};
        String[] getters = {"id", "name", "password", "accountType", "activeStatus"};
        TableColumn<User, ?>[] tc = new TableColumn[getters.length];
        double colWidth = Math.max(80, screenWidth * 0.08);
        double minColWidth = screenWidth * 0.06;
        double maxColWidth = screenWidth * 0.12;
        for (int i = 0; i < getters.length; i++) {
            tc[i] = new TableColumn<>(names[i]);
            tc[i].setCellValueFactory(new PropertyValueFactory<>(getters[i]));
            tc[i].setStyle("-fx-alignment: CENTER; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
            if (i == 0) {
                tc[i].setMinWidth(60);
                tc[i].setPrefWidth(80);
                tc[i].setMaxWidth(100);
            } else {
                tc[i].setMinWidth(minColWidth);
                tc[i].setPrefWidth(colWidth);
                tc[i].setMaxWidth(maxColWidth);
            }
        }
        TableColumn<User, Void> deleteColumn = createDeleteColumn();
        deleteColumn.setMinWidth(200);
        deleteColumn.setPrefWidth(230);
        deleteColumn.setMaxWidth(250);
        userTable.getColumns().clear();
        userTable.getColumns().addAll(tc);
        userTable.getColumns().add(deleteColumn);
        userTable.setItems(userList);
        userTable.setEditable(true);
        userTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setupTableEditing(tc);
    }

    private TableColumn<User, Void> createDeleteColumn() {
        TableColumn<User, Void> deleteColumn = new TableColumn<>("Actions");
        deleteColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createDeleteButton();

            private Button createDeleteButton() {
                ImageView icon = new ImageView(getClass().getResource("/rmUser.png").toExternalForm());
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                icon.setPreserveRatio(true);
                Button b = new Button("Delete", icon);
                b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
                b.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
                b.setPrefWidth(120);
                b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
                b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
                b.setOnAction(event -> handleDeleteUser());
                return b;
            }

            private void handleDeleteUser() {
                User user = getTableView().getItems().get(getIndex());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Delete User");
                alert.setContentText("Are you sure you want to delete user: " + user.getName() + "?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteFromDatabase(user);
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

    private void setupTableEditing(TableColumn<User, ?>[] tc) {
        // Username column - editable text field
        ((TableColumn<User, String>) tc[1]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<User, String>) tc[1]).setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setName(event.getNewValue());
            updateUser(user);
        });

        // Password column - editable text field
        ((TableColumn<User, String>) tc[2]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<User, String>) tc[2]).setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setPassword(event.getNewValue());
            updateUser(user);
        });

        // Account Type column - ComboBox with predefined values
        ObservableList<String> accountTypes = FXCollections.observableArrayList("Admin", "Employee", "Customer");
        ((TableColumn<User, String>) tc[3]).setCellFactory(ComboBoxTableCell.forTableColumn(accountTypes));
        ((TableColumn<User, String>) tc[3]).setOnEditCommit(event -> {
            User user = event.getRowValue();
            user.setAccountType(event.getNewValue());
            updateUser(user);
        });

        // Active Status column - CheckBox
        ((TableColumn<User, Boolean>) tc[4]).setCellFactory(param -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    user.setActiveStatus(checkBox.isSelected());
                    updateUser(user);
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                }
            }
        });
    }

    private void updateUser(User user) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String updateSQL = "UPDATE User SET uname = '" + user.getName() +
                    "', password = '" + user.getPassword() +
                    "', accountType = '" + user.getAccountType() +
                    "', activeStatus = " + user.isActiveStatus() +
                    " WHERE uID = " + user.getId();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updateSQL);
            MainApplication.readDB();
            showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update user: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteFromDatabase(User user) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            try {
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DELETE FROM User WHERE uID = " + user.getId());
                MainApplication.readDB();
                showAlert("Success", "User " + user.getName() + " deleted successfully!", Alert.AlertType.INFORMATION);
            } catch (SQLException ex) {
                showAlert("Error", "Failed to delete user: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            showAlert("Error", "Database connection error.", Alert.AlertType.ERROR);
        }
    }

    private void addUser(TextField[] textFields, ComboBox<String> roleComboBox, CheckBox activeStatusCheckBox, ComboBox<Integer> personID) {
        if (validateUser(textFields, roleComboBox, personID)) {
            try {
                String username = textFields[0].getText().trim();
                String password = textFields[1].getText().trim();
                String accountType = roleComboBox.getValue();
                boolean activeStatus = activeStatusCheckBox.isSelected();
                int pID = personID.getValue();
                addUser(username, password, accountType, activeStatus, pID);
                clear(textFields, roleComboBox, activeStatusCheckBox, personID);
                showAlert("Success", "User added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add user: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validateUser(TextField[] tf, ComboBox<String> cb1, ComboBox<Integer> cb3) {
        for (TextField t : tf) {
            if (t.getText().trim().isEmpty()) {
                showAlert("Validation Error", "Please Fill In All Required Fields.", Alert.AlertType.WARNING);
                return false;
            }
        }
        if (cb1.getValue() == null || cb3.getValue() == null) {
            showAlert("Validation Error", "Please Fill In All Required Fields.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void addUser(String username, String password, String accountType, boolean activeStatus, int pID) throws SQLException, ClassNotFoundException {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            String insertUserSQL = "INSERT INTO User (uname, password, accountType, activeStatus, pID) VALUES ('" +
                    username + "', '" + password + "', '" + accountType + "', " + activeStatus + ", " + pID + ")";
            stmt.executeUpdate(insertUserSQL);
            MainApplication.readDB();
        }
    }

    private void clear(TextField[] tf, ComboBox<String> cb1, CheckBox cb2, ComboBox<Integer> cb3) {
        for (TextField t : tf) t.clear();
        cb1.getSelectionModel().clearSelection();
        cb2.setSelected(false);
        cb3.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public HBox userTablePane() {
        return createUserTablePane();
    }
}