package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import security.SslPropertyUtil;

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

        try {
            configureSslProperties();
            String host = System.getProperty("hrm.rmi.host", "localhost");
            Registry registry = LocateRegistry.getRegistry(host, 1099, new SslRMIClientSocketFactory());
            service = (HRMService) registry.lookup("HRMService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to RMI server: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        initComponents();
    }

    private void configureSslProperties() {
        String trustStorePath = SslPropertyUtil.requireProperty("hrm.ssl.truststore");
        String trustStorePassword = SslPropertyUtil.requireProperty("hrm.ssl.truststore.password");
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
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

        getRootPane().setDefaultButton(loginButton);

        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        try {
            String role = service.login(username, password);

            if (role == null) {
                statusLabel.setText("Invalid username or password.");
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();

            if (role.equals("HR")) {
                new HRDashboard(service, username).setVisible(true);
            } else  {
                new EmployeeDashboard(service, username).setVisible(true);
            }

        } catch (Exception e) {
            statusLabel.setText("Login error occurred.");
            JOptionPane.showMessageDialog(this,
                    "Error during login: " + e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}
