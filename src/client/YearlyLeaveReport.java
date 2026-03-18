package client;

import model.Employee;
import model.LeaveApplication;
import remote.HRMService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class YearlyLeaveReport extends JFrame {

    private HRMService service;
    private String employeeName;
    private int employeeId = -1;

    private JTable leaveTable;
    private DefaultTableModel tableModel;
    private JLabel totalDaysLabel, approvedLabel, rejectedLabel, pendingLabel;

    public YearlyLeaveReport(HRMService service, String employeeName) throws RemoteException {
        super("Yearly Leave Report for " + employeeName);
        this.service = service;
        this.employeeName = employeeName;

        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
    }

    /**
     * Build the UI layout only
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // 10px gap around components
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding around edges

        // Title
        JLabel titleLabel = new JLabel("Yearly Leave Report - " + employeeName);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Start Date", "End Date", "Days", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        leaveTable = new JTable(tableModel);
        leaveTable.setFillsViewportHeight(true);
        leaveTable.getTableHeader().setReorderingAllowed(false);
        leaveTable.getTableHeader().setResizingAllowed(false);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tablePanel.add(new JScrollPane(leaveTable), BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Summary
        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 10, 5)); // hgap=10, vgap=5
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));

        totalDaysLabel = new JLabel("Total Leave Days: 0");
        approvedLabel = new JLabel("Approved: 0");
        rejectedLabel = new JLabel("Rejected: 0");
        pendingLabel = new JLabel("Pending: 0");

        // Align text to left for consistency
        totalDaysLabel.setHorizontalAlignment(SwingConstants.LEFT);
        approvedLabel.setHorizontalAlignment(SwingConstants.LEFT);
        rejectedLabel.setHorizontalAlignment(SwingConstants.LEFT);
        pendingLabel.setHorizontalAlignment(SwingConstants.LEFT);

        summaryPanel.add(totalDaysLabel);
        summaryPanel.add(approvedLabel);
        summaryPanel.add(rejectedLabel);
        summaryPanel.add(pendingLabel);

        add(summaryPanel, BorderLayout.SOUTH);
    }

    /**
     * Load data from service and populate the table & summary
     */
    private void loadData() throws RemoteException {
        int year = java.time.Year.now().getValue();

        Employee emp = service.getEmployeeDetails(employeeName);
        if (emp != null) {
            employeeId = emp.getEmployeeId();
        }

        List<LeaveApplication> reportList = service.generateYearlyReport(employeeId, year);

        int totalDays = 0;
        int approved = 0, rejected = 0, pending = 0;

        for (LeaveApplication leave : reportList) {
            int days = calculateDays(leave.getStartDate(), leave.getEndDate());

            tableModel.addRow(new Object[]{
                    leave.getStartDate(),
                    leave.getEndDate(),
                    days,
                    leave.getStatus()
            });

            totalDays += days;

            switch (leave.getStatus().toLowerCase()) {
                case "approved": approved++; break;
                case "rejected": rejected++; break;
                case "pending": pending++; break;
            }
        }

        // Update summary labels
        totalDaysLabel.setText("Total Leave Days: " + totalDays);
        approvedLabel.setText("Approved: " + approved);
        rejectedLabel.setText("Rejected: " + rejected);
        pendingLabel.setText("Pending: " + pending);
    }

    // Simple days calculation (basic version)
    private int calculateDays(String start, String end) {
        try {
            java.time.LocalDate s = java.time.LocalDate.parse(start);
            java.time.LocalDate e = java.time.LocalDate.parse(end);
            return (int) java.time.temporal.ChronoUnit.DAYS.between(s, e) + 1;
        } catch (Exception e) {
            return 0;
        }
    }
}