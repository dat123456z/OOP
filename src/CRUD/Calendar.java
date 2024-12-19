package CRUD;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class Calendar {

    public static void showExpenseCalendar(JFrame frame, Connection conn) {
        // Tạo hộp thoại hiển thị Calendar
        JDialog calendarDialog = new JDialog(frame, "Monthly Expense Calendar", true);
        calendarDialog.setSize(900, 700);
        calendarDialog.setLayout(new BorderLayout());

        // Phần tiêu đề
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("MONTHLY EXPENSE", JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(new Color(41, 128, 185));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel chọn năm và tháng
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel yearLabel = new JLabel("Year:");
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        JLabel monthLabel = new JLabel("Month:");
        JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });

        // Thêm các năm từ 2020 đến năm hiện tại
        int currentYear = Year.now().getValue();
        for (int year = 2020; year <= currentYear; year++) {
            yearComboBox.addItem(year);
        }

        filterPanel.add(yearLabel);
        filterPanel.add(yearComboBox);
        filterPanel.add(monthLabel);
        filterPanel.add(monthComboBox);

        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        calendarDialog.add(headerPanel, BorderLayout.NORTH);

        // Panel hiển thị calendar
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(6, 7, 10, 10));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        calendarDialog.add(calendarPanel, BorderLayout.CENTER);

        // Thêm ItemListener để cập nhật calendar khi thay đổi năm hoặc tháng
        yearComboBox.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                int selectedYear = (int) yearComboBox.getSelectedItem();
                int selectedMonth = monthComboBox.getSelectedIndex() + 1;
                updateCalendar(calendarPanel, conn, selectedYear, selectedMonth, frame);
            }
        });

        monthComboBox.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                int selectedYear = (int) yearComboBox.getSelectedItem();
                int selectedMonth = monthComboBox.getSelectedIndex() + 1;
                updateCalendar(calendarPanel, conn, selectedYear, selectedMonth, frame);
            }
        });

        // Hiển thị giao diện lần đầu với tháng hiện tại
        yearComboBox.setSelectedItem(currentYear);
        monthComboBox.setSelectedIndex(java.time.LocalDate.now().getMonthValue() - 1);
        updateCalendar(calendarPanel, conn, currentYear, java.time.LocalDate.now().getMonthValue(), frame);

        calendarDialog.setLocationRelativeTo(frame);
        calendarDialog.setVisible(true);
    }

    private static void updateCalendar(JPanel calendarPanel, Connection conn, int year, int month, JFrame frame) {
        calendarPanel.removeAll(); // Xóa nội dung cũ

        // Lấy dữ liệu chi tiêu từ cơ sở dữ liệu
        List<String[]> expenseData = new ArrayList<>();
        try {
            String query = "SELECT date, SUM(amount) AS total FROM expenses " +
                    "WHERE YEAR(date) = ? AND MONTH(date) = ? GROUP BY date ORDER BY date ASC";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, year);
                pstmt.setInt(2, month);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        expenseData.add(new String[]{rs.getDate("date").toString(), String.valueOf(rs.getDouble("total"))});
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        // Hiển thị dữ liệu trong calendar
        for (int day = 1; day <= 31; day++) {
            String expense = "";
            for (String[] data : expenseData) {
                if (data[0].endsWith("-" + (day < 10 ? "0" : "") + day)) {
                    expense = data[1];
                    break;
                }
            }

            JButton dayButton = new JButton("<html><center>" + day + "<br>" + expense + "</center></html>");
            dayButton.setFont(new Font("Roboto", Font.PLAIN, 15));
            dayButton.setBackground(expense.isEmpty() ? Color.LIGHT_GRAY : new Color(52, 152, 219));
            dayButton.setForeground(expense.isEmpty() ? Color.BLACK : Color.WHITE);
            dayButton.setFocusPainted(false);

            final int finalDay = day;
            final String finalExpense = expense;
            dayButton.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                    "Details for day " + finalDay + ": " + (finalExpense.isEmpty() ? "No expenses" : finalExpense)));

            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}
