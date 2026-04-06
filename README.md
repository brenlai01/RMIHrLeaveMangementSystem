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
│   ├── UpdatePersonalDetailsForm.java
│   └── ViewLeaveHistoryForm.java
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

## Individual Presentation Scope (What We Implemented)

### Member 1 — @DingDingDing
- Employee registration flow (`registerEmployee()`)
- Employee profile retrieval (`getEmployeeDetails()`)
- Leave balance initialization and update logic (`setAndUpdateLeaveBalance()`)
- Demo flow: create employee → view details → show leave balance update

### Member 2 — @AT
- Personal details update module (`updatePersonalDetails()`)
- Family details update module (`updateFamilyDetails()`)
- Input validation and update behavior for employee profile changes
- Demo flow: edit personal/family details → verify persisted changes

### Member 3 — @yuan🤩
- Leave balance checking feature (`checkLeaveBalance()`)
- Leave application workflow (`applyLeave()`)
- Leave request validation (balance check and request data validation)
- Demo flow: check balance → apply leave → verify request submission/state

### Member 4 — @Wan Rou
- Leave history retrieval (`getLeaveHistory()`)
- Leave status tracking (`getLeaveStatus()`)
- Yearly leave report generation (`generateYearlyReport()`)
- Demo flow: show history list → check leave status → display yearly report

### Member 5 / Integration Owner — @Luwit (Brend)
- Authentication flow (`login`, `logout`)
- SSL/TLS setup scope for secure client-server communication
- Security-related implementation choices for session/auth flow
- Demo flow: secure login/logout + SSL/TLS configuration explanation
- Integration of all member modules into one end-to-end workflow
- System architecture and responsibility mapping across components
- Integration challenges encountered and fixes applied
- Final full-system walkthrough and conclusion

### Suggested Individual Presentation Format (2–4 mins each)
1. Scope
2. What was implemented
3. Key logic / validation
4. Demo flow
5. Challenge faced + fix

---

## TODO List (Remaining for Team)

- [ ] Implement `HRMServiceImpl` — all database queries (Members 1 & 2)
- [ ] Connect `LoginFrame` to RMI server (Member 3)
- [ ] Implement HR dashboard features including yearly report (Member 3)
- [ ] Implement employee dashboard navigation and leave application logic (Member 4)
- [ ] Implement update personal details and leave history display (Member 5)
- [ ] Create `RegisterEmployeeForm` for HR to register new employees
- [ ] Hash passwords with SHA-256 before storing in database
- [ ] **[SECURITY — confirm with lecturer first]** Add SSL/TLS for RMI communication using `javax.net.ssl`
- [ ] Replace hard-coded database credentials with a config file or environment variable

---

## Features

| Feature                          | HR Staff | Employee |
|----------------------------------|----------|----------|
| Login                            | ✔        | ✔        |
| Register Employee                | ✔        |          |
| Set Employee Number of Leaves    | ✔        |          |
| View All Employees               | ✔        |          |
| Generate Yearly Leave Report     | ✔        |          |
| View Personal Details            |          | ✔        |
| Update Personal Details          |          | ✔        |
| Update Family Details            |          | ✔        |
| Check Leave Balance              |          | ✔        |
| Apply for Leave                  |          | ✔        |
| View Leave Status                |          | ✔        |
| View Leave History               |          | ✔        |

---

## Security Notes

> **SSL/TLS:** The server and client contain TODO comments for adding SSL/TLS socket factories to the RMI connection. This should be confirmed with the lecturer and **implemented last**, after all core business logic is complete.

> **Passwords:** Sample data in `hrm_database.sql` uses plain-text passwords for development only. Replace with SHA-256 hashed values before any production or submission use.

> **Important:** The GUI client must **NEVER** connect to MySQL directly — all database access must go through the RMI server.

---

## License

This project is for educational purposes as part of a Distributed Computing course assignment.
