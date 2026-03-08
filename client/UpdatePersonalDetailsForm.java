package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;

public class UpdatePersonalDetailsForm extends JFrame {

    private HRMService service;
    private String username;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField icPassportField;
    private JButton updateButton;

    public UpdatePersonalDetailsForm(HRMService service, String username) {
        super("Update Personal Details");
        this.service = service;
        this.username = username;
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("First Name:"), gbc);

        firstNameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Last Name:"), gbc);

        lastNameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("IC/Passport:"), gbc);

        icPassportField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(icPassportField, gbc);

        updateButton = new JButton("Update");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(updateButton, gbc);

        // TODO: pre-populate fields with current data from service.getEmployeeDetails(username)
        updateButton.addActionListener(e -> handleUpdate());

        add(panel);
    }

    private void handleUpdate() {
        // TODO: create updated Employee object
        // TODO: call service.updatePersonalDetails(emp)
        // TODO: show success or error message
    }
}
