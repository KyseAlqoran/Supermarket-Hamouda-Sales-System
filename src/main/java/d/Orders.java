package d.demo6;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;

public class Orders {
    private int oID;
    private LocalDate oDate;
    private boolean status;
    private int cID;
    private int dID;
    private boolean isActive;

    public Orders(int oID, Date oDate, boolean status, int cID, int dID, boolean isActive) {
        this.oID = oID;
        this.oDate = oDate.toLocalDate();
        this.status = status;
        this.cID = cID;
        this.dID = dID;
        this.isActive = isActive;
    }

    public int getOID() {
        return oID;
    }

    public void setOID(int oID) {
        this.oID = oID;
    }

    public LocalDate getODate() {
        return oDate;
    }

    public void setODate(LocalDate oDate) {
        this.oDate = oDate;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCID() {
        return cID;
    }

    public void setCID(int cID) {
        this.cID = cID;
    }

    public int getDID() {
        return dID;
    }

    public void setDID(int dID) {
        this.dID = dID;
    }

    public int getoID() {
        return getOID();
    }

    public void setoID(int oID) {
        setOID(oID);
    }

    public LocalDate getoDate() {
        return getODate();
    }

    public void setoDate(LocalDate oDate) {
        setODate(oDate);
    }

    public boolean isStatus() {
        return getStatus();
    }

    public int getcID() {
        return getCID();
    }

    public void setcID(int cID) {
        setCID(cID);
    }

    public int getdID() {
        return getDID();
    }

    public void setdID(int dID) {
        setDID(dID);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCustomerName() {
        String name = "";
        String SQL = "SELECT pFirstName, pLastName FROM Person WHERE pID = " + this.cID;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            if (rs.next()) name = rs.getString("pFirstName") + " " + rs.getString("pLastName");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return name;
    }

    public String getDeliveryLocation() {
        String address = "";
        String SQL = "SELECT Address FROM Delivery WHERE dID = " + this.dID;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            if (rs.next()) address = rs.getString("Address");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return address;
    }
}