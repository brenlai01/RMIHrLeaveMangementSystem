package client;

import remote.HRMService;
import model.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeManagementForm extends JFrame {

    private HRMService service;
    private String username;

    // Form fields
    private JTextField employeeIdField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField icPassportField;
    private JTextField familyDetailsField;
    private JTextField leaveBalanceField;

    // Buttons
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton backButton;

    // Table
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeManagementForm(HRMService service, String username) {
        super("Employee Management");
        this.service = service;
        this.username = username;
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadEmployees();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Left panel - Employee form
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Employee ID (read-only, auto-generated)
        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("Employee ID:"), gbc);
        employeeIdField = new JTextField(20);
        employeeIdField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 0;
        leftPanel.add(employeeIdField, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        leftPanel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        leftPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        leftPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        leftPanel.add(passwordField, gbc);

        // First Name
        gbc.gridx = 0; gbc.gridy = 3;
        leftPanel.add(new JLabel("First Name:"), gbc);
        firstNameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3;
        leftPanel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 4;
        leftPanel.add(new JLabel("Last Name:"), gbc);
        lastNameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        leftPanel.add(lastNameField, gbc);

        // IC/Passport
        gbc.gridx = 0; gbc.gridy = 5;
        leftPanel.add(new JLabel("IC/Passport:"), gbc);
        icPassportField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 5;
        leftPanel.add(icPassportField, gbc);

        // Family Details
        gbc.gridx = 0; gbc.gridy = 6;
        leftPanel.add(new JLabel("Family Details:"), gbc);
        familyDetailsField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 6;
        leftPanel.add(familyDetailsField, gbc);

        // Leave Balance
        gbc.gridx = 0; gbc.gridy = 7;
        leftPanel.add(new JLabel("Leave Balance:"), gbc);
        leaveBalanceField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 7;
        leftPanel.add(leaveBalanceField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        createButton = new JButton("Create");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        createButton.addActionListener(e -> handleCreate());
        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        leftPanel.add(buttonPanel, gbc);

        // Back button
        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            dispose();
            new HRDashboard(service, username).setVisible(true);
        });
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        leftPanel.add(backButton, gbc);

        // Right panel - Employee table
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Employee List"));

        String[] columns = {"ID", "Username", "First Name", "Last Name", "IC/Passport", "Family Details", "Leave Balance"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = service.getAllEmployees();
            tableModel.setRowCount(0);

            for (Employee emp : employees) {
                Object[] row = {
                    emp.getEmployeeId(),
                    emp.getUsername(),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getIcPassport(),
                    emp.getFamilyDetails(),
                    emp.getLeaveBalance()
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading employees: " + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleTableSelection() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            employeeIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            usernameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            firstNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            lastNameField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            icPassportField.setText(tableModel.getValueAt(selectedRow, 4).toString());

            Object familyDetails = tableModel.getValueAt(selectedRow, 5);
            familyDetailsField.setText(familyDetails != null ? familyDetails.toString() : "");

            leaveBalanceField.setText(tableModel.getValueAt(selectedRow, 6).toString());
            passwordField.setText("");
        }
    }

    private void handleCreate() {
        if (!validateFields(true)) {
            return;
        }

        try {
            Employee emp = new Employee();
            emp.setUsername(usernameField.getText().trim());
            emp.setPassword(new String(passwordField.getPassword()));
            emp.setFirstName(firstNameField.getText().trim());
            emp.setLastName(lastNameField.getText().trim());
            emp.setIcPassport(icPassportField.getText().trim());
            emp.setFamilyDetails(familyDetailsField.getText().trim());
            emp.setLeaveBalance(Integer.parseInt(leaveBalanceField.getText().trim()));

            service.registerEmployee(emp);
            JOptionPane.showMessageDialog(this,
                    "Employee created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            loadEmployees();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error creating employee: " + e.getMessage(),
                    "Create Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        if (employeeIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee from the table to update.",
                    "Update Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateFields(false)) {
            return;
        }

        try {
            Employee emp = new Employee();
            emp.setEmployeeId(Integer.parseInt(employeeIdField.getText()));
            emp.setUsername(usernameField.getText().trim());

            String password = new String(passwordField.getPassword());
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Password cannot be empty. Please enter the password.",
                        "Update Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            emp.setPassword(password);

            emp.setFirstName(firstNameField.getText().trim());
            emp.setLastName(lastNameField.getText().trim());
            emp.setIcPassport(icPassportField.getText().trim());
            emp.setFamilyDetails(familyDetailsField.getText().trim());
            emp.setLeaveBalance(Integer.parseInt(leaveBalanceField.getText().trim()));

            service.updateEmployee(emp);
            JOptionPane.showMessageDialog(this,
                    "Employee updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            loadEmployees();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating employee: " + e.getMessage(),
                    "Update Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        if (employeeIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee from the table to delete.",
                    "Delete Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this employee?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                service.deleteEmployee(employeeId);
                JOptionPane.showMessageDialog(this,
                        "Employee deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                loadEmployees();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting employee: " + e.getMessage(),
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateFields(boolean isCreate) {
        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (isCreate && new String(passwordField.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Password is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "First Name is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Last Name is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (icPassportField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "IC/Passport is required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            int leaveBalance = Integer.parseInt(leaveBalanceField.getText().trim());
            if (leaveBalance < 0) {
                JOptionPane.showMessageDialog(this,
                        "Leave Balance cannot be negative.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Leave Balance must be a valid number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearFields() {
        employeeIdField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        icPassportField.setText("");
        familyDetailsField.setText("");
        leaveBalanceField.setText("");
        employeeTable.clearSelection();
    }
}
