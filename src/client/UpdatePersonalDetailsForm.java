package client;

import model.Employee;
import remote.HRMService;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.function.Consumer;

public class UpdatePersonalDetailsForm extends JFrame {

    private HRMService service;
    private String username;
    private Employee currentEmployee;
    private Consumer<String> usernameUpdateListener;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField icPassportField;
    private JButton updateButton;
    private JButton backButton;

    public UpdatePersonalDetailsForm(HRMService service, String username) {
        this(service, username, null);
    }

    public UpdatePersonalDetailsForm(HRMService service, String username, Consumer<String> usernameUpdateListener) {
        super("Update Personal Details");
        this.service = service;
        this.username = username;
        this.usernameUpdateListener = usernameUpdateListener;
        setSize(480, 380);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel header = new JLabel("Update Account Details", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(header, gbc);

        gbc.gridy = 1;
        JLabel signedIn = new JLabel("Signed in as: " + username, SwingConstants.CENTER);
        panel.add(signedIn, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("New Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("New Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("First Name:"), gbc);

        firstNameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Last Name:"), gbc);

        lastNameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("IC/Passport:"), gbc);

        icPassportField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(icPassportField, gbc);

        updateButton = new JButton("Update");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(updateButton, gbc);

        backButton = new JButton("Back to Dashboard");
        gbc.gridy = 8;
        panel.add(backButton, gbc);

        loadEmployeeDetails();
        updateButton.addActionListener(e -> handleUpdate());
        backButton.addActionListener(e -> dispose());

        add(panel);
    }

    private void handleUpdate() {
        String newUsername = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String icPassport = icPassportField.getText().trim();

        if (newUsername.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || icPassport.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentEmployee == null || currentEmployee.getEmployeeId() <= 0) {
            JOptionPane.showMessageDialog(this, "Cannot update without loaded employee details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Employee updated = new Employee();
        updated.setEmployeeId(currentEmployee.getEmployeeId());
        updated.setUsername(newUsername);
        updated.setPassword(password);
        updated.setFirstName(firstName);
        updated.setLastName(lastName);
        updated.setIcPassport(icPassport);

        try {
            service.updatePersonalDetails(updated);
            currentEmployee = updated;
            this.username = newUsername;
            if (usernameUpdateListener != null) {
                usernameUpdateListener.accept(newUsername);
            }
            JOptionPane.showMessageDialog(this, "Personal details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Failed to update personal details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployeeDetails() {
        try {
            currentEmployee = service.getEmployeeDetails(username);
            if (currentEmployee != null) {
                usernameField.setText(currentEmployee.getUsername());
                passwordField.setText(currentEmployee.getPassword());
                firstNameField.setText(currentEmployee.getFirstName());
                lastNameField.setText(currentEmployee.getLastName());
                icPassportField.setText(currentEmployee.getIcPassport());
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load personal details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
