package d.demo6;

public class Supplier {
    private int supID;
    private String supplierName;
    private String phoneNumber;
    private boolean isActive;
    public Supplier(int supID, String supplierName, String phoneNumber, boolean isActive) {
        this.supID = supID;
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
