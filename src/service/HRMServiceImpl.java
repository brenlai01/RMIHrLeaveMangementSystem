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
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;

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
        // Input validation
        if (emp == null) {
            throw new RemoteException("Employee object cannot be null");
        }
        if (emp.getUsername() == null || emp.getUsername().trim().isEmpty()) {
            throw new RemoteException("Username cannot be empty");
        }
        if (emp.getPassword() == null || emp.getPassword().trim().isEmpty()) {
            throw new RemoteException("Password cannot be empty");
        }
        if (emp.getFirstName() == null || emp.getFirstName().trim().isEmpty()) {
            throw new RemoteException("First name cannot be empty");
        }
        if (emp.getLastName() == null || emp.getLastName().trim().isEmpty()) {
            throw new RemoteException("Last name cannot be empty");
        }
        if (emp.getIcPassport() == null || emp.getIcPassport().trim().isEmpty()) {
            throw new RemoteException("IC/Passport cannot be empty");
        }
        if (emp.getLeaveBalance() < 0) {
            throw new RemoteException("Leave balance cannot be negative");
        }

        String sql = "INSERT INTO employees (username, password, first_name, last_name, ic_passport, family_details, leave_balance) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            stmt.setString(1, emp.getUsername().trim());
            stmt.setString(2, emp.getPassword());
            stmt.setString(3, emp.getFirstName().trim());
            stmt.setString(4, emp.getLastName().trim());
            stmt.setString(5, emp.getIcPassport().trim());
            stmt.setString(6, emp.getFamilyDetails() != null ? emp.getFamilyDetails().trim() : "");
            stmt.setInt(7, emp.getLeaveBalance());

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RemoteException("Error registering employee: " + e.getMessage(), e);
        }
    }

    @Override
    public Employee getEmployeeDetails(String username) throws RemoteException {
        String empSql = "SELECT * FROM employees WHERE username = ?";
        Employee emp = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            try (PreparedStatement empStmt = connection.prepareStatement(empSql)) {
                empStmt.setString(1, username);
                try (ResultSet empRs = empStmt.executeQuery()) {
                    if (empRs.next()) {
                        emp = new Employee();
                        emp.setEmployeeId(empRs.getInt("employee_id"));
                        emp.setUsername(empRs.getString("username"));
                        emp.setPassword(empRs.getString("password"));
                        emp.setFirstName(empRs.getString("first_name"));
                        emp.setLastName(empRs.getString("last_name"));
                        emp.setIcPassport(empRs.getString("ic_passport"));
                        emp.setFamilyDetails(empRs.getString("family_details"));
                        emp.setLeaveBalance(empRs.getInt("leave_balance"));
                    }
                }
            }
        } catch (Exception e) {
            throw new RemoteException("Error fetching employee details: " + e.getMessage(), e);
        }

        return emp;
    }

    @Override
    public void updatePersonalDetails(Employee emp) throws RemoteException {
        if (emp == null) {
            throw new RemoteException("Employee object cannot be null");
        }
        if (emp.getEmployeeId() <= 0) {
            throw new RemoteException("Invalid employee ID");
        }
        if (emp.getUsername() == null || emp.getUsername().trim().isEmpty()) {
            throw new RemoteException("Username cannot be empty");
        }
        if (emp.getPassword() == null || emp.getPassword().trim().isEmpty()) {
            throw new RemoteException("Password cannot be empty");
        }
        if (emp.getFirstName() == null || emp.getFirstName().trim().isEmpty()) {
            throw new RemoteException("First name cannot be empty");
        }
        if (emp.getLastName() == null || emp.getLastName().trim().isEmpty()) {
            throw new RemoteException("Last name cannot be empty");
        }
        if (emp.getIcPassport() == null || emp.getIcPassport().trim().isEmpty()) {
            throw new RemoteException("IC/Passport cannot be empty");
        }

        String checkSql = "SELECT employee_id FROM employees WHERE employee_id = ?";
        String duplicateSql = "SELECT employee_id FROM employees WHERE username = ? AND employee_id <> ?";
        String updateSql = "UPDATE employees SET username = ?, password = ?, first_name = ?, last_name = ?, ic_passport = ? WHERE employee_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, emp.getEmployeeId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new RemoteException("Employee with ID " + emp.getEmployeeId() + " does not exist");
                    }
                }
            }

            try (PreparedStatement duplicateStmt = connection.prepareStatement(duplicateSql)) {
                duplicateStmt.setString(1, emp.getUsername().trim());
                duplicateStmt.setInt(2, emp.getEmployeeId());
                try (ResultSet rs = duplicateStmt.executeQuery()) {
                    if (rs.next()) {
                        throw new RemoteException("Username " + emp.getUsername() + " is already taken");
                    }
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setString(1, emp.getUsername().trim());
                stmt.setString(2, emp.getPassword());
                stmt.setString(3, emp.getFirstName().trim());
                stmt.setString(4, emp.getLastName().trim());
                stmt.setString(5, emp.getIcPassport().trim());
                stmt.setInt(6, emp.getEmployeeId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RemoteException("Failed to update personal details");
                }
            }

        } catch (Exception e) {
            throw new RemoteException("Error updating personal details: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateFamilyDetails(String username, String familyDetails) throws RemoteException {
        if (username == null || username.trim().isEmpty()) {
            throw new RemoteException("Username cannot be empty");
        }

        String trimmedFamily = familyDetails != null ? familyDetails.trim() : "";
        String checkSql = "SELECT employee_id FROM employees WHERE username = ?";
        String updateSql = "UPDATE employees SET family_details = ? WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, username.trim());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new RemoteException("Employee with username " + username + " does not exist");
                    }
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setString(1, trimmedFamily);
                stmt.setString(2, username.trim());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RemoteException("Failed to update family details");
                }
            }

        } catch (Exception e) {
            throw new RemoteException("Error updating family details: " + e.getMessage(), e);
        }
    }

    @Override
    public int checkLeaveBalance(String username) throws RemoteException {
        int balance = 0;

        try {
            Connection conn = DatabaseConnection.getConnection();

            String sql = "SELECT leave_balance FROM employees WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                balance = rs.getInt("leave_balance");
            } else {
                throw new RemoteException("User not found");
            }

            conn.close();

        } catch (Exception e) {
            throw new RemoteException("Error fetching leave balance: " + e.getMessage());
        }

        return balance;
    }

    @Override
    public void applyLeave(String username, String startDate, String endDate, String reason) throws RemoteException {

        try {
            Connection conn = DatabaseConnection.getConnection();

            // 3. Get employee_id
            String getIdSql = "SELECT employee_id FROM employees WHERE username = ?";
            PreparedStatement ps1 = conn.prepareStatement(getIdSql);
            ps1.setString(1, username);

            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                throw new RemoteException("User not found");
            }

            int employeeId = rs.getInt("employee_id");

            // 4. Insert leave
            String insertSql = "INSERT INTO leave_applications (employee_id, start_date, end_date, reason, status, applied_at) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps2 = conn.prepareStatement(insertSql);
            ps2.setInt(1, employeeId);
            ps2.setString(2, startDate);
            ps2.setString(3, endDate);
            ps2.setString(4, reason);
            ps2.setString(5, "PENDING");
            ps2.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            ps2.executeUpdate();

            conn.close();

        } catch (Exception e) {
            throw new RemoteException("Error applying leave: " + e.getMessage());
        }
    }

    @Override
    public List<LeaveApplication> getLeaveHistory(String username) throws RemoteException {
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
    public List<LeaveApplication> generateYearlyReport(int employeeId, int year) throws RemoteException {
        List<LeaveApplication> report = new ArrayList<>();

        String sql = "SELECT * FROM leave_applications " +
                "WHERE employee_id = ? AND YEAR(start_date) = ? " +
                "ORDER BY start_date ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (connection == null) {
                throw new RemoteException("Failed to connect to database");
            }

            stmt.setInt(1, employeeId);
            stmt.setInt(2, year);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LeaveApplication leave = new LeaveApplication();

                leave.setLeaveId(rs.getInt("id"));
                leave.setEmployeeId(rs.getInt("employee_id"));
                leave.setStartDate(String.valueOf(rs.getDate("start_date")));
                leave.setEndDate(String.valueOf(rs.getDate("end_date")));
                leave.setStatus(rs.getString("status"));

                report.add(leave);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error generating yearly report");
        }

        return report;
    }

    @Override
    public List<Employee> getAllEmployees() throws RemoteException {
        String sql = "SELECT * FROM employees";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setUsername(rs.getString("username"));
                emp.setPassword(rs.getString("password"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setIcPassport(rs.getString("ic_passport"));
                emp.setFamilyDetails(rs.getString("family_details"));
                emp.setLeaveBalance(rs.getInt("leave_balance"));
                employees.add(emp);
            }

        } catch (Exception e) {
            throw new RemoteException("Error fetching employees: " + e.getMessage(), e);
        }

        return employees;
    }

    @Override
    public void updateEmployee(Employee emp) throws RemoteException {
        // Input validation
        if (emp == null) {
            throw new RemoteException("Employee object cannot be null");
        }
        if (emp.getEmployeeId() <= 0) {
            throw new RemoteException("Invalid employee ID");
        }
        if (emp.getUsername() == null || emp.getUsername().trim().isEmpty()) {
            throw new RemoteException("Username cannot be empty");
        }
        if (emp.getPassword() == null || emp.getPassword().trim().isEmpty()) {
            throw new RemoteException("Password cannot be empty");
        }
        if (emp.getFirstName() == null || emp.getFirstName().trim().isEmpty()) {
            throw new RemoteException("First name cannot be empty");
        }
        if (emp.getLastName() == null || emp.getLastName().trim().isEmpty()) {
            throw new RemoteException("Last name cannot be empty");
        }
        if (emp.getIcPassport() == null || emp.getIcPassport().trim().isEmpty()) {
            throw new RemoteException("IC/Passport cannot be empty");
        }
        if (emp.getLeaveBalance() < 0) {
            throw new RemoteException("Leave balance cannot be negative");
        }

        // Check if employee exists
        String checkSql = "SELECT employee_id FROM employees WHERE employee_id = ?";
        String updateSql = "UPDATE employees SET username = ?, password = ?, first_name = ?, last_name = ?, ic_passport = ?, family_details = ?, leave_balance = ? WHERE employee_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            // Check if employee exists
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, emp.getEmployeeId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new RemoteException("Employee with ID " + emp.getEmployeeId() + " does not exist");
                    }
                }
            }

            // Update employee
            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setString(1, emp.getUsername().trim());
                stmt.setString(2, emp.getPassword());
                stmt.setString(3, emp.getFirstName().trim());
                stmt.setString(4, emp.getLastName().trim());
                stmt.setString(5, emp.getIcPassport().trim());
                stmt.setString(6, emp.getFamilyDetails() != null ? emp.getFamilyDetails().trim() : "");
                stmt.setInt(7, emp.getLeaveBalance());
                stmt.setInt(8, emp.getEmployeeId());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RemoteException("Failed to update employee");
                }
            }

        } catch (Exception e) {
            throw new RemoteException("Error updating employee: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteEmployee(int employeeId) throws RemoteException {
        // Input validation
        if (employeeId <= 0) {
            throw new RemoteException("Invalid employee ID");
        }

        // Check if employee exists
        String checkSql = "SELECT employee_id FROM employees WHERE employee_id = ?";
        String deleteSql = "DELETE FROM employees WHERE employee_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            // Check if employee exists
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, employeeId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new RemoteException("Employee with ID " + employeeId + " does not exist");
                    }
                }
            }

            // Delete employee
            try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
                stmt.setInt(1, employeeId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RemoteException("Failed to delete employee");
                }
            }

        } catch (Exception e) {
            throw new RemoteException("Error deleting employee: " + e.getMessage(), e);
        }
    }

    @Override
    public void setAndUpdateLeaveBalance(int employeeId, int leaveBalance) throws RemoteException {
        // Input validation
        if (employeeId <= 0) {
            throw new RemoteException("Invalid employee ID");
        }
        if (leaveBalance < 0) {
            throw new RemoteException("Leave balance cannot be negative");
        }

        // Check if employee exists
        String checkSql = "SELECT employee_id FROM employees WHERE employee_id = ?";
        String updateSql = "UPDATE employees SET leave_balance = ? WHERE employee_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection == null) {
                throw new RemoteException("Failed to connect to hrm_db");
            }

            // Check if employee exists
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, employeeId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new RemoteException("Employee with ID " + employeeId + " does not exist");
                    }
                }
            }

            // Update leave balance
            try (PreparedStatement stmt = connection.prepareStatement(updateSql)) {
                stmt.setInt(1, leaveBalance);
                stmt.setInt(2, employeeId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RemoteException("Failed to update leave balance");
                }
            }

        } catch (Exception e) {
            throw new RemoteException("Error updating leave balance: " + e.getMessage(), e);
        }
    }
}
