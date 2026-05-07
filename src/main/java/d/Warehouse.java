package d.demo6;

public class Warehouse {
    private int id;
    private int capacity;
    private String location;

    public Warehouse(int id, int capacity, String location) {
        this.id = id;
        this.capacity = capacity;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
