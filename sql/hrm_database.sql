-- ============================================
-- HRM System Database Schema
-- Crest Solutions - Distributed Computing Assignment
-- ============================================

CREATE DATABASE IF NOT EXISTS hrm_db;
USE hrm_db;

-- HR Staff Table
CREATE TABLE IF NOT EXISTS hr_staff (
    hr_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,   -- TODO: store hashed password (SHA-256)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employees Table
CREATE TABLE IF NOT EXISTS employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,   -- TODO: store hashed password (SHA-256)
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    ic_passport VARCHAR(50),
    family_details TEXT,
    leave_balance INT DEFAULT 14,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Leave Applications Table
CREATE TABLE IF NOT EXISTS leave_applications (
    leave_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',  -- PENDING, APPROVED, REJECTED
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Sample HR staff account
-- TODO: replace plain text password with hashed version before production
INSERT INTO hr_staff (username, password) VALUES ('admin', 'admin123');

-- Sample Employee account for testing
INSERT INTO employees (username, password, first_name, last_name, ic_passport, leave_balance)
VALUES ('emp001', 'emp123', 'John', 'Doe', 'A1234567', 14);
