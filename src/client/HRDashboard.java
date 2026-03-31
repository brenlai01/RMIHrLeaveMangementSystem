package client;

import model.Employee;
import remote.HRMService;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

// HR Staff dashboard - accessible only after HR login
public class HRDashboard extends JFrame {

    private HRMService service;
    private String username;
    private JButton employeeManagementButton;
    private JButton yearlyReportButton;
    private JButton checkLeaveButton;
    private JButton logoutButton;

    public HRDashboard(HRMService service, String username) {
        super("HRM System - HR Dashboard");
        this.service = service;
        this.username = username;
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        employeeManagementButton = new JButton("Employee Management");
        employeeManagementButton.addActionListener(e -> {
            dispose();
            new EmployeeManagementForm(service, username).setVisible(true);
        });

        yearlyReportButton = new JButton("Generate Yearly Leave Report");
        yearlyReportButton.addActionListener(e -> {
            List<Employee> employees;

            try {
                employees = service.getAllEmployees();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }

            String[] usernames = new String[employees.size()];

            for (int i = 0; i < employees.size(); i++) {
                usernames[i] = employees.get(i).getUsername();
            }

            JComboBox<String> employeeComboBox = new JComboBox<>(usernames);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    employeeComboBox,
                    "Select Employee",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                String selectedEmployee = (String) employeeComboBox.getSelectedItem();

                if (selectedEmployee != null) {
                    try {
                        new YearlyLeaveReport(service, selectedEmployee).setVisible(true);
                        dispose();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        checkLeaveButton = new JButton("Check Leave Applications");
        checkLeaveButton.addActionListener(e -> {
            dispose();
            new CheckLeaveForm(service, username).setVisible(true);
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panel.add(employeeManagementButton);
        panel.add(yearlyReportButton);
        panel.add(checkLeaveButton);
        panel.add(logoutButton);

        add(panel);
    }
}
