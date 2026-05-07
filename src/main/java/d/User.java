package d.demo6;

public class User {
    private int id;
    private String name;
    private String password;
    private String accountType;
    private boolean activeStatus;
    private int pID;

    public User(int id, String name, String password, String accountType, boolean activeStatus, int pID) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.accountType = accountType;
        this.activeStatus = activeStatus;
        this.pID = pID;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }
}
