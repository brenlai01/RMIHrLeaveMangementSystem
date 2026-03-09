package client;

import remote.HRMService;
import javax.swing.*;
import java.awt.*;

public class ViewLeaveHistoryForm extends JFrame {

    private HRMService service;
    private String username;
    private JTable leaveTable;
    private JScrollPane scrollPane;

    public ViewLeaveHistoryForm(HRMService service, String username) {
        super("Leave History");
        this.service = service;
        this.username = username;
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        String[] columns = {"Leave ID", "Start Date", "End Date", "Reason", "Status"};
        leaveTable = new JTable(new Object[0][5], columns);

        // TODO: call service.getLeaveHistory(username) and populate table rows

        scrollPane = new JScrollPane(leaveTable);
        add(scrollPane, BorderLayout.CENTER);
    }
}
