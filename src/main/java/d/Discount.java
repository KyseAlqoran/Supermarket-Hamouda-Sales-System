package d.demo6;

import java.time.LocalDate;
import java.sql.Date;

public class Discount {
    private int disID;
    private LocalDate startDate;
    private LocalDate endDate;
    private double disPercentage;
    private int prodID;

    public Discount(int disID, Date startDate, Date endDate, double disPercentage, int prodID) {
        this.disID = disID;
        this.startDate = startDate.toLocalDate();
        this.endDate = endDate.toLocalDate();
        this.disPercentage = disPercentage;
        this.prodID = prodID;
    }

    public int getDisID() {
        return disID;
    }

    public void setDisID(int disID) {
        this.disID = disID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getDisPercentage() {
        return disPercentage;
    }

    public void setDisPercentage(double disPercentage) {
        this.disPercentage = disPercentage;
    }

    public int getProdID() {
        return prodID;
    }

    public void setProdID(int prodID) {
        this.prodID = prodID;
    }
}
