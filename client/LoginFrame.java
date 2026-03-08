package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginFrame extends JFrame {

    // RMI remote object
    private HRMService service;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginFrame() {
        super("HRM System - Crest Solutions | Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // TODO: [SECURITY - implement last] Use SSL/TLS socket factory before RMI lookup
        // TODO: Connect to RMI server: service = (HRMService) Naming.lookup("rmi://localhost:1099/HRMService")

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passwordField, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(statusLabel, gbc);

        loginButton.addActionListener(e -> handleLogin());

        // TODO: style components as needed
        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // TODO: call service.login(username, password)
        // TODO: call service.getUserRole(username) to determine which dashboard to open
        // TODO: if role is "HR" open HRDashboard, if "EMPLOYEE" open EmployeeDashboard
        // TODO: show error message on failed login
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}
