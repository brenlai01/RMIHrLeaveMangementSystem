package client;

import model.Employee;
import remote.HRMService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class ViewPersonalDetailsForm extends JFrame {

    private final HRMService service;
    private String username;
    private final EmployeeDashboard dashboard;
    private JTextField employeeIdField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField icPassportField;
    private JTextField familyDetailsField;
    private JTextField leaveBalanceField;
    private JButton backButton;

    public ViewPersonalDetailsForm(HRMService service, String username) {
        this(service, username, null);
    }

    public ViewPersonalDetailsForm(HRMService service, String username, EmployeeDashboard dashboard) {
        super("Personal Details");
        this.service = service;
        this.username = username;
        this.dashboard = dashboard;
        setSize(500, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Your Personal Details", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 16f));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        employeeIdField = createReadOnlyField();
        usernameField = createReadOnlyField();
        passwordField = new JPasswordField(20);
        passwordField.setEditable(false);
        firstNameField = createReadOnlyField();
        lastNameField = createReadOnlyField();
        icPassportField = createReadOnlyField();
        familyDetailsField = createReadOnlyField();
        leaveBalanceField = createReadOnlyField();

        addRow(formPanel, gbc, 0, "Employee ID:", employeeIdField);
        addRow(formPanel, gbc, 1, "Username:", usernameField);
        addRow(formPanel, gbc, 2, "Password:", passwordField);
        addRow(formPanel, gbc, 3, "First Name:", firstNameField);
        addRow(formPanel, gbc, 4, "Last Name:", lastNameField);
        addRow(formPanel, gbc, 5, "IC/Passport:", icPassportField);
        addRow(formPanel, gbc, 6, "Family Details:", familyDetailsField);
        addRow(formPanel, gbc, 7, "Leave Balance:", leaveBalanceField);

        add(formPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back to Dashboard");
        footer.add(backButton);
        add(footer, BorderLayout.SOUTH);

        backButton.addActionListener(e -> returnToDashboard());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                showDashboard();
            }
        });

        loadDetails();
    }

    public void updateUsername(String newUsername) {
        this.username = newUsername;
        loadDetails();
    }

    private void loadDetails() {
        try {
            Employee emp = service.getEmployeeDetails(username);
            if (emp != null) {
                employeeIdField.setText(String.valueOf(emp.getEmployeeId()));
                usernameField.setText(emp.getUsername());
                passwordField.setText(emp.getPassword());
                firstNameField.setText(emp.getFirstName());
                lastNameField.setText(emp.getLastName());
                icPassportField.setText(emp.getIcPassport());
                familyDetailsField.setText(emp.getFamilyDetails());
                leaveBalanceField.setText(String.valueOf(emp.getLeaveBalance()));
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load personal details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createReadOnlyField() {
        JTextField field = new JTextField(20);
        field.setEditable(false);
        return field;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void returnToDashboard() {
        dispose();
        showDashboard();
    }

    private void showDashboard() {
        if (dashboard != null) {
            dashboard.setVisible(true);
        }
    }
}

