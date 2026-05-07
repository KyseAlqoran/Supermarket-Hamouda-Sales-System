package d.demo6;

import java.time.LocalDate;

public class Customer extends Person implements Comparable<Customer> {

    private LocalDate LastPurchaseDate;
    private boolean isActive;
    public Customer(int pID, String pFirstName, String pLastName, String pPhoneNumber, String pEmail, LocalDate lastPurchaseDate, boolean isActive) {
        super(pID, pFirstName, pLastName, pPhoneNumber, pEmail);
        LastPurchaseDate = lastPurchaseDate;
        this.isActive = isActive;
    }

    public LocalDate getLastPurchaseDate() {
        return LastPurchaseDate;
    }

    public void setLastPurchaseDate(LocalDate lastPurchaseDate) {
        LastPurchaseDate = lastPurchaseDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return super.toString() + "LastPurchaseDate: " + LastPurchaseDate;
    }

    @Override
    public int compareTo(Customer o) {
        return Integer.compare(getpID(), o.getpID());
    }
}
