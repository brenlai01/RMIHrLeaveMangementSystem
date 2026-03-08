package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;

public class EmployeeDashboard extends JFrame {

    private HRMService service;
    private String username;
    private JButton viewPersonalDetailsButton;
    private JButton updatePersonalDetailsButton;
    private JButton updateFamilyDetailsButton;
    private JButton checkLeaveBalanceButton;
    private JButton applyLeaveButton;
    private JButton viewLeaveStatusButton;
    private JButton viewLeaveHistoryButton;
    private JButton logoutButton;

    public EmployeeDashboard(HRMService service, String username) {
        super("HRM System - Employee Dashboard");
        this.service = service;
        this.username = username;
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        viewPersonalDetailsButton = new JButton("View Personal Details");
        viewPersonalDetailsButton.addActionListener(e -> {
            // TODO: call service.getEmployeeDetails(username) and display
        });

        updatePersonalDetailsButton = new JButton("Update Personal Details");
        updatePersonalDetailsButton.addActionListener(e -> {
            // TODO: open UpdatePersonalDetailsForm
        });

        updateFamilyDetailsButton = new JButton("Update Family Details");
        updateFamilyDetailsButton.addActionListener(e -> {
            // TODO: call service.updateFamilyDetails(...)
        });

        checkLeaveBalanceButton = new JButton("Check Leave Balance");
        checkLeaveBalanceButton.addActionListener(e -> {
            // TODO: call service.checkLeaveBalance(username) and display
        });

        applyLeaveButton = new JButton("Apply for Leave");
        applyLeaveButton.addActionListener(e -> {
            // TODO: open ApplyLeaveForm
        });

        viewLeaveStatusButton = new JButton("View Leave Status");
        viewLeaveStatusButton.addActionListener(e -> {
            // TODO: call service.getLeaveStatus(username) and display
        });

        viewLeaveHistoryButton = new JButton("View Leave History");
        viewLeaveHistoryButton.addActionListener(e -> {
            // TODO: open ViewLeaveHistoryForm
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            // TODO: close window, reopen LoginFrame
        });

        panel.add(viewPersonalDetailsButton);
        panel.add(updatePersonalDetailsButton);
        panel.add(updateFamilyDetailsButton);
        panel.add(checkLeaveBalanceButton);
        panel.add(applyLeaveButton);
        panel.add(viewLeaveStatusButton);
        panel.add(viewLeaveHistoryButton);
        panel.add(logoutButton);

        add(panel);
    }
}
