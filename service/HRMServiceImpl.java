package service;

import remote.HRMService;
import model.Employee;
import model.LeaveApplication;
import database.DatabaseConnection;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;

// Multithreading: RMI server automatically handles each client call in a separate thread
public class HRMServiceImpl extends UnicastRemoteObject implements HRMService {

    public HRMServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        // TODO: query hr_staff and employees table, verify credentials
        return false;
    }

    @Override
    public String getUserRole(String username) throws RemoteException {
        // TODO: check hr_staff table first, then employees table
        return null;
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
