package service;

import remote.HRMService;
import model.Employee;
import model.LeaveApplication;
import database.DatabaseConnection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

// Multithreading: RMI server automatically handles each client call in a separate thread
public class HRMServiceImpl extends UnicastRemoteObject implements HRMService {

    public HRMServiceImpl() throws RemoteException {
        super();
    }

    @Override
    // Method to check user credentials in employee and HR tables
    public String login(String username, String password) throws RemoteException {
        String hrSql = "SELECT * FROM hr_staff WHERE username = ? AND password = ?";
        String empSql = "SELECT * FROM employees WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            // Check hr_staff table first
            try (PreparedStatement hrStmt = connection.prepareStatement(hrSql)) {
                hrStmt.setString(1, username);
                hrStmt.setString(2, password);

                try (ResultSet hrRs = hrStmt.executeQuery()) {
                    if (hrRs.next()) {
                        return "HR";
                    }
                }
            }

            // Check employees table if user not in  hr_staff table
            try (PreparedStatement empStmt = connection.prepareStatement(empSql)) {
                empStmt.setString(1, username);
                empStmt.setString(2, password);

                try (ResultSet empRs = empStmt.executeQuery()) {
                    if (empRs.next()) {
                        return "Employee";
                    }
                }
            }
            // return null if user credentials not found in both tables
            return null;

        } catch (Exception e) {
            throw new RemoteException("Error during login: " + e.getMessage(), e);
        }
    }

    @Override
    public void registerEmployee(Employee emp) throws RemoteException {
        // TODO: insert new employee record into database
    }

    @Override
    public Employee getEmployeeDetails(String username) throws RemoteException {
        // TODO: query employees table by username
        return null;
    }

    @Override
    public void updatePersonalDetails(Employee emp) throws RemoteException {
        // TODO: update employee record in database
    }

    @Override
    public void updateFamilyDetails(String username, String familyDetails) throws RemoteException {
        // TODO: update family_details field in database
    }

    @Override
    public int checkLeaveBalance(String username) throws RemoteException {
        // TODO: query leave_balance from employees table
        return 0;
    }

    @Override
    public void applyLeave(LeaveApplication application) throws RemoteException {
        // TODO: insert leave application into leave_applications table
        // TODO: decide and implement leave balance deduction policy:
        //       option A - deduct immediately on application (optimistic)
        //       option B - deduct only after HR approves (recommended)
    }

    @Override
    public List<LeaveApplication> getLeaveHistory(String username) throws RemoteException {
        // TODO: query all leave applications for employee
        return new ArrayList<>();
    }
        List<LeaveApplication> history = new ArrayList<>();

        String leaveSql = "SELECT * FROM leave_applications WHERE employee_id = ? ORDER BY applied_at DESC";

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            int employeeId = -1;

            // Get Employee ID
            Employee emp = getEmployeeDetails(username);

            if (emp == null) {
                return history;
            }

            employeeId = emp.getEmployeeId();

            // Step 2: Get leave applications
            try (PreparedStatement leaveStmt = connection.prepareStatement(leaveSql)) {
                leaveStmt.setInt(1, employeeId);
                try (ResultSet leaveRs = leaveStmt.executeQuery()) {
                    while (leaveRs.next()) {
                        LeaveApplication leave = new LeaveApplication();
                        leave.setLeaveId(leaveRs.getInt("leave_id"));
                        leave.setEmployeeId(leaveRs.getInt("employee_id"));
                        leave.setStartDate(String.valueOf(leaveRs.getDate("start_date")));
                        leave.setEndDate(String.valueOf(leaveRs.getDate("end_date")));
                        leave.setReason(leaveRs.getString("reason"));
                        leave.setStatus(leaveRs.getString("status"));
                        leave.setAppliedAt(leaveRs.getString("applied_at"));

                        history.add(leave);
                    }
                }
            }

        } catch (Exception e) {
            throw new RemoteException("Error fetching leave history: " + e.getMessage(), e);
        }

        return history;
    }

    @Override
    public List<LeaveApplication> getLeaveStatus(String username) throws RemoteException {
        // TODO: query leave applications filtered by status
        return new ArrayList<>();
    }

    @Override
    public List<LeaveApplication> generateYearlyReport(int year) throws RemoteException {
        // TODO: query all leave applications for given year
        return new ArrayList<>();
    }
}
