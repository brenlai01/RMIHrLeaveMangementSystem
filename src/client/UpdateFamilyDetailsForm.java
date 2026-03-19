package client;

import model.Employee;
import remote.HRMService;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class UpdateFamilyDetailsForm extends JFrame {

    private final HRMService service;
    private final String username;
    private JRadioButton marriedYes;
    private JRadioButton marriedNo;
    private JSpinner childrenSpinner;
    private JTextField emergencyContactField;
    private JButton saveButton;
    private JButton backButton;

    public UpdateFamilyDetailsForm(HRMService service, String username) {
        super("Update Family Details");
        this.service = service;
        this.username = username;
        setSize(520, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Update Family Details", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel hint = new JLabel("Please update your family information:", SwingConstants.CENTER);
        formPanel.add(hint, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Married:"), gbc);
        JPanel marriedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        marriedYes = new JRadioButton("Yes");
        marriedNo = new JRadioButton("No");
        ButtonGroup marriedGroup = new ButtonGroup();
        marriedGroup.add(marriedYes);
        marriedGroup.add(marriedNo);
        marriedPanel.add(marriedYes);
        marriedPanel.add(marriedNo);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(marriedPanel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Number of Children:"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        childrenSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        Dimension spinnerSize = childrenSpinner.getPreferredSize();
        spinnerSize.width = 60; // tighter width
        childrenSpinner.setPreferredSize(spinnerSize);
        childrenSpinner.setMinimumSize(spinnerSize);
        JPanel childrenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        childrenPanel.add(childrenSpinner);
        gbc.gridx = 1;
        formPanel.add(childrenPanel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Emergency Contact:"), gbc);
        emergencyContactField = new JTextField(25);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        formPanel.add(emergencyContactField, gbc);

        gbc.anchor = GridBagConstraints.EAST;

        add(formPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        backButton = new JButton("Back to Dashboard");
        saveButton = new JButton("Save Family Details");
        footer.add(backButton);
        footer.add(saveButton);
        add(footer, BorderLayout.SOUTH);

        loadCurrentDetails();

        saveButton.addActionListener(e -> handleSave());
        backButton.addActionListener(e -> dispose());
    }

    private void loadCurrentDetails() {
        try {
            Employee emp = service.getEmployeeDetails(username);
            if (emp != null) {
                String details = emp.getFamilyDetails();
                boolean married = false;
                int children = 0;
                String contact = "";

                if (details != null && !details.trim().isEmpty()) {
                    String lower = details.toLowerCase();
                    if (lower.contains("married: yes")) {
                        married = true;
                    }
                    if (lower.contains("married: no")) {
                        married = false;
                    }
                    for (String line : details.split("\\n")) {
                        if (line.toLowerCase().startsWith("children:")) {
                            try {
                                children = Integer.parseInt(line.replaceAll("[^0-9]", "").trim());
                            } catch (NumberFormatException ignored) { }
                        }
                        if (line.toLowerCase().startsWith("emergency contact:")) {
                            contact = line.substring(line.indexOf(':') + 1).trim();
                        }
                    }
                }

                if (married) {
                    marriedYes.setSelected(true);
                } else {
                    marriedNo.setSelected(true);
                }
                childrenSpinner.setValue(children);
                emergencyContactField.setText(contact);
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load family details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSave() {
        boolean married = marriedYes.isSelected();
        int children = (Integer) childrenSpinner.getValue();
        String contact = emergencyContactField.getText().trim();

        if (contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an emergency contact.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Married: ").append(married ? "Yes" : "No").append('\n');
        sb.append("Children: ").append(children).append('\n');
        sb.append("Emergency Contact: ").append(contact);

        try {
            service.updateFamilyDetails(username, sb.toString());
            JOptionPane.showMessageDialog(this, "Family details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Failed to update family details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
