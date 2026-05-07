package d.demo6;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Sale {
    private int saleID;
    private LocalDate date;
    private double total_price;
    private int bID;
    private int pID;
    private String branchName;
    private String personName;

    public Sale(int saleID, Date date, int bID, int pID) {
        this.saleID = saleID;
        this.date = date.toLocalDate();
        this.bID = bID;
        this.pID = pID;
        this.personName = getPersonNameSQL();
        this.branchName = getBranchNameSQL();
        this.total_price = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        String query = "SELECT SUM(quantity * unitPrice * (1 - discount / 100.0)) " +
                "FROM Sale_Details WHERE saleID = " + this.saleID;
        double result = 0;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                result = rs.getDouble(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Math.round(result * 100.0) / 100.0;
    }

    private String getPersonNameSQL() {
        try (Connection con = MainApplication.dbConn.connectDB()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.pFirstName, p.pLastName FROM person p WHERE p.pID = " + pID);
            if (rs.next()) {
                String name = rs.getString("pFirstName") + " " + rs.getString("pLastName");
                return name;}
                } catch(Exception e){
                    e.printStackTrace();
                }
                return "";
            }

            private String getBranchNameSQL () {
                try (Connection con = MainApplication.dbConn.connectDB();
                     Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT bName FROM Branch WHERE bID = " + this.bID)) {
                    if (rs.next()) return rs.getString(1);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                return "";
            }

            public double getTotal_price () {
                this.total_price = calculateTotalPrice();
                return total_price;
            }

            public int getSaleID () {
                return saleID;
            }

            public void setSaleID ( int saleID){
                this.saleID = saleID;
            }

            public LocalDate getDate () {
                return date;
            }

            public void setDate (LocalDate date){
                this.date = date;
            }

            public void setTotal_price ( double total_price){
                this.total_price = total_price;
            }

            public int getbID () {
                return bID;
            }

            public void setbID ( int bID){
                this.bID = bID;
            }

            public int getpID () {
                return pID;
            }

            public void setpID ( int pID){
                this.pID = pID;
            }

            public String getPersonName () {
                return personName;
            }

            public String getBranchName () {
                return this.branchName;
            }
        }
