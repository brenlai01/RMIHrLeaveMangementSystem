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
- Demo flow: register employee → view profile

### Member 2 — @AT
- Personal details update module (`updatePersonalDetails()`)
- Family details update module (`updateFamilyDetails()`)
- Demo flow: edit details → verify persisted changes

### Member 3 — @yuan🤩
- Leave balance checking feature (`checkLeaveBalance()`)
- Leave application workflow (`applyLeave()`)
- Demo flow: check balance → apply leave

### Member 4 — @Wan Rou
- Leave history retrieval (`getLeaveHistory()`)
- Leave status tracking and yearly report (`getLeaveStatus()`, `generateYearlyReport()`)
- Demo flow: show history list → check status → display yearly report

### Member 5 / Integration Owner — @Luwit (Brend)
- Authentication and security scope (`login`, `logout`)
- Integration walkthrough (end-to-end module flow, architecture, challenges/fixes)
- Demo flow: secure login/security explanation + full-system handoff

### Suggested Individual Presentation Format (same airtime for everyone)
1. Scope (30s)
2. Key logic / validation (45–60s)
3. Demo flow (45–60s)
4. One challenge faced + fix (30s)

---

## TODO List (Remaining for Team)

- [ ] Implement `HRMServiceImpl` — all database queries (Members 1 & 2)
- [ ] Connect `LoginFrame` to RMI server (Member 3)
- [ ] Implement HR dashboard features including yearly report (Member 3)
- [ ] Implement employee dashboard navigation and leave application logic (Member 4)
- [ ] Implement update personal details and leave history display (Member 5)
- [ ] Create `RegisterEmployeeForm` for HR to register new employees
- [ ] Hash passwords with SHA-256 before storing in database
- [ ] Add SSL/TLS for RMI communication using `javax.net.ssl`
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

> **Passwords:** Sample data in `hrm_database.sql` uses plain-text passwords for development only. Replace with SHA-256 hashed values before any production or submission use.

> **Important:** The GUI client must **NEVER** connect to MySQL directly — all database access must go through the RMI server.

---

## License

This project is for educational purposes as part of a Distributed Computing course assignment.
