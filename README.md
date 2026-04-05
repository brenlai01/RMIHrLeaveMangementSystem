# RMI HR Leave Management System — Crest Solutions

A Three-Tier Distributed HR Leave Management System built with Java RMI, Java Swing GUI, and MySQL, developed as a group assignment for a Distributed Computing course.

---

## Description

This system allows HR staff to manage employee leave applications and allows employees to apply for leave, view their leave history, and update personal details. All communication between the client GUI and the MySQL database is routed through a Java RMI server, ensuring a clean three-tier architecture.

---

## Three-Tier Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT TIER (Tier 1)                        │
│        Java Swing GUI (LoginFrame, EmployeeDashboard, etc.)         │
│              Communicates via Java RMI (TCP/IP)                     │
└───────────────────────────┬─────────────────────────────────────────┘
                            │  RMI over TCP/IP (port 1099)
┌───────────────────────────▼─────────────────────────────────────────┐
│                      APPLICATION TIER (Tier 2)                      │
│         Java RMI Server (HRMServer + HRMServiceImpl)                │
│              Communicates via JDBC (TCP/IP)                         │
└───────────────────────────┬─────────────────────────────────────────┘
                            │  JDBC over TCP/IP (port 3306)
┌───────────────────────────▼─────────────────────────────────────────┐
│                         DATA TIER (Tier 3)                          │
│                     MySQL Database (hrm_db)                         │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Project Structure

```
RMIHrLeaveMangementSystem/
│
├── model/
│   ├── Employee.java
│   └── LeaveApplication.java
│
├── remote/
│   └── HRMService.java
│
├── service/
│   └── HRMServiceImpl.java
│
├── server/
│   └── HRMServer.java
│
├── database/
│   └── DatabaseConnection.java
│
├── client/
│   ├── LoginFrame.java
│   ├── EmployeeDashboard.java
│   ├── HRDashboard.java
│   ├── ApplyLeaveForm.java
│   ├── CheckLeaveForm.java
│   ├── EmployeeManagementForm.java
│   ├── UpdateFamilyDetailsForm.java
│   ├── UpdatePersonalDetailsForm.java
│   ├── ViewLeaveHistoryForm.java
│   ├── ViewPersonalDetailsForm.java
│   └── YearlyLeaveReport.java
│
├── sql/
│   └── hrm_database.sql
│
└── README.md

```

## Team Task Breakdown

| Member   | Owns / Responsible For                                                       |
|----------|------------------------------------------------------------------------------|
| Member 1 | `service/HRMServiceImpl.java` — login, getUserRole, registerEmployee         |
| Member 2 | `service/HRMServiceImpl.java` — leave balance, applyLeave, leave history     |
| Member 3 | `client/LoginFrame.java`, `client/HRDashboard.java`                          |
| Member 4 | `client/EmployeeDashboard.java`, `client/ApplyLeaveForm.java`                |
| Member 5 | `client/UpdatePersonalDetailsForm.java`, `client/ViewLeaveHistoryForm.java`  |

---

## Gap Analysis & Remaining Work

> **Assessment of current `main` branch.** The claim "only SSL/TLS remains" is **accurate** — all core business logic and UI wiring is now complete. The only outstanding item is SSL/TLS for RMI transport security.

### ✅ Fully Implemented

| Component | Notes |
|---|---|
| `HRMServer.java` | RMI registry starts on port 1099, binds service |
| `HRMService.java` (remote interface) | All method signatures declared |
| `DatabaseConnection.java` | JDBC connection to `hrm_db` |
| `Employee.java` / `LeaveApplication.java` | Serializable models with all fields, getters/setters |
| `HRMServiceImpl.login()` | Checks `hr_staff` then `employees` tables |
| `HRMServiceImpl.registerEmployee()` | Full validation + INSERT |
| `HRMServiceImpl.getEmployeeDetails()` | SELECT by username |
| `HRMServiceImpl.updatePersonalDetails()` | Validation + duplicate check + UPDATE |
| `HRMServiceImpl.updateFamilyDetails()` | Existence check + UPDATE |
| `HRMServiceImpl.checkLeaveBalance()` | SELECT leave_balance |
| `HRMServiceImpl.applyLeave()` | INSERT leave_applications |
| `HRMServiceImpl.getLeaveHistory()` | SELECT all leaves by employee |
| `HRMServiceImpl.getLeaveStatus()` | SELECT all leaves by employee (ordered by date) |
| `HRMServiceImpl.getPendingLeaveApplications()` | SELECT WHERE status = PENDING |
| `HRMServiceImpl.updateLeaveStatus()` | UPDATE leave status (APPROVED/REJECTED) |
| `HRMServiceImpl.generateYearlyReport()` | SELECT by employee + year |
| `HRMServiceImpl.getAllEmployees()` | SELECT all employees |
| `HRMServiceImpl.updateEmployee()` | Full validation + UPDATE |
| `HRMServiceImpl.deleteEmployee()` | Existence check + DELETE |
| `HRMServiceImpl.setAndUpdateLeaveBalance()` | Validation + UPDATE leave_balance |
| `LoginFrame.java` | RMI lookup + login + role-based redirect |
| `HRDashboard.java` | All 3 buttons wired (Employee Management, Yearly Report, Check Leave) |
| `EmployeeDashboard.java` | All 7 feature buttons wired |
| `ApplyLeaveForm.java` | Date pickers, validation, two-thread submit logic |
| `UpdatePersonalDetailsForm.java` | Pre-populated fields + handleUpdate() |
| `UpdateFamilyDetailsForm.java` | Pre-populated fields + handleSave() |
| `ViewLeaveHistoryForm.java` | Loads and displays leave history in table |
| `EmployeeManagementForm.java` | Full CRUD (Create/Update/Delete) + table view |
| `CheckLeaveForm.java` | Lists pending leaves with Approve/Reject actions |
| `YearlyLeaveReport.java` | Table + summary (Total/Approved/Rejected/Pending) |
| `hrm_database.sql` | 3 tables + sample data |

### 🔴 One Remaining Functional Gap

| Item | Effort | Priority |
|---|---|---|
| **SSL/TLS for RMI** — TODOs exist in `HRMServer.java` and `LoginFrame.java` | L | Implement last (after lecturer confirmation) |

### ⚠️ Known Issues / Code Quality

| Issue | File | Severity |
|---|---|---|
| Passwords stored as plain text in DB (`admin123`, `emp123`) | `hrm_database.sql` | Medium — acceptable for development/assignment, must be hashed for any real deployment |
| DB credentials hardcoded (`root`/`root`) | `DatabaseConnection.java` | Low — each developer should use their own local credentials; consider `.gitignore` |

---

## Features

| Feature                          | HR Staff | Employee |
|----------------------------------|----------|----------|
| Login                            | ✔        | ✔        |
| Register Employee                | ✔        |          |
| Update Employee Details          | ✔        |          |
| Delete Employee                  | ✔        |          |
| Set Employee Leave Balance       | ✔        |          |
| View All Employees               | ✔        |          |
| Approve / Reject Leave           | ✔        |          |
| Generate Yearly Leave Report     | ✔        |          |
| View Personal Details            |          | ✔        |
| Update Personal Details          |          | ✔        |
| Update Family Details            |          | ✔        |
| Check Leave Balance              |          | ✔        |
| Apply for Leave                  |          | ✔        |
| View Leave Status                |          | ✔        |
| View Leave History               |          | ✔        |

---

## Multithreading

Multithreading is present in **two places**:

1. **RMI runtime (implicit):** Every incoming client call to `HRMServiceImpl` is dispatched on a separate RMI-managed thread. This happens automatically — no explicit thread creation is needed in your code. Each method in `HRMServiceImpl` is a potential concurrent execution point.

2. **`ApplyLeaveForm.java` (explicit):** Two application-level threads are used when submitting leave:
   - `checkThread` — calculates requested days and fetches current leave balance via RMI
   - `applyThread` — waits for `checkThread` to finish (`checkThread.join()`), validates balance, then calls `service.applyLeave()` over RMI

   This demonstrates deliberate use of `java.lang.Thread` and thread coordination (`join()`).

---

## How to Run

### Prerequisites
- Java 11+
- MySQL running on `localhost:3306`
- Maven

### Setup
1. Run `sql/hrm_database.sql` in your MySQL client to create the schema and sample data.
2. Update `src/database/DatabaseConnection.java` with your MySQL username and password.

### Start the Server
```bash
mvn compile
mvn exec:java -Dexec.mainClass="server.HRMServer"
```

### Start the Client
```bash
mvn exec:java -Dexec.mainClass="client.LoginFrame"
```

### Sample Credentials
| Role     | Username | Password |
|----------|----------|----------|
| HR Staff | admin    | admin123 |
| Employee | emp001   | emp123   |

---

## Security Notes

> **SSL/TLS:** The server (`HRMServer.java`) and client (`LoginFrame.java`) contain `TODO` comments for adding SSL/TLS socket factories to the RMI connection. This should be confirmed with the lecturer and **implemented last**, after all core business logic is complete.

> **Passwords:** Sample data in `hrm_database.sql` uses plain-text passwords for development only. The SQL schema includes TODO comments to replace these with SHA-256 hashed values before any production or submission use.

> **Important:** The GUI client must **NEVER** connect to MySQL directly — all database access must go through the RMI server.

---

## License

This project is for educational purposes as part of a Distributed Computing course assignment.
