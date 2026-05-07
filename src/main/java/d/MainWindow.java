package d.demo6;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainWindow {
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static ObservableList<Warehouse> warehouseList = FXCollections.observableArrayList();
    public static ObservableList<Sale_Details> saleDetailsList = FXCollections.observableArrayList();
    public static ObservableList<Order_Detail> orderDetails = FXCollections.observableArrayList();
    public static ObservableList<Delivery> deliveryList = FXCollections.observableArrayList();
    public static ObservableList<Discount> discountList = FXCollections.observableArrayList();
    public static ObservableList<Branch> branchList = FXCollections.observableArrayList();
    public static TableView<Customer> customerTable = new TableView<>();
    public static TableView<Warehouse> warehouseTable = new TableView<>();
    public static TableView<Sale_Details> saleDetailsTable = new TableView<>();
    public static TableView<Order_Detail> orderDetailsTable = new TableView<>();
    public static TableView<Delivery> deliveryTable = new TableView<>();
    public static TableView<Discount> discountTable = new TableView<>();
    public static TableView<Branch> branchTable = new TableView<>();
    public BorderPane root = new BorderPane();
    private Stage s;
    private Branch currentBranch;

    MainWindow(Stage s) {
        this.s = s;
    }

    public BorderPane p() {
        root.setCenter(new LoginFX(this, s).loginScene());
        return root;
    }

}
