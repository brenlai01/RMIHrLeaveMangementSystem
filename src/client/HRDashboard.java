package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;

// HR Staff dashboard - accessible only after HR login
public class HRDashboard extends JFrame {

    private HRMService service;
    private String username;
    private JButton employeeManagementButton;
    private JButton yearlyReportButton;
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
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        employeeManagementButton = new JButton("Employee Management");
        employeeManagementButton.addActionListener(e -> {
            dispose();
            new EmployeeManagementForm(service, username).setVisible(true);
        });

        yearlyReportButton = new JButton("Generate Yearly Leave Report");
        yearlyReportButton.addActionListener(e -> {
            // TODO: call service.generateYearlyReport(year) and display
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panel.add(employeeManagementButton);
        panel.add(yearlyReportButton);
        panel.add(logoutButton);

        add(panel);
    }
}
