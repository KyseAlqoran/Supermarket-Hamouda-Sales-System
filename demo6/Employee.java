package d.demo6;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Employee extends Person implements Comparable<Employee> {

    private double employeeSalary;
    private String employeeHireDate;
    private int branchID;
    private String branchName;
    private boolean isActive;

    public Employee(int pID, String pFirstName, String pLastName, String pPhoneNumber, String pEmail,
                    double employeeSalary, String employeeHireDate, int branchID, boolean isActive) {
        super(pID, pFirstName, pLastName, pPhoneNumber, pEmail);
        this.employeeSalary = employeeSalary;
        this.employeeHireDate = employeeHireDate;
        this.branchID = branchID;
        this.isActive = isActive;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(double employeeSalary) {
        if (employeeSalary > 0)
            this.employeeSalary = employeeSalary;
    }

    public String getEmployeeHireDate() {
        return employeeHireDate;
    }

    public void setEmployeeHireDate(String employeeHireDate) {
        this.employeeHireDate = employeeHireDate;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        try {
            this.branchID = Integer.parseInt(branchID);
        } catch (Exception e) {
        }
    }

    public static boolean isValidName(String checkName) {
        if (checkName.isEmpty()) {
            return false;
        }
        for (int i = 0; i < checkName.length(); i++)
            if (!Character.isLetter(checkName.charAt(i)))
                return false;
        return true;
    }

    public static boolean isValidPhoneNumber(String checkPhoneNumber) {
        if (checkPhoneNumber.length() == 10) {
            for (int i = 0; i < checkPhoneNumber.length(); i++) {
                if (!Character.isDigit(checkPhoneNumber.charAt(i))) {
                    return false;
                }
            }
            return checkPhoneNumber.startsWith("059") || checkPhoneNumber.startsWith("056");
        }
        return false;
    }

    public static boolean isValidSalary(double checkSalary) {
        return checkSalary > 0;
    }

    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inputDate = LocalDate.parse(date, formatter);
            LocalDate currentDate = LocalDate.now();
            if (inputDate.isAfter(currentDate)) {
                return false;
            }
            Period period = Period.between(inputDate, currentDate);
            if (period.getYears() > 90) {
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public int getPID() {
        return super.getpID();
    }

    public String getpEmail() {
        return super.getpEmail();
    }

    public String getPFirstName() {
        return super.getpFirstName();
    }

    public String getPLastName() {
        return super.getpLastName();
    }

    public String getPhoneNumber() {
        return super.getpPhoneNumber();
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getBranchName() {
        if (this.branchName != null) return this.branchName;
        String SQL = "SELECT bName FROM Branch WHERE bID = " + this.branchID;
        try (Connection con = MainApplication.dbConn.connectDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            if (rs.next()) {
                this.branchName = rs.getString("bName");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return this.branchName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public int compareTo(Employee o) {
        return Integer.compare(getpID(), o.getpID());
    }
}