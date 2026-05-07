package d.demo6;

public class Sale_Details {
    private int saleID;
    private int prodID;
    private int quantity;
    private double total;
    private double discount;

    public Sale_Details(int saleID, int prodID, int quantity,double unitPrice,double discount) {
        this.saleID = saleID;
        this.prodID = prodID;
        this.quantity = quantity;
        this.total = unitPrice;
        this.discount = discount;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
