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

---

## Prerequisites

- **Java JDK 8+**
- **MySQL 8+**
- **mysql-connector-java JAR** (e.g., `mysql-connector-j-8.x.x.jar`)

---

## Setup Instructions

1. **Import SQL Schema**
   - Open MySQL Workbench or MySQL CLI
   - Run: `source sql/hrm_database.sql`

2. **Configure Database Connection**
   - Open `database/DatabaseConnection.java`
   - Update the `PASSWORD` constant to match your MySQL root password

3. **Add MySQL Connector to Classpath**
   - Download `mysql-connector-java` JAR
   - Place it in the project root or a `lib/` directory

4. **Compile the Project**
   ```bash
   javac -cp .:lib/mysql-connector-j-8.x.x.jar \
       model/*.java remote/*.java database/*.java \
       service/*.java server/*.java client/*.java
   ```

5. **Run the RMI Server**
   ```bash
   java -cp .:lib/mysql-connector-j-8.x.x.jar server.HRMServer
   ```

6. **Run the Client**
   ```bash
   java -cp .:lib/mysql-connector-j-8.x.x.jar client.LoginFrame
   ```

---

## Team Task Breakdown

| Member   | Owns / Responsible For                                                       |
|----------|------------------------------------------------------------------------------|
| Member 1 | `service/HRMServiceImpl.java` — login, getUserRole, registerEmployee         |
| Member 2 | `service/HRMServiceImpl.java` — leave balance, applyLeave, leave history     |
| Member 3 | `client/LoginFrame.java`, `client/HRDashboard.java`                          |
| Member 4 | `client/EmployeeDashboard.java`, `client/ApplyLeaveForm.java`                |
| Member 5 | `client/UpdatePersonalDetailsForm.java`, `client/ViewLeaveHistoryForm.java`  |

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

## Multithreading in Java RMI

This project does **not** manually create any threads (no `new Thread(...)` or `ExecutorService` calls).  
Multithreading is provided automatically by the **Java RMI runtime**.

How it works:

```
Client 1 (Employee PC)  ──→ ┐
Client 2 (HR Laptop)    ──→ ├──→ RMI Server (HRMServer)
Client 3 (Employee PC)  ──→ ┘         │
                                       ▼
                          ┌─────────────────────────────┐
                          │  RMI Internal Thread Pool    │
                          │                             │
                          │  Thread-1 → login()         │
                          │  Thread-2 → checkLeaveBalance() │
                          │  Thread-3 → login()         │
                          └─────────────────────────────┘
                                       │
                                  HRMServiceImpl
                                       │
                                  MySQL Database
```

Key points:

- When a client calls a remote method, the RMI runtime picks a thread from its **internal thread pool** and executes the method on that thread.
- Each concurrent client call therefore runs on a **different thread**; this is visible from the `[THREAD]` log lines printed by `HRMServiceImpl`.  
  Example output when two clients call `login()` simultaneously:
  ```
  [THREAD] RMI TCP Connection(1)-127.0.0.1 handling login()
  [THREAD] RMI TCP Connection(2)-127.0.0.1 handling login()
  ```
- Because multiple threads share the single `HRMServiceImpl` instance, methods that modify shared state (e.g. `applyLeave()`) should be marked `synchronized` to prevent race conditions when they are implemented.
- This design is standard in industry distributed systems — developers write normal sequential code and the RMI/NIO layer handles concurrency transparently.

---



> **SSL/TLS:** The server and client contain TODO comments for adding SSL/TLS socket factories to the RMI connection. This should be confirmed with the lecturer and **implemented last**, after all core business logic is complete.

> **Passwords:** Sample data in `hrm_database.sql` uses plain-text passwords for development only. Replace with SHA-256 hashed values before any production or submission use.

> **Important:** The GUI client must **NEVER** connect to MySQL directly — all database access must go through the RMI server.

---

## License

This project is for educational purposes as part of a Distributed Computing course assignment.
