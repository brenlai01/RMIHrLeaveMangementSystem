package remote;

import model.Employee;
import model.LeaveApplication;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/** RMI Remote Interface - defines all services available to clients over the network */
public interface HRMService extends Remote {

    boolean login(String username, String password) throws RemoteException;

    // returns "HR" or "EMPLOYEE"
    String getUserRole(String username) throws RemoteException;

    void registerEmployee(Employee emp) throws RemoteException;

    Employee getEmployeeDetails(String username) throws RemoteException;

    void updatePersonalDetails(Employee emp) throws RemoteException;

    void updateFamilyDetails(String username, String familyDetails) throws RemoteException;

    int checkLeaveBalance(String username) throws RemoteException;

    void applyLeave(LeaveApplication application) throws RemoteException;

    List<LeaveApplication> getLeaveHistory(String username) throws RemoteException;

    List<LeaveApplication> getLeaveStatus(String username) throws RemoteException;

    List<LeaveApplication> generateYearlyReport(int year) throws RemoteException;
}
