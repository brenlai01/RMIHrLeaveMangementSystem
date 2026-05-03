package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.rmi.RemoteException;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ApplyLeaveForm extends JFrame {

    private HRMService service;
    private String username;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JTextArea reasonArea;
    private JButton submitButton;
    private JButton backButton;

    public ApplyLeaveForm(HRMService service, String username) {
        super("Apply for Leave");
        this.service = service;
        this.username = username;
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        // ADD PADDING HERE: top, left, bottom, right
        panel.setBorder(new EmptyBorder(60, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Increased vertical spacing between rows
        gbc.anchor = GridBagConstraints.WEST;

        // Start Date - Fixed: removing "JDateChooser" prefix to use the class field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Start Date:"), gbc);

        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        startDateChooser.setMinSelectableDate(new Date());
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(startDateChooser, gbc);

        // End Date - Fixed: removing "JDateChooser" prefix
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("End Date:"), gbc);

        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setMinSelectableDate(new Date());
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(endDateChooser, gbc);

        // Reason
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Reason:"), gbc);

        reasonArea = new JTextArea(4, 20);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(reasonArea), gbc);

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmit());
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;       // Span across label and input
        gbc.weighty = 0;         // Reset weight so buttons don't stretch vertically
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5); // 15px top gap to separate from Reason
        panel.add(submitButton, gbc);

        // Back Button
        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            dispose();
            new EmployeeDashboard(service, username).setVisible(true);
        });
        gbc.gridx = 0; gbc.gridy = 4; // Use row 4 (immediately after Submit)
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5); // Smaller 5px gap between the two buttons
        panel.add(backButton, gbc);

        add(panel);
    }

    private void handleSubmit() {

        Date startRaw = startDateChooser.getDate();
        Date endRaw = endDateChooser.getDate();
        String reason = reasonArea.getText().trim();

        if (startRaw == null || endRaw == null || reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        if (endRaw.before(startRaw)) {
            JOptionPane.showMessageDialog(this, "End date cannot be before start date!");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(startRaw);
        String endDate = sdf.format(endRaw);

        submitButton.setEnabled(false);

        // Shared variables to communicate between threads
        final long[] daysRequested = new long[1];
        final int[] balance = new int[1];

        // Thread 1: calculate days and check leave balance
        Thread checkThread = new Thread(() -> {
            // System.out.println("Check thread running: " + Thread.currentThread().getName());
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            daysRequested[0] = ChronoUnit.DAYS.between(start, end) + 1;

            if (daysRequested[0] <= 0) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Invalid date range!");
                    submitButton.setEnabled(true);
                });
                return;
            }

            try {
                balance[0] = service.checkLeaveBalance(username);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        // Thread 2: apply leave after checkThread finishes
        Thread applyThread = new Thread(() -> {
            try {
                checkThread.join(); // wait for balance check

                if (daysRequested[0] > balance[0]) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Not enough leave balance! You have " + balance[0] + " days left.");
                        submitButton.setEnabled(true);
                    });
                    return;
                }

                // Apply leave
                // System.out.println("Apply thread running: " + Thread.currentThread().getName());
                service.applyLeave(username, startDate, endDate, reason);

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Leave application submitted successfully and is pending approval");
                    submitButton.setEnabled(true);
                    dispose();
                    new EmployeeDashboard(service, username).setVisible(true);
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                    submitButton.setEnabled(true);
                });
            }
        });

        // Start both threads
        checkThread.start();
        applyThread.start();
    }
}
