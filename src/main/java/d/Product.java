package d.demo6;

import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Product {
    private int prodID;
    private String prodName;
    private String description;
    private double price;
    private int quantity;
    private LocalDate expire_Date;
    private int catID;
    private String photoPath;
    private int wID;
    private SimpleStringProperty catName = new SimpleStringProperty();
    private SimpleStringProperty location = new SimpleStringProperty();
    private boolean isActive;

    public Product(int prodID, String prodName, String description, double price, int quantity, Date expire_Date, int catID, int wID, String photoPath, boolean isActive) {
        this.prodID = prodID;
        this.prodName = prodName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.expire_Date = expire_Date.toLocalDate();
        this.catID = catID;
        this.wID = wID;
        if (photoPath == null || photoPath.equals("null")) {
            this.photoPath = "noProd.png";
        } else {
            this.photoPath = photoPath;
        }
        this.catName.set(getCategoryName());
        this.location.set(getWarehouseLocation());
        this.isActive = isActive;
    }

    private String getCategoryName() {
        String result = "";
        String SQL = "SELECT c.catName " +
                "FROM Product p " +
                "JOIN Category c ON p.catID = c.catID " +
                "WHERE p.prodID = " + prodID + ";";
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Executing Query");
        }
        return result;
    }

    private String getWarehouseLocation() {
        String result = "";
        String SQL = "SELECT w.wLocation " +
                "FROM Product p " +
                "JOIN Warehouse w ON p.wID = w.wID " +
                "WHERE p.prodID = " + prodID + ";";
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Executing Query");
        }
        return result;
    }

    public int getProdID() {
        return prodID;
    }

    public void setProdID(int prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpire_Date() {
        return expire_Date;
    }

    public void setExpire_Date(LocalDate expire_Date) {
        this.expire_Date = expire_Date;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getwID() {
        return wID;
    }

    public void setwID(int wID) {
        this.wID = wID;
    }

    public String getCatName() {
        return catName.get();
    }

    public SimpleStringProperty catNameProperty() {
        return catName;
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setCatName(String catName) {
        this.catName.set(catName);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z0-9\\s]+");
    }

    public static boolean isValidPrice(Double price) {
        return price != null && price > 0;
    }

    public static boolean isValidQuantity(Integer quantity) {
        return quantity != null && quantity >= 0;
    }

    public static boolean isValidDate(String date) {
        try {
            java.time.LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
