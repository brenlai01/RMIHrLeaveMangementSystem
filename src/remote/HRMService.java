package remote;

import model.Employee;
import model.LeaveApplication;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/** RMI Remote Interface - defines all services available to clients over the network */
public interface HRMService extends Remote {

    // will return user role (HR or Employee) on successful login, null if credentials are invalid
    String login(String username, String password) throws RemoteException;

    void registerEmployee(Employee emp) throws RemoteException;

    Employee getEmployeeDetails(String username) throws RemoteException;

    void updatePersonalDetails(Employee emp) throws RemoteException;

    void updateFamilyDetails(String username, String familyDetails) throws RemoteException;

    int checkLeaveBalance(String username) throws RemoteException;

    void applyLeave(LeaveApplication application) throws RemoteException;

    List<LeaveApplication> getLeaveHistory(String username) throws RemoteException;

    List<LeaveApplication> getLeaveStatus(String username) throws RemoteException;

    List<LeaveApplication> generateYearlyReport(int year) throws RemoteException;

    List<Employee> getAllEmployees() throws RemoteException;

    void updateEmployee(Employee emp) throws RemoteException;

    void deleteEmployee(int employeeId) throws RemoteException;

    void setAndUpdateLeaveBalance(int employeeId, int leaveBalance) throws RemoteException;
}
