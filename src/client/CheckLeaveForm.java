package client;

import model.LeaveApplication;
import remote.HRMService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * HR view for pending leave applications with Approve/Reject actions.
 */
public class CheckLeaveForm extends JFrame {

	private final HRMService service;
	private final String username;
	private JTable leaveTable;
	private DefaultTableModel tableModel;

	public CheckLeaveForm(HRMService service, String username) {
		super("Pending Leave Applications");
		this.service = service;
		this.username = username;
		setSize(800, 500);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initComponents();
		loadPendingLeaves();
	}

	private void initComponents() {
		// Title
		JLabel titleLabel = new JLabel("Pending Leave Applications", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

		// Table model
		String[] columns = {"Leave ID", "Employee ID", "Start Date", "End Date", "Reason", "Status", "Applied At"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		leaveTable = new JTable(tableModel);
		leaveTable.getTableHeader().setReorderingAllowed(false);
		leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leaveTable.setRowHeight(24);

		JScrollPane scrollPane = new JScrollPane(leaveTable);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		// Action buttons
		JButton approveButton = new JButton("Approve");
		approveButton.addActionListener(e -> updateStatus("APPROVED"));

		JButton rejectButton = new JButton("Reject");
		rejectButton.addActionListener(e -> updateStatus("REJECTED"));

		JButton backButton = new JButton("Back to Dashboard");
		backButton.addActionListener(e -> {
			dispose();
			new HRDashboard(service, username).setVisible(true);
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
		buttonPanel.add(approveButton);
		buttonPanel.add(rejectButton);
		buttonPanel.add(backButton);

		setLayout(new BorderLayout());
		add(titleLabel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void loadPendingLeaves() {
		// Clear existing rows
		tableModel.setRowCount(0);

		try {
			List<LeaveApplication> pending = service.getPendingLeaveApplications();

			if (pending == null || pending.isEmpty()) {
				tableModel.addRow(new Object[]{"No pending applications", "", "", "", "", "", ""});
				leaveTable.setEnabled(false);
				return;
			}

			leaveTable.setEnabled(true);
			for (LeaveApplication app : pending) {
				tableModel.addRow(new Object[]{
						app.getLeaveId(),
						app.getEmployeeId(),
						app.getStartDate(),
						app.getEndDate(),
						app.getReason(),
						app.getStatus(),
						app.getAppliedAt()
				});
			}
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(this, "Error loading pending leaves: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateStatus(String status) {
		int selectedRow = leaveTable.getSelectedRow();

		if (!leaveTable.isEnabled() || selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "Please select a leave record first.");
			return;
		}

		Object leaveIdValue = tableModel.getValueAt(selectedRow, 0);
		if (!(leaveIdValue instanceof Integer)) {
			JOptionPane.showMessageDialog(this, "Invalid selection.");
			return;
		}

		int leaveId = (Integer) leaveIdValue;

		try {
			service.updateLeaveStatus(leaveId, status);
			JOptionPane.showMessageDialog(this, "Leave status updated to " + status + ".");
			loadPendingLeaves();
		} catch (RemoteException e) {
			Throwable cause = e;

			while (cause.getCause() != null) {
				cause = cause.getCause();
			}

			JOptionPane.showMessageDialog(this, cause.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
