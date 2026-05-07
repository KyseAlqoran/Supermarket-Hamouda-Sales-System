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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeFX {
    public static ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    public static TableView<Employee> employeeTable = new TableView<>(employeeList);
    private double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    public BorderPane createAddEmployeePane() {
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        mainPane.setPadding(new Insets(30));

        Label headerLabel = new Label("Add New Employee");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        VBox header = new VBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        GridPane formGrid = createEmployeeForm();

        VBox cardContainer = new VBox(header, formGrid);
        cardContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        cardContainer.setPadding(new Insets(30));
        cardContainer.setMaxWidth(600);

        mainPane.setCenter(cardContainer);
        return mainPane;
    }

    private GridPane createEmployeeForm() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        String[] labelTexts = {"First Name:", "Last Name:", "Salary:", "Hire Date:", "Phone Number:", "Branch:", "Role:"};
        Label[] labels = new Label[labelTexts.length];
        TextField[] textFields = new TextField[4]; // Reduced from 5 to 4
        DatePicker datePicker = new DatePicker();
        ComboBox<String> branchComboBox = new ComboBox<>();
        ComboBox<String> roleComboBox = new ComboBox<>();

        initializeFormComponents(labels, labelTexts, textFields, datePicker, branchComboBox, roleComboBox);
        addComponentsToGrid(grid, labels, textFields, datePicker, branchComboBox, roleComboBox);

        Button submitButton = createSubmitButton(textFields, branchComboBox, datePicker, roleComboBox);
        grid.add(submitButton, 1, 7);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        return grid;
    }

    private void initializeFormComponents(Label[] labels, String[] labelTexts, TextField[] textFields,
                                          DatePicker datePicker, ComboBox<String> branchComboBox, ComboBox<String> roleComboBox) {
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

        branchComboBox.getItems().addAll("Hamouda Supermarket 1", "Hamouda Supermarket 2");
        branchComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        branchComboBox.setPrefWidth(300);

        roleComboBox.getItems().addAll("Admin", "Employee");
        roleComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        roleComboBox.setPrefWidth(300);
    }

    private void addComponentsToGrid(GridPane grid, Label[] labels, TextField[] textFields,
                                     DatePicker datePicker, ComboBox<String> branchComboBox, ComboBox<String> roleComboBox) {
        for (int i = 0; i < 3; i++) {
            grid.add(labels[i], 0, i);
            grid.add(textFields[i], 1, i);
        }
        grid.add(labels[3], 0, 3);
        grid.add(datePicker, 1, 3);
        grid.add(labels[4], 0, 4);
        grid.add(textFields[3], 1, 4); // Moved phone number to index 3
        grid.add(labels[5], 0, 5);
        grid.add(branchComboBox, 1, 5);
        grid.add(labels[6], 0, 6);
        grid.add(roleComboBox, 1, 6);
    }

    private Button createSubmitButton(TextField[] textFields, ComboBox<String> branchComboBox,
                                      DatePicker datePicker, ComboBox<String> roleComboBox) {
        Button submitButton = new Button("Add Employee");
        submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        submitButton.setPrefWidth(200);
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #229954; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        submitButton.setOnAction(e -> addEmployee(textFields, branchComboBox, datePicker, roleComboBox));
        return submitButton;
    }

    public HBox createEmployeeTablePane() {
        HBox mainPane = new HBox(15);
        mainPane.setStyle("-fx-background-color: #ecf0f1;");
        VBox sidebar = createSidebar();
        createEmployeeTable();
        mainPane.getChildren().addAll(sidebar, employeeTable);
        mainPane.setAlignment(Pos.CENTER);
        return mainPane;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");
        sidebar.setPrefWidth(650);
        sidebar.setPadding(new Insets(30));
        Label actionLabel = new Label("Actions");
        actionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        TextField searchField = createSearchField();
        Button addEmployeeButton = createAddEmployeeButton();
        sidebar.getChildren().addAll(actionLabel, searchField, addEmployeeButton);
        return sidebar;
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search employees... (Press Enter)");
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-color: white;");
        searchField.setPrefWidth(250);
        searchField.setOnAction(e -> performSearch(searchField.getText()));
        return searchField;
    }

    private void performSearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            employeeTable.setItems(employeeList);
            employeeTable.refresh();
        } else {
            ObservableList<Employee> filteredList = FXCollections.observableArrayList();
            String lowerSearchText = searchText.toLowerCase().trim();
            for (Employee emp : employeeList) {
                if (String.valueOf(emp.getpID()).contains(lowerSearchText) ||
                        emp.getpFirstName().toLowerCase().contains(lowerSearchText) ||
                        emp.getpLastName().toLowerCase().contains(lowerSearchText) ||
                        emp.getpPhoneNumber().contains(lowerSearchText) ||
                        emp.getBranchName().toLowerCase().contains(lowerSearchText)) {
                    filteredList.add(emp);
                }
            }
            employeeTable.setItems(filteredList);
            employeeTable.refresh();
        }
    }

    private Button createAddEmployeeButton() {
        ImageView addIcon = new ImageView(getClass().getResource("/addEmployee.png").toExternalForm());
        addIcon.setFitHeight(24);
        addIcon.setFitWidth(24);
        addIcon.setPreserveRatio(true);
        Button addButton = new Button("Add Employee", addIcon);
        addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        addButton.setPrefWidth(250);
        addButton.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
        addButton.setOnAction(e -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(createAddEmployeePane(), 700, 700));
            stage.setResizable(false);
            stage.setTitle("Add New Employee");
            stage.show();
        });
        return addButton;
    }

    private void createEmployeeTable() {
        String[] columnNames = {"ID", "First Name", "Last Name", "Salary", "Hire Date", "Phone Number", "Branch"};
        String[] propertyNames = {"pID", "pFirstName", "pLastName", "employeeSalary", "employeeHireDate", "PhoneNumber", "branchName"};
        TableColumn<Employee, ?>[] tc = new TableColumn[columnNames.length];
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

        tc[0] = new TableColumn<>("ID");
        tc[3] = new TableColumn<>("Salary");
        tc[0].setCellValueFactory(new PropertyValueFactory<>("pID"));
        tc[3].setCellValueFactory(new PropertyValueFactory<>("employeeSalary"));
        tc[0].setStyle("-fx-alignment: CENTER; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        tc[3].setStyle("-fx-alignment: CENTER; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        tc[0].setPrefWidth(80);
        tc[0].setMinWidth(60);
        tc[0].setMaxWidth(100);
        tc[3].setPrefWidth(100);
        tc[3].setMinWidth(80);
        tc[3].setMaxWidth(120);

        TableColumn<Employee, Void> deleteColumn = createDeleteColumn();
        deleteColumn.setMinWidth(200);
        deleteColumn.setPrefWidth(230);
        deleteColumn.setMaxWidth(250);

        employeeTable.getColumns().clear();
        employeeTable.getColumns().addAll(tc);
        employeeTable.getColumns().add(deleteColumn);
        employeeTable.setItems(employeeList);
        employeeTable.setEditable(true);
        employeeTable.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        employeeTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                bindTableDimensions();
            }
        });

        setupTableEditing(tc);
    }

    private void bindTableDimensions() {
        employeeTable.prefWidthProperty().bind(employeeTable.getScene().widthProperty().subtract(250));
        employeeTable.prefHeightProperty().bind(employeeTable.getScene().heightProperty().subtract(100));
    }

    private TableColumn<Employee, Void> createDeleteColumn() {
        TableColumn<Employee, Void> deleteColumn = new TableColumn<>("Actions");
        deleteColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 12px; -fx-text-fill: #2c3e50; -fx-background-color: #ffffff;");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = createDeleteButton();

            private Button createDeleteButton() {
                ImageView deleteIcon = new ImageView(getClass().getResource("/rmEmployee.png").toExternalForm());
                deleteIcon.setFitHeight(20);
                deleteIcon.setFitWidth(20);
                deleteIcon.setPreserveRatio(true);
                Button btn = new Button("Deactivate", deleteIcon);
                btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
                btn.setPrefWidth(120);
                btn.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);"));
                btn.setOnAction(event -> handleDeactivateEmployee());
                return btn;
            }

            private void handleDeactivateEmployee() {
                Employee employee = getTableView().getItems().get(getIndex());
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deactivation");
                confirmAlert.setHeaderText("Deactivate Employee");
                confirmAlert.setContentText("Are you sure you want to deactivate " + employee.getpFirstName() + " " + employee.getpLastName() + "?");
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deactivateEmployee(employee);
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

    private void deactivateEmployee(Employee employee) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            try {
                Statement stmt = con.createStatement();
                String updateEmployeeSQL = "UPDATE Employee SET isActive = false WHERE eID = " + employee.getPID();
                stmt.executeUpdate(updateEmployeeSQL);
                String updateUserSQL = "UPDATE User SET activeStatus = false WHERE pID = " + employee.getPID();
                stmt.executeUpdate(updateUserSQL);
                MainApplication.readDB();
                showAlert("Success", "Employee deactivated successfully!", Alert.AlertType.INFORMATION);
            } catch (SQLException ex) {
                con.rollback();
                showAlert("Error", "Failed to deactivate employee: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            showAlert("Error", "Database connection error.", Alert.AlertType.ERROR);
        }
    }

    private void setupTableEditing(TableColumn<Employee, ?>[] tc) {
        ((TableColumn<Employee, String>) tc[1]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Employee, String>) tc[1]).setOnEditCommit(event -> {
            Employee emp = event.getRowValue();
            if (Employee.isValidName(event.getNewValue())) {
                emp.setpFirstName(event.getNewValue());
                updateEmployee(emp);
            } else {
                revertTableEdit(event, event.getOldValue());
                showAlert("Invalid Name", "Name must contain only letters.", Alert.AlertType.WARNING);
            }
        });

        ((TableColumn<Employee, String>) tc[2]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Employee, String>) tc[2]).setOnEditCommit(event -> {
            Employee emp = event.getRowValue();
            if (Employee.isValidName(event.getNewValue())) {
                emp.setpLastName(event.getNewValue());
                updateEmployee(emp);
            } else {
                revertTableEdit(event, event.getOldValue());
                showAlert("Invalid Name", "Name must contain only letters.", Alert.AlertType.WARNING);
            }
        });

        ((TableColumn<Employee, Double>) tc[3]).setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        ((TableColumn<Employee, Double>) tc[3]).setOnEditCommit(event -> {
            Employee emp = event.getRowValue();
            if (Employee.isValidSalary(event.getNewValue())) {
                emp.setEmployeeSalary(event.getNewValue());
                updateEmployee(emp);
            } else {
                revertSalaryEdit(event, event.getOldValue());
                showAlert("Invalid Salary", "Salary must be greater than 0.", Alert.AlertType.WARNING);
            }
        });

        ((TableColumn<Employee, String>) tc[5]).setCellFactory(TextFieldTableCell.forTableColumn());
        ((TableColumn<Employee, String>) tc[5]).setOnEditCommit(event -> {
            Employee emp = event.getRowValue();
            if (Employee.isValidPhoneNumber(event.getNewValue())) {
                emp.setpPhoneNumber(event.getNewValue());
                updateEmployee(emp);
            } else {
                revertTableEdit(event, event.getOldValue());
                showAlert("Invalid Phone Number", "Phone number must start with 059 or 056 and contain 10 digits.", Alert.AlertType.WARNING);
            }
        });
    }

    private void revertTableEdit(TableColumn.CellEditEvent<Employee, String> event, String oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setpFirstName(oldValue);
        event.getTableView().refresh();
    }

    private void revertSalaryEdit(TableColumn.CellEditEvent<Employee, Double> event, Double oldValue) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setEmployeeSalary(oldValue);
        event.getTableView().refresh();
    }

    private void updateEmployee(Employee emp) {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            String updatePersonSQL = "UPDATE person SET " +
                    "pFirstName = '" + emp.getpFirstName() + "', " +
                    "pLastName = '" + emp.getpLastName() + "', " +
                    "pPhoneNumber = '" + emp.getpPhoneNumber() + "' " +
                    "WHERE pID = " + emp.getPID();

            String updateEmployeeSQL = "UPDATE employee SET " +
                    "esalary = " + emp.getEmployeeSalary() + ", " +
                    "ehiredate = '" + emp.getEmployeeHireDate() + "' " +
                    "WHERE eID = " + emp.getPID();

            Statement stmt = con.createStatement();
            stmt.executeUpdate(updatePersonSQL);
            stmt.executeUpdate(updateEmployeeSQL);

            showAlert("Success", "Employee updated successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to update employee: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void addEmployee(TextField[] textFields, ComboBox<String> branchComboBox,
                             DatePicker datePicker, ComboBox<String> roleComboBox) {
        if (validateFormFields(textFields, branchComboBox, datePicker, roleComboBox)) {
            try {
                String firstName = textFields[0].getText().trim();
                String lastName = textFields[1].getText().trim();
                double salary = Double.parseDouble(textFields[2].getText().trim());
                String hireDate = datePicker.getValue().toString();
                String phoneNumber = textFields[3].getText().trim();
                String branchName = branchComboBox.getValue();
                String role = roleComboBox.getValue();
                int branchId = getBranchId(branchName);
                addEmployee(firstName, lastName, salary, hireDate, phoneNumber, branchId, role);
                clearForm(textFields, branchComboBox, datePicker, roleComboBox);
                showAlert("Success", "Employee added successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add employee: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validateFormFields(TextField[] textFields, ComboBox<String> branchComboBox,
                                       DatePicker datePicker, ComboBox<String> roleComboBox) {
        for (TextField tf : textFields) {
            if (tf == null) continue;
            if (tf.getText().trim().isEmpty()) {
                showAlert("Validation Error", "Please fill in all required fields.", Alert.AlertType.WARNING);
                return false;
            }
        }

        if (datePicker.getValue() == null || branchComboBox.getValue() == null || roleComboBox.getValue() == null) {
            showAlert("Validation Error", "Please fill in all required fields.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Employee.isValidName(textFields[0].getText())) {
            showAlert("Validation Error", "First name must contain only letters.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Employee.isValidName(textFields[1].getText())) {
            showAlert("Validation Error", "Last name must contain only letters.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            double salary = Double.parseDouble(textFields[2].getText());
            if (!Employee.isValidSalary(salary)) {
                showAlert("Validation Error", "Salary must be a positive number.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid salary amount.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Employee.isValidPhoneNumber(textFields[3].getText())) {
            showAlert("Validation Error", "Phone number must start with 059 or 056 and contain 10 digits.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private int getBranchId(String branchName) {
        return "Hamouda Supermarket 1".equals(branchName) ? 1 : 2;
    }

    private void addEmployee(String firstName, String lastName, double salary, String hireDate,
                             String phoneNumber, int branchId, String accountType) throws SQLException, ClassNotFoundException {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            String insertPersonSQL = "INSERT INTO Person (pFirstName, pLastName, pPhoneNumber, pEmail) " +
                    "VALUES ('" + firstName + "', '" + lastName + "', '" + phoneNumber + "', '')";
            stmt.executeUpdate(insertPersonSQL);
            ResultSet rs = stmt.executeQuery("SELECT MAX(pID) FROM Person");
            int newPersonID = -1;
            if (rs.next()) newPersonID = rs.getInt(1);
            rs.close();

            String insertEmployeeSQL = "INSERT INTO Employee (eID, esalary, ehiredate, bID, isActive) " +
                    "VALUES (" + newPersonID + ", " + salary + ", '" + hireDate + "', " + branchId + ", true)";
            stmt.executeUpdate(insertEmployeeSQL);

            String username = firstName.toLowerCase() + "_" + lastName.toLowerCase();
            String password = username + branchId;
            String insertUserSQL = "INSERT INTO User (uname, password, activeStatus, pID, accountType) " +
                    "VALUES ('" + username + "', '" + password + "', true, " + newPersonID + ", '" + accountType + "')";
            stmt.executeUpdate(insertUserSQL);

            MainApplication.readDB();
        }
    }

    private void clearForm(TextField[] textFields, ComboBox<String> branchComboBox,
                           DatePicker datePicker, ComboBox<String> roleComboBox) {
        for (TextField tf : textFields) {
            tf.clear();
        }
        datePicker.setValue(null);
        branchComboBox.getSelectionModel().clearSelection();
        roleComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public HBox employeeTablePane() {
        return createEmployeeTablePane();
    }
}