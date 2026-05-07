package d.demo6;

public class Order_Detail {
    private int oID;
    private int prodID;
    private int quantity;
    private double unitPrice;

    public Order_Detail(int oID, int prodID, int quantity) {
        this.oID = oID;
        this.prodID = prodID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getoID() {
        return oID;
    }

    public void setoID(int oID) {
        this.oID = oID;
    }

    public int getProdID() {
        return prodID;
    }

    public void setProdID(int prodID) {
        this.prodID = prodID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
