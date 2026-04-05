package model;

import java.io.Serializable;

// Serializable - transferred between client and server via RMI
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private int employeeId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String icPassport;
    private String familyDetails;
    private int leaveBalance = 14;

    public Employee() {
    }

    public Employee(String username, String password, String firstName, String lastName,
                    String icPassport, String familyDetails, int leaveBalance) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.icPassport = icPassport;
        this.familyDetails = familyDetails;
        this.leaveBalance = leaveBalance;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIcPassport() {
        return icPassport;
    }

    public void setIcPassport(String icPassport) {
        this.icPassport = icPassport;
    }

    public String getFamilyDetails() {
        return familyDetails;
    }

    public void setFamilyDetails(String familyDetails) {
        this.familyDetails = familyDetails;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    @Override
    public String toString() {
        return "Employee{id=" + employeeId + ", username='" + username + "', name='" + firstName + " " + lastName + "'}";
    }
}
