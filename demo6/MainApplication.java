package d.demo6;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private static String URL = "127.0.0.1";
    private static String dbUsername = "root";
    private static String dbPassword = "zaidqam1254";
    private static String port = "3306";
    private static String dbName = "SuperMarketManagementSystem";
    public static DBConn dbConn;

    @Override
    public void start(Stage stage) throws ClassNotFoundException {
        stage.setScene(new Scene(new MainWindow(stage).p(), 800, 600));
        stage.setTitle("Super Market Management System");
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        readDB();
        launch(args);
    }

    public static void readDB() throws SQLException, ClassNotFoundException {
        readEmployee();
        readUsers();
        readCustomers();
        readSale();
        readCategory();
        readWarehouse();
        readProducts();
        readSaleDetails();
        readSuppliers();
        readSupplies();
        readBranches();
        readDeliveries();
        readOrders();
        readOrder_Details();
        readDiscount();
    }

    private static void readEmployee() throws SQLException, ClassNotFoundException {
        EmployeeFX.employeeList.clear();
        EmployeeFX.employeeTable.setItems(EmployeeFX.employeeList);
        EmployeeFX.employeeTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "SELECT * FROM Person p, Employee e WHERE p.pID = e.eID";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (rs.getBoolean("isActive")) {
                EmployeeFX.employeeList.add(new Employee(
                        rs.getInt("pID"),
                        rs.getString("pFirstName"),
                        rs.getString("pLastName"),
                        rs.getString("pPhoneNumber"),
                        rs.getString("pEmail"),
                        rs.getDouble("esalary"),
                        rs.getString("ehiredate"),
                        rs.getInt("bID"),
                        rs.getBoolean("isActive")
                ));
            }
        }
        rs.close();
        stmt.close();
    }

    private static void readUsers() throws SQLException, ClassNotFoundException {
        UsersFX.userList.clear();
        UsersFX.userTable.setItems(UsersFX.userList);
        UsersFX.userTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from User";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            UsersFX.userList.add(new User(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4),
                    rs.getBoolean(5), rs.getInt(6)));
        }
        rs.close();
        stmt.close();
    }

    private static void readCustomers() throws SQLException, ClassNotFoundException {
        MainWindow.customerList.clear();
        MainWindow.customerTable.setItems(MainWindow.customerList);
        MainWindow.customerTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "SELECT * FROM Customer JOIN Person ON Customer.cID = Person.pID";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (rs.getBoolean("isActive")) {
                MainWindow.customerList.add(new Customer(
                        rs.getInt("cID"),
                        rs.getString("pFirstName"),
                        rs.getString("pLastName"),
                        rs.getString("pPhoneNumber"),
                        rs.getString("pEmail"),
                        rs.getDate("LastPurchaseDate").toLocalDate(),
                        rs.getBoolean("isActive")
                ));
            }
        }
        rs.close();
        stmt.close();
    }

    private static void readCategory() throws SQLException, ClassNotFoundException {
        CategoryFX.categoryList.clear();
        CategoryFX.categoryTable.setItems(CategoryFX.categoryList);
        CategoryFX.categoryTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from category";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (rs.getBoolean(5)) {
                CategoryFX.categoryList.add(new Category(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getBoolean(5)));
            }
        }
        rs.close();
        stmt.close();
    }

    private static void readWarehouse() throws SQLException, ClassNotFoundException {
        MainWindow.warehouseList.clear();
        MainWindow.warehouseTable.setItems(MainWindow.warehouseList);
        MainWindow.warehouseTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from warehouse";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            MainWindow.warehouseList.add(
                    new Warehouse(rs.getInt(1), rs.getInt(2), rs.getString(3)));
        }
        rs.close();
        stmt.close();
    }

    private static void readProducts() throws SQLException, ClassNotFoundException {
        ProductFX.productList.clear();
        ProductFX.productTable.setItems(ProductFX.productList);
        ProductFX.productTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from product";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (rs.getBoolean(10)) {
                ProductFX.productList.add(new Product(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getDouble(4), rs.getInt(5), rs.getDate(6), rs.getInt(7),
                        rs.getInt(8), rs.getString(9), rs.getBoolean(10)));
            }
        }
        rs.close();
        stmt.close();
        ProductFX.productTable.setItems(ProductFX.productList);
        ProductFX.productTable.refresh();
    }

    private static void readSale() throws SQLException, ClassNotFoundException {
        SaleFX.saleList.clear();
        SaleFX.saleTable.setItems(SaleFX.saleList);
        SaleFX.saleTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from sale";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            SaleFX.saleList.add(new Sale(
                    rs.getInt(1), rs.getDate(2),
                    rs.getInt(3), rs.getInt(4)
            ));
        }
        rs.close();
        stmt.close();
        SaleFX.saleTable.setItems(SaleFX.saleList);
        SaleFX.saleTable.refresh();
    }

    private static void readSaleDetails() throws SQLException, ClassNotFoundException {
        MainWindow.saleDetailsList.clear();
        MainWindow.saleDetailsTable.setItems(MainWindow.saleDetailsList);
        MainWindow.saleDetailsTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from sale_details";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            MainWindow.saleDetailsList.add(new Sale_Details(rs.getInt(1), rs.getInt(2),
                    rs.getInt(3), rs.getDouble(4), rs.getDouble(5)));
        }
        rs.close();
        stmt.close();
        MainWindow.saleDetailsTable.setItems(MainWindow.saleDetailsList);
        MainWindow.saleDetailsTable.refresh();
    }

    private static void readSuppliers() throws SQLException, ClassNotFoundException {
        SupplierFX.suppliersList.clear();
        SupplierFX.suppliersTable.setItems(SupplierFX.suppliersList);
        SupplierFX.suppliersTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from supplier";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (rs.getBoolean(4)) {
                SupplierFX.suppliersList.add(new Supplier(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
            }
        }
        rs.close();
        stmt.close();
        SupplierFX.suppliersTable.setItems(SupplierFX.suppliersList);
        SupplierFX.suppliersTable.refresh();
    }

    private static void readSupplies() throws SQLException, ClassNotFoundException {
        SupplyFX.supplyList.clear();
        SupplyFX.supplyTable.setItems(SupplyFX.supplyList);
        SupplyFX.supplyTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from supply";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            SupplyFX.supplyList.add(new Supply(rs.getInt(1), rs.getInt(2),
                    rs.getDate(3), rs.getDouble(4), rs.getInt(5), rs.getInt(6)));
        }
        SupplyFX.supplyTable.setItems(SupplyFX.supplyList);
        SupplyFX.supplyTable.refresh();
    }

    private static void readBranches() throws SQLException, ClassNotFoundException {
        MainWindow.branchList.clear();
        MainWindow.branchTable.setItems(MainWindow.branchList);
        MainWindow.branchTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from branch";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            MainWindow.branchList.add(new Branch(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        rs.close();
        stmt.close();
        MainWindow.branchTable.setItems(MainWindow.branchList);
        MainWindow.branchTable.refresh();
    }


    private static void readDeliveries() throws SQLException, ClassNotFoundException {
        MainWindow.deliveryList.clear();
        MainWindow.deliveryTable.setItems(MainWindow.deliveryList);
        MainWindow.deliveryTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from delivery";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (rs.getBoolean(5)) {
                MainWindow.deliveryList.add(new Delivery(rs.getInt(1), rs.getDate(2), rs.getBoolean(3), rs.getString(4), rs.getBoolean(5)));
            }
        }
        rs.close();
        stmt.close();
        MainWindow.deliveryTable.setItems(MainWindow.deliveryList);
        MainWindow.deliveryTable.refresh();
    }


    private static void readOrders() throws SQLException, ClassNotFoundException {
        OrderFX.ordersList.clear();
        OrderFX.ordersTable.setItems(OrderFX.ordersList);
        OrderFX.ordersTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from orders";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            if (rs.getBoolean(6)) {
                OrderFX.ordersList.add(new Orders(rs.getInt(1), rs.getDate(2),
                        rs.getBoolean(3), rs.getInt(4), rs.getInt(5), rs.getBoolean(6)));
            }
        }
        rs.close();
        stmt.close();
        OrderFX.ordersTable.setItems(OrderFX.ordersList);
        OrderFX.ordersTable.refresh();
    }


    private static void readOrder_Details() throws SQLException, ClassNotFoundException {
        MainWindow.orderDetails.clear();
        MainWindow.orderDetailsTable.setItems(MainWindow.orderDetails);
        MainWindow.orderDetailsTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from order_detail";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            MainWindow.orderDetails.add(new Order_Detail(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
        }
        rs.close();
        stmt.close();
        MainWindow.orderDetailsTable.setItems(MainWindow.orderDetails);
        MainWindow.orderDetailsTable.refresh();
    }


    private static void readDiscount() throws SQLException, ClassNotFoundException {
        MainWindow.discountList.clear();
        MainWindow.discountTable.setItems(MainWindow.discountList);
        MainWindow.discountTable.refresh();
        dbConn = new DBConn(URL, port, dbName, dbUsername, dbPassword);
        Connection con = dbConn.connectDB();
        String SQL = "select * from discount";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()) {
            MainWindow.discountList.add(new Discount(rs.getInt(1),
                    rs.getDate(2), rs.getDate(3), rs.getDouble(4), rs.getInt(5)));
        }
        rs.close();
        stmt.close();
        MainWindow.discountTable.setItems(MainWindow.discountList);
        MainWindow.discountTable.refresh();
    }

}