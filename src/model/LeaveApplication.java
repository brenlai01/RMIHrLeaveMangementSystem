package model;

import java.io.Serializable;

// Serializable - transferred between client and server via RMI
public class LeaveApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    private int leaveId;
    private int employeeId;
    private String startDate;
    private String endDate;
    private String reason;
    private String status = "PENDING";
    private String appliedAt;

    public LeaveApplication() {
    }

    public LeaveApplication(int employeeId, String startDate, String endDate,
                            String reason, String status) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(String appliedAt) {
        this.appliedAt = appliedAt;
    }

    @Override
    public String toString() {
        // TODO: return a meaningful string representation of the LeaveApplication
        return super.toString();
    }
}
