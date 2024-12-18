package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {
    private Connection conn;

    public DashboardPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());
        
        // Thêm Dashboard Panel vào giao diện
        add(createDashboardPanel(), BorderLayout.CENTER);
    }

    // Tạo Panel chứa ComboBox chọn tháng, năm và nút Load Data
    private JPanel createDashboardPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JLabel monthLabel = new JLabel("Select Month:");
        JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });

        JLabel yearLabel = new JLabel("Select Year:");
        JComboBox<String> yearComboBox = new JComboBox<>();
        for (int year = 2020; year <= 2030; year++) {
            yearComboBox.addItem(String.valueOf(year));
        }

        JButton loadButton = new JButton("Load Data");

        controlPanel.add(monthLabel);
        controlPanel.add(monthComboBox);
        controlPanel.add(yearLabel);
        controlPanel.add(yearComboBox);
        controlPanel.add(loadButton);

        // Bảng để hiển thị dữ liệu thống kê
        String[] columnNames = {"Summary", "Value"};
        DefaultTableModel tableModel = new DefaultTableModel(new Object[4][2], columnNames);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(table);

        // Sự kiện khi nhấn nút Load Data
        loadButton.addActionListener(e -> {
            int selectedMonth = monthComboBox.getSelectedIndex() + 1; // Tháng tính từ 0
            int selectedYear = Integer.parseInt(yearComboBox.getSelectedItem().toString());
            loadData(tableModel, selectedMonth, selectedYear, monthComboBox);
        });

        // Tạo panel chứa bảng và điều khiển
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.add(controlPanel, BorderLayout.NORTH);
        dashboardPanel.add(tableScrollPane, BorderLayout.CENTER);

        return dashboardPanel;
    }

    // Hàm riêng để tải dữ liệu
    private void loadData(DefaultTableModel tableModel, int month, int year, JComboBox<String> monthComboBox) {
        try {
            // Truy vấn dữ liệu cho tháng được chọn
            String query = "SELECT SUM(amount) AS totalSpent, COUNT(*) AS transactions " +
                    "FROM expenses WHERE MONTH(date) = ? AND YEAR(date) = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tableModel.setValueAt("Total Spent in " + monthComboBox.getSelectedItem(), 0, 0);
                tableModel.setValueAt(rs.getDouble("totalSpent") + " VND", 0, 1);

                tableModel.setValueAt("Transactions in " + monthComboBox.getSelectedItem(), 1, 0);
                tableModel.setValueAt(rs.getInt("transactions"), 1, 1);
            }

            // Truy vấn dữ liệu cho năm được chọn
            String queryYear = "SELECT SUM(amount) AS totalSpent, COUNT(*) AS transactions " +
                    "FROM expenses WHERE YEAR(date) = ?";
            PreparedStatement psYear = conn.prepareStatement(queryYear);
            psYear.setInt(1, year);
            ResultSet rsYear = psYear.executeQuery();

            if (rsYear.next()) {
                tableModel.setValueAt("Total Spent This Year", 2, 0);
                tableModel.setValueAt(rsYear.getDouble("totalSpent") + " VND", 2, 1);

                tableModel.setValueAt("Transactions This Year", 3, 0);
                tableModel.setValueAt(rsYear.getInt("transactions"), 3, 1);
            }

            // Làm mới bảng
            tableModel.fireTableDataChanged();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
