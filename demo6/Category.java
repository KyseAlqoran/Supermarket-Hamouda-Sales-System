package d.demo6;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Category {
    private int catID;
    private String catName;
    private String description;
    private int total_products;
    private String photoPath;
    private boolean isActive;

    public Category(int catID, String catName, String description, String photoPath, boolean isActive) {
        this.catID = catID;
        this.catName = catName;
        this.total_products = gettotalProductsSQL();
        this.description = description;
        this.photoPath = photoPath;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getTotal_products() {
        return total_products;
    }

    public void setTotal_products(int total_products) {
        this.total_products = total_products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    private int gettotalProductsSQL() {
        String query = "SELECT COUNT(*) FROM Product WHERE catID = " + this.catID + " AND isActive = TRUE;";
        int result = 0;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
