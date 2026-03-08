package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;

public class ApplyLeaveForm extends JFrame {

    private HRMService service;
    private String username;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextArea reasonArea;
    private JButton submitButton;

    public ApplyLeaveForm(HRMService service, String username) {
        super("Apply for Leave");
        this.service = service;
        this.username = username;
        setSize(400, 350);
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
        panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);

        startDateField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(startDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);

        endDateField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(endDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Reason:"), gbc);

        reasonArea = new JTextArea(4, 20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(new JScrollPane(reasonArea), gbc);

        submitButton = new JButton("Submit");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        submitButton.addActionListener(e -> handleSubmit());

        add(panel);
    }

    private void handleSubmit() {
        // TODO: validate date format (YYYY-MM-DD)
        // TODO: create LeaveApplication object
        // TODO: call service.applyLeave(application)
        // TODO: show success or error message
    }
}
