package d.demo6;

public class Person {
    private int pID;
    private String pFirstName;
    private String pLastName;
    private String pPhoneNumber;
    private String pEmail;

    public Person(int pID, String pFirstName, String pLastName, String pPhoneNumber, String pEmail) {
        this.pID = pID;
        this.pFirstName = pFirstName;
        this.pLastName = pLastName;
        this.pPhoneNumber = pPhoneNumber;
        this.pEmail = pEmail;
    }

    public int getpID() {
        return pID;
    }


    public String getpFirstName() {
        return pFirstName;
    }

    public void setpFirstName(String pFirstName) {
        this.pFirstName = pFirstName;
    }

    public String getpLastName() {
        return pLastName;
    }

    public void setpLastName(String pLastName) {
        this.pLastName = pLastName;
    }

    public String getpPhoneNumber() {
        return pPhoneNumber;
    }

    public void setpPhoneNumber(String pPhoneNumber) {
        this.pPhoneNumber = pPhoneNumber;
    }

    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    @Override
    public String toString() {
        return "Person{" +
                "pID=" + pID +
                ", pFirstName='" + pFirstName + '\'' +
                ", pLastName='" + pLastName + '\'' +
                ", pPhoneNumber='" + pPhoneNumber + '\'' +
                ", pEmail='" + pEmail + '\'' +
                '}';
    }
}
