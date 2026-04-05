package client;

import remote.HRMService;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ViewLeaveHistoryForm extends JFrame {

    private HRMService service;
    private String username;
    private JTable leaveTable;
    private JScrollPane scrollPane;
    private JButton backButton;

    public ViewLeaveHistoryForm(HRMService service, String username) {
        super("Leave History");
        this.service = service;
        this.username = username;
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        loadLeaveHistory();
    }

    private void initComponents(Object[][] tableData, boolean isEmpty) {
        // Page title
        JLabel pageTitle = new JLabel("Leave History", SwingConstants.CENTER);
        pageTitle.setFont(new Font("Arial", Font.BOLD, 22));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        // Section title
        JLabel sectionTitle = new JLabel("Your Leave Records");
        sectionTitle.setFont(new Font("Arial", Font.PLAIN, 16));
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // Table setup
        String[] columns = {"Leave ID", "Start Date", "End Date", "Reason", "Status", "Apply Date"};

        leaveTable = new JTable(tableData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // non-editable
            }
        };

        leaveTable.getTableHeader().setReorderingAllowed(false);
        leaveTable.getTableHeader().setResizingAllowed(false);

        // Custom renderer for "merged row"
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isEmpty) {
                    if (column == 0) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                        setFont(getFont().deriveFont(Font.ITALIC));
                    } else {
                        setText("");
                    }
                } else {
                    setHorizontalAlignment(SwingConstants.CENTER);
                }
                return this;
            }
        };

        for (int i = 0; i < leaveTable.getColumnCount(); i++) {
            leaveTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        if (isEmpty) leaveTable.setShowGrid(false);

        leaveTable.setRowHeight(25);
        leaveTable.setFillsViewportHeight(true);
        leaveTable.setIntercellSpacing(new Dimension(10, 5));

        scrollPane = new JScrollPane(leaveTable);
        scrollPane.setPreferredSize(new Dimension(650, 250));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Back button
        backButton = new JButton("Go Back to Dashboard");
        backButton.setPreferredSize(new Dimension(220, 40));
        backButton.addActionListener(e -> {
            dispose();
            new EmployeeDashboard(service, username).setVisible(true);
        });

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(pageTitle, BorderLayout.CENTER);
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.add(sectionTitle, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(sectionPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(backButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private Object[][] loadLeaveHistory() {
        Object[][] data;
        boolean isEmpty = false;

        try {
            var history = service.getLeaveHistory(username);
            if (history == null || history.isEmpty()) {
                isEmpty = true;
                data = new Object[][] {{"No record history found", "", "", "", "", ""}};
            } else {
                data = new Object[history.size()][6];
                for (int i = 0; i < history.size(); i++) {
                    var app = history.get(i);
                    data[i][0] = app.getLeaveId();
                    data[i][1] = app.getStartDate();
                    data[i][2] = app.getEndDate();
                    data[i][3] = app.getReason();
                    data[i][4] = app.getStatus();
                    data[i][5] = app.getAppliedAt();
                }
            }
        } catch (Exception e) {
            isEmpty = true;
            data = new Object[][] {{"Error loading history", "", "", "", "", ""}};
        }

        // Call UI init with the processed data
        initComponents(data, isEmpty);

        return data;
    }
}