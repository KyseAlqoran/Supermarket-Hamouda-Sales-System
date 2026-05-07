package d.demo6;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Analytics {
    private String[] queries = {
            "SELECT p.pFirstName AS First_Name, p.pLastName AS Last_Name, e.esalary AS Salary FROM Employee e JOIN Person p ON e.eID = p.pID WHERE e.esalary = (SELECT MAX(esalary) FROM Employee WHERE isActive = TRUE) AND e.isActive = TRUE",
            "SELECT p.pFirstName AS First_Name, p.pLastName AS Last_Name, e.esalary AS Salary FROM Employee e JOIN Person p ON e.eID = p.pID WHERE e.isActive = TRUE ORDER BY e.esalary DESC",
            "SELECT p.prodName AS Product_Name, p.quantity AS Quantity FROM Product p WHERE p.quantity < 50 AND p.isActive = TRUE",
            "SELECT p.prodName AS Product_Name, MAX(sup.supDate) AS Latest_Supply_Date FROM Supply sup JOIN Product p ON sup.prodID = p.prodID WHERE p.isActive = TRUE GROUP BY p.prodName",
            "SELECT s.sName AS Supplier_Name, COUNT(sup.supID) AS Supply_Count FROM Supply sup JOIN Supplier s ON sup.sID = s.sID WHERE s.isActive = TRUE GROUP BY s.sName",
            "SELECT p.prodName AS Product_Name, p.price AS Price FROM Product p WHERE p.isActive = TRUE ORDER BY p.price DESC",
            "SELECT p.prodName AS Product_Name, SUM(od.quantity) AS Quantity FROM Order_Detail od JOIN Product p ON od.prodID = p.prodID WHERE p.isActive = TRUE GROUP BY p.prodName ORDER BY Quantity DESC",
            "SELECT p.prodName AS Product_Name, SUM(p.quantity) AS Quantity FROM Product p WHERE p.isActive = TRUE GROUP BY p.prodName",
            "SELECT p.prodName AS Product_Name, p.price AS Price FROM Product p WHERE p.price > 30 AND p.isActive = TRUE",
            "SELECT p.prodName AS Product_Name, p.expire_Date AS Expiry_Date FROM Product p WHERE YEAR(p.expire_Date) = " + LocalDate.now().getYear() + " AND p.isActive = TRUE",
            "SELECT b.bName AS Branch_Name, COUNT(s.saleID) AS Sale_Count FROM Sale s JOIN Branch b ON s.bID = b.bID GROUP BY b.bName",
            "SELECT p.pFirstName AS First_Name, p.pLastName AS Last_Name FROM Person p JOIN Sale s ON p.pID = s.pID WHERE YEAR(s.sale_date) = ",
            "SELECT w.wLocation AS Warehouse_Location, SUM(p.quantity) AS Total_Quantity FROM Warehouse w JOIN Product p ON w.wID = p.wID WHERE p.isActive = TRUE GROUP BY w.wLocation",
            "SELECT p.pFirstName AS First_Name, p.pLastName AS Last_Name, COUNT(s.saleID) AS Sale_Count FROM Person p JOIN Sale s ON p.pID = s.pID GROUP BY p.pFirstName, p.pLastName ORDER BY Sale_Count DESC",
            "SELECT s.sName AS Supplier_Name, SUM(sup.quantity) AS Total_Quantity FROM Supplier s JOIN Supply sup ON s.sID = sup.sID WHERE s.isActive = TRUE GROUP BY s.sName",
            "SELECT o.oDate AS Order_Date, o.status AS Status FROM Orders o WHERE YEAR(o.oDate) = " + LocalDate.now().getYear() + " AND o.isActive = TRUE",
            "SELECT p.prodName AS Product_Name FROM Product p WHERE p.expire_Date < '" + LocalDate.now() + "' AND p.isActive = TRUE",
            "SELECT COUNT(o.oID) AS Order_Count FROM Orders o WHERE o.status = TRUE AND o.isActive = TRUE",
            "SELECT p.pFirstName AS First_Name, p.pLastName AS Last_Name FROM Person p JOIN User u ON p.pID = u.pID WHERE u.activeStatus = TRUE",
            "SELECT sd.saleID AS Sale_ID, SUM((sd.unitPrice * sd.quantity) * (1 - sd.discount/100)) AS Total_Amount FROM Sale_Details sd GROUP BY sd.saleID"
    };

    private String[] descriptions = {
            "Employee with Highest Salary",
            "Employees Sorted by Salary",
            "Products with Low Quantity ( Less Than 50 )",
            "Last Restocking Date per Product",
            "Total Purchases by Supplier",
            "Products Sorted by Price",
            "Total Quantity Ordered per Product",
            "Total Stock Quantity per Product",
            "Products with price greater than 30",
            "Products expiring in " + LocalDate.now().getYear(),
            "Number of sales per branch",
            "Persons who made sales in specific year",
            "Total quantity of products per warehouse location",
            "Number of sales per person",
            "Total supplied quantity per supplier",
            "Orders placed in the year " + LocalDate.now().getYear(),
            "Products expired before today",
            "Number of orders with status TRUE",
            "Active users",
            "Total revenue per sale"
    };

    private String[] categories = {
            "Employees",
            "Employees",
            "Products & Inventory",
            "Products & Inventory",
            "Sales & Revenue",
            "Employees",
            "Sales & Revenue",
            "Sales & Revenue",
            "Products & Inventory",
            "Products & Inventory",
            "Sales & Revenue",
            "Sales & Revenue",
            "Employees",
            "Employees",
            "Products & Inventory",
            "Products & Inventory",
            "Supply Chain",
            "Supply Chain",
            "Products & Inventory",
            "Company Info",
            "Sales & Revenue"
    };
    private final int[] dynamicQueryIndices = {11};
    private TextArea resultArea;
    private Label statusLabel;
    private ProgressIndicator progressIndicator;
    private TabPane tabPane;
    private TextField searchField;
    private String currentSearchTerm = "";

    public ScrollPane analyticsProbe() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");

        Label headerLabel = new Label("📊 Business Analytics Dashboard");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchSection = createSearchSection();
        tabPane = createCategoryTabs();
        VBox resultsSection = createResultsSection();

        root.getChildren().addAll(headerLabel, searchSection, tabPane, resultsSection);

        ScrollPane mainScrollPane = new ScrollPane(root);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setFitToHeight(true);
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainScrollPane.setVvalue(0.0);
        mainScrollPane.setPannable(true);

        return mainScrollPane;
    }

    private HBox createSearchSection() {
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(15));
        searchBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-color: #e9ecef; -fx-border-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 3, 0, 0, 2);");

        Label searchLabel = new Label("🔍 Search Reports:");
        searchLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #495057; -fx-font-size: 14px;");

        searchField = new TextField();
        searchField.setPromptText("Type to search analytics reports...");
        searchField.setPrefWidth(350);
        searchField.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-border-color: #ced4da; -fx-padding: 8 12; -fx-font-size: 13px;");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            currentSearchTerm = newValue.toLowerCase();
            updateTabsWithSearch();
        });

        Button clearButton = new Button("Clear");
        clearButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 8 16; -fx-font-weight: bold;");
        clearButton.setOnAction(e -> {
            searchField.clear();
            currentSearchTerm = "";
            updateTabsWithSearch();
        });

        searchBox.getChildren().addAll(searchLabel, searchField, clearButton);
        return searchBox;
    }

    private TabPane createCategoryTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefHeight(500);
        tabPane.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-border-color: #e9ecef; -fx-border-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 3, 0, 0, 2);");

        Tab allTab = createTab("All Reports", -1);
        Tab productsTab = createTab("📦 Products & Inventory", 0);
        Tab salesTab = createTab("💰 Sales & Revenue", 1);
        Tab employeesTab = createTab("👥 Employees", 2);
        Tab supplyTab = createTab("🚚 Supply Chain", 3);
        Tab companyTab = createTab("🏢 Company Info", 4);
        Tab customerTab = createTab("👤 Customer Data", 5);
        Tab ordersTab = createTab("📋 Orders", 6);

        List<Tab> tabs = List.of(allTab, productsTab, salesTab, employeesTab, supplyTab, companyTab, customerTab, ordersTab);
        for (Tab tab : tabs) {
            tab.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8 8 0 0;");
        }

        tabPane.getTabs().addAll(tabs);
        tabPane.lookupAll(".tab-header-background").forEach(node -> {
            node.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10 10 0 0;");
        });

        return tabPane;
    }

    private Tab createTab(String tabName, int categoryFilter) {
        Tab tab = new Tab(tabName);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white; -fx-background-radius: 0 0 10 10;");

        tab.setUserData(new TabData(content, categoryFilter));
        updateTabContent(content, categoryFilter, "");

        scrollPane.setContent(content);
        tab.setContent(scrollPane);

        return tab;
    }

    private void updateTabsWithSearch() {
        for (Tab tab : tabPane.getTabs()) {
            TabData tabData = (TabData) tab.getUserData();
            if (tabData != null) {
                updateTabContent(tabData.content, tabData.categoryFilter, currentSearchTerm);
            }
        }
    }

    private void updateTabContent(VBox content, int categoryFilter, String searchTerm) {
        content.getChildren().clear();
        boolean hasResults = false;

        for (int i = 0; i < descriptions.length; i++) {
            if (shouldIncludeInCategory(i, categoryFilter) && matchesSearch(i, searchTerm)) {
                content.getChildren().add(createReportButton(i));
                hasResults = true;
            }
        }

        if (!hasResults && !searchTerm.isEmpty()) {
            Label noResultsLabel = new Label("🔍 No reports found matching \"" + searchTerm + "\"");
            noResultsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; " +
                    "-fx-padding: 20; -fx-font-style: italic;");
            content.getChildren().add(noResultsLabel);
        }
    }

    private boolean matchesSearch(int index, String searchTerm) {
        if (searchTerm.isEmpty()) return true;
        String description = descriptions[index].toLowerCase();
        String category = categories[index].toLowerCase();
        return description.contains(searchTerm) || category.contains(searchTerm);
    }

    private boolean shouldIncludeInCategory(int index, int categoryFilter) {
        String category = categories[index];
        return switch (categoryFilter) {
            case 0 -> category.equals("Products & Inventory");
            case 1 -> category.equals("Sales & Revenue");
            case 2 -> category.equals("Employees");
            case 3 -> category.equals("Supply Chain");
            case 4 -> category.equals("Company Info");
            case 5 -> category.equals("Customer Data");
            case 6 -> category.equals("Orders");
            default -> true;
        };
    }

    private HBox createReportButton(int index) {
        HBox reportItem = new HBox(15);
        reportItem.setAlignment(Pos.CENTER_LEFT);
        reportItem.setPadding(new Insets(15));
        reportItem.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e9ecef; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 2, 0, 0, 1);");

        Label iconLabel = new Label(getIconForCategory(categories[index]));
        iconLabel.setStyle("fx-font-size: 20px; -fx-text-fill: #2c3e50;");

        VBox infoBox = new VBox(4);
        Label titleLabel = new Label(descriptions[index]);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        Label categoryLabel = new Label(categories[index]);
        categoryLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d; -fx-font-weight: 500;");

        if (isDynamicQuery(index)) {
            Label dynamicLabel = new Label("Requires Input");
            dynamicLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #fd7e14; " +
                    "-fx-font-weight: bold; -fx-background-color: #fff3cd; " +
                    "-fx-padding: 2 6; -fx-background-radius: 12;");
            infoBox.getChildren().addAll(titleLabel, categoryLabel, dynamicLabel);
        } else {
            infoBox.getChildren().addAll(titleLabel, categoryLabel);
        }

        Button runButton = new Button("Run Report");
        runButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-font-weight: bold; -fx-padding: 10 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,123,255,0.3), 2, 0, 0, 1);");
        runButton.setOnAction(e -> executeReport(index));

        reportItem.setOnMouseEntered(e -> {
            reportItem.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #007bff; " +
                    "-fx-border-radius: 10; -fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,123,255,0.15), 6, 0, 0, 3);");
            runButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-font-weight: bold; -fx-padding: 10 20; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,123,255,0.4), 3, 0, 0, 2);");
        });

        reportItem.setOnMouseExited(e -> {
            reportItem.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e9ecef; " +
                    "-fx-border-radius: 10; -fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 2, 0, 0, 1);");
            runButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-font-weight: bold; -fx-padding: 10 20; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,123,255,0.3), 2, 0, 0, 1);");
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        reportItem.getChildren().addAll(iconLabel, infoBox, spacer, runButton);
        return reportItem;
    }

    private boolean isDynamicQuery(int index) {
        for (int dynamicIndex : dynamicQueryIndices) {
            if (index == dynamicIndex) return true;
        }
        return false;
    }

    private String getIconForCategory(String category) {
        return switch (category) {
            case "Products & Inventory" -> "📦";
            case "Sales & Revenue" -> "💰";
            case "Employees" -> "👥";
            case "Supply Chain" -> "🚚";
            case "Company Info" -> "🏢";
            case "Customer Data" -> "👤";
            case "Orders" -> "📋";
            default -> "📊";
        };
    }

    private VBox createResultsSection() {
        VBox resultsBox = new VBox(12);

        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 8, 0));

        Label resultsLabel = new Label("📈 Report Results");
        resultsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(20, 20);
        progressIndicator.setVisible(false);

        statusLabel = new Label("Ready to run reports");
        statusLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-style: italic; -fx-font-size: 13px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button clearResultsButton = new Button("Clear Results");
        clearResultsButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 8 16; -fx-font-weight: bold;");
        clearResultsButton.setOnAction(e -> {
            resultArea.clear();
            statusLabel.setText("Results cleared");
        });

        headerBox.getChildren().addAll(resultsLabel, progressIndicator, statusLabel, spacer, clearResultsButton);

        resultArea = new TextArea();
        resultArea.setPrefRowCount(20);
        resultArea.setStyle("-fx-font-family: 'Courier New', monospace; -fx-font-size: 13px; " +
                "-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-font-weight: bold; -fx-padding: 15;");
        resultArea.setPromptText("Report results will appear here...");
        resultArea.setEditable(false);

        resultsBox.getChildren().addAll(headerBox, resultArea);
        return resultsBox;
    }

    private void executeReport(int index) {
        if (isDynamicQuery(index)) {
            String userInput = getUserInput(index);
            if (userInput == null) {
                statusLabel.setText("❌ Query cancelled by user");
                return;
            }
            executeDynamicReport(index, userInput);
        } else {
            executeStaticReport(index);
        }
    }

    private String getUserInput(int index) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Query Parameter Required");
        dialog.setHeaderText("Enter Parameter for: " + descriptions[index]);

        switch (index) {
            case 11:
                dialog.setContentText("Enter Year:");
                dialog.getEditor().setPromptText("e.g., 2024");
                break;
        }

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void executeDynamicReport(int index, String parameter) {
        progressIndicator.setVisible(true);
        statusLabel.setText("Executing: " + descriptions[index] + " with parameter: " + parameter);

        Thread queryThread = new Thread(() -> {
            String result = runDynamicQuery(queries[index], parameter, index);
            javafx.application.Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                if (result.startsWith("Error:")) {
                    statusLabel.setText("❌ " + result);
                    resultArea.setText("=== " + descriptions[index] + " (Parameter: " + parameter + ") ===\n" + result);
                } else {
                    statusLabel.setText("✅ " + descriptions[index] + " (Parameter: " + parameter + ") - Completed");
                    resultArea.setText("=== " + descriptions[index] + " (Parameter: " + parameter + ") ===\n" + result);
                }
            });
        });
        queryThread.setDaemon(true);
        queryThread.start();
    }

    private void executeStaticReport(int index) {
        progressIndicator.setVisible(true);
        statusLabel.setText("Executing: " + descriptions[index]);

        Thread queryThread = new Thread(() -> {
            String result = runQuery(queries[index]);
            javafx.application.Platform.runLater(() -> {
                progressIndicator.setVisible(false);
                if (result.equals("Error executing query")) {
                    statusLabel.setText("❌ Error executing query");
                    resultArea.setText("Error occurred while executing the query. Please check the console for details.");
                }
                else {
                    statusLabel.setText("✅ " + descriptions[index] + " - Completed");
                    resultArea.setText("=== " + descriptions[index] + " ===\n" + result);
                }
            });
        });
        queryThread.setDaemon(true);
        queryThread.start();
    }

    private String runDynamicQuery(String query, String parameter, int queryIndex) {
        StringBuilder sb = new StringBuilder();

        if (queryIndex == 11) {
            try {
                int year = Integer.parseInt(parameter);
                if (year < 2015 || year > LocalDate.now().getYear()) {
                    return "Error: Year must be between 2015 and " + LocalDate.now().getYear();
                }
            } catch (NumberFormatException e) {
                return "Error: Year must be a valid number";
            }
        }

        try (Connection con = MainApplication.dbConn.connectDB()) {
            String finalQuery = query + parameter;
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(finalQuery)) {

                int colCount = rs.getMetaData().getColumnCount();
                List<List<String>> rows = new ArrayList<>();
                List<String> headers = new ArrayList<>();

                for (int i = 1; i <= colCount; i++) {
                    headers.add(rs.getMetaData().getColumnLabel(i));
                }
                rows.add(headers);

                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    for (int i = 1; i <= colCount; i++) {
                        row.add(rs.getString(i) != null ? rs.getString(i) : "NULL");
                    }
                    rows.add(row);
                }

                int[] maxWidths = new int[colCount];
                for (List<String> row : rows) {
                    for (int i = 0; i < colCount; i++) {
                        if (row.get(i).length() > maxWidths[i]) {
                            maxWidths[i] = row.get(i).length();
                        }
                    }
                }

                for (List<String> row : rows) {
                    for (int i = 0; i < colCount; i++) {
                        sb.append(String.format("%-" + (maxWidths[i] + 2) + "s", row.get(i)));
                    }
                    sb.append("\n");
                }

                for (int width : maxWidths) {
                    sb.append("-".repeat(width + 2));
                }
                sb.append("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error executing query: " + ex.getMessage();
        }

        return sb.toString();
    }

    private String runQuery(String query) {
        StringBuilder sb = new StringBuilder();

        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int colCount = rs.getMetaData().getColumnCount();
            List<List<String>> rows = new ArrayList<>();
            List<String> headers = new ArrayList<>();

            for (int i = 1; i <= colCount; i++) {
                headers.add(rs.getMetaData().getColumnLabel(i));
            }
            rows.add(headers);

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= colCount; i++) {
                    String value = rs.getString(i);
                    if (queries.length > 0 && query.equals(queries[queries.length - 1]) &&
                            rs.getMetaData().getColumnLabel(i).equals("Total_Amount")) {
                        try {
                            double amount = Double.parseDouble(value);
                            value = String.format("%.2f", amount);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    row.add(value != null ? value : "NULL");
                }
                rows.add(row);
            }

            int[] maxWidths = new int[colCount];
            for (List<String> row : rows) {
                for (int i = 0; i < colCount; i++) {
                    if (row.get(i).length() > maxWidths[i]) {
                        maxWidths[i] = row.get(i).length();
                    }
                }
            }

            for (List<String> row : rows) {
                for (int i = 0; i < colCount; i++) {
                    sb.append(String.format("%-" + (maxWidths[i] + 2) + "s", row.get(i)));
                }
                sb.append("\n");
            }

            for (int width : maxWidths) {
                sb.append("-".repeat(width + 2));
            }
            sb.append("\n");

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error executing query";
        }

        return sb.toString();
    }

    private static class TabData {
        final VBox content;
        final int categoryFilter;

        TabData(VBox content, int categoryFilter) {
            this.content = content;
            this.categoryFilter = categoryFilter;
        }
    }
}