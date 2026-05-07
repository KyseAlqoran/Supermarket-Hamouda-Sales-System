package d.demo6;

import java.time.LocalDate;
import java.sql.Date;

public class Delivery {
    private int id;
    private LocalDate date;
    private boolean status;
    private String address;
    private boolean isActive;

    public Delivery(int id, Date date, boolean status, String address, boolean isActive) {
        this.id = id;
        this.date = date.toLocalDate();
        this.status = status;
        this.address = address;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
