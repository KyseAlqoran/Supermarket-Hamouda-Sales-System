package d.demo6;

public class Branch {
    private int id;
    private String name;
    private String location;
    private String phoneNumber;
    public Branch(int id, String name, String location, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "bID=" + id +
                ", bName='" + name + '\'' +
                ", location='" + location + '\'' +
                ", bPhoneNumbers='" + phoneNumber + '\'' +
                '}';
    }
}
