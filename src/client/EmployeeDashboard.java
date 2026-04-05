package client;

import model.Employee;
import remote.HRMService;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class EmployeeDashboard extends JFrame {

    private HRMService service;
    private String username;
    private JButton viewPersonalDetailsButton;
    private JButton updatePersonalDetailsButton;
    private JButton updateFamilyDetailsButton;
    private JButton applyLeaveButton;
    private JButton viewLeaveStatusButton;
    private JButton viewLeaveHistoryButton;
    private JButton logoutButton;
    private JLabel leaveBalanceLabel;

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

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        leaveBalanceLabel = new JLabel("Leave Balance: Loading...");

        headerPanel.add(leaveBalanceLabel);
        add(headerPanel, BorderLayout.NORTH);
        loadLeaveBalance();

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        viewPersonalDetailsButton = new JButton("View Personal Details");
        viewPersonalDetailsButton.addActionListener(e -> {
            ViewPersonalDetailsForm form = new ViewPersonalDetailsForm(service, username);
            form.setVisible(true);
        });

        updatePersonalDetailsButton = new JButton("Update Personal Details");
        updatePersonalDetailsButton.addActionListener(e -> {
            UpdatePersonalDetailsForm form = new UpdatePersonalDetailsForm(service, username, updated -> this.username = updated);
            form.setVisible(true);
        });

        updateFamilyDetailsButton = new JButton("Update Family Details");
        updateFamilyDetailsButton.addActionListener(e -> {
            UpdateFamilyDetailsForm form = new UpdateFamilyDetailsForm(service, username);
            form.setVisible(true);
        });

        applyLeaveButton = new JButton("Apply for Leave");
        applyLeaveButton.addActionListener(e -> {
            new ApplyLeaveForm(service, username).setVisible(true);
            this.setVisible(false);
        });

        viewLeaveStatusButton = new JButton("View Leave Status");
        viewLeaveStatusButton.addActionListener(e -> {
            // TODO: call service.getLeaveStatus(username) and display
        });

        viewLeaveHistoryButton = new JButton("View Leave History");
        viewLeaveHistoryButton.addActionListener(e -> {
            new ViewLeaveHistoryForm(service, username).setVisible(true);
            this.setVisible(false);
        });

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panel.add(viewPersonalDetailsButton);
        panel.add(updatePersonalDetailsButton);
        panel.add(updateFamilyDetailsButton);
        panel.add(applyLeaveButton);
        panel.add(viewLeaveStatusButton);
        panel.add(viewLeaveHistoryButton);
        panel.add(logoutButton);

        add(panel);
    }

    private void loadLeaveBalance() {
        try {
            int balance = service.checkLeaveBalance(username);
            leaveBalanceLabel.setText("Leave Balance: " + balance + " days");
        } catch (Exception ex) {
            leaveBalanceLabel.setText("Leave Balance: N/A");
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
