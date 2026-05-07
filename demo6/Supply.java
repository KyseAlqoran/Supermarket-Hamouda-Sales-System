package d.demo6;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Supply {
    private int supID;
    private int quantity;
    private LocalDate supDate;
    private double unitCost;
    private int prodID;
    private int sID;
    private String supplierName;
    private String productName;

    public Supply(int supID, int quantity, Date supDate, double unitCost, int prodID, int sID) {
        this.supID = supID;
        this.quantity = quantity;
        this.supDate = supDate.toLocalDate();
        this.unitCost = unitCost;
        this.prodID = prodID;
        this.sID = sID;
    }

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getSupDate() {
        return supDate;
    }

    public void setSupDate(LocalDate supDate) {
        this.supDate = supDate;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public int getProdID() {
        return prodID;
    }

    public void setProdID(int prodID) {
        this.prodID = prodID;
    }

    public int getsID() {
        return sID;
    }

    public void setsID(int sID) {
        this.sID = sID;
    }
    public String getSupplierName() {
        String query = "SELECT sName FROM Supplier WHERE sID = " + sID;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) supplierName = rs.getString("sName");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Getting Supplier Name");
        }
        return supplierName;
    }

    public String getProductName() {
        String query = "SELECT prodName FROM Product WHERE prodID = " + prodID;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) productName = rs.getString("prodName");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error Getting Product Name");
        }
        return productName;
    }
}
