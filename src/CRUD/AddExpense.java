package CRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddExpense {
    public static void AddExpense(DefaultTableModel model, Connection conn, JFrame frame) {
        // Danh sách category
        String[] categories = {"Category", "Food", "Rent", "Shopping", "Medical", "Meeting", "Sports", "Services", "Other"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories); // ComboBox để chọn Category
        categoryComboBox.setSelectedIndex(0); // Hiển thị "Category" khi mở lên

        JTextField descriptionField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField amountField = new JTextField();

        Object[] message = {
            "Category:", categoryComboBox, // ComboBox
            "Description:", descriptionField,
            "Date (yyyy-MM-dd):", dateField,
            "Amount:", amountField
        };

        int option = JOptionPane.showConfirmDialog(
                frame, message, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String category = (String) categoryComboBox.getSelectedItem();
                
                    // Kiểm tra category
                    while (category.equals("Category")) {
                        JOptionPane.showMessageDialog(frame, "Please select a valid category!", "Input Error", JOptionPane.ERROR_MESSAGE);
                        categoryComboBox.requestFocusInWindow();
                        int retry = JOptionPane.showConfirmDialog(frame, message, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);
                        if (retry != JOptionPane.OK_OPTION) return;
                        category = (String) categoryComboBox.getSelectedItem();
                    }
                
                    String description = descriptionField.getText().trim();
                    String date;
                    double amount;
                
                    // Vòng lặp để yêu cầu nhập lại date đúng định dạng
                    while (true) {
                        try {
                            date = dateField.getText().trim();
                            java.sql.Date.valueOf(date); // Kiểm tra format yyyy-MM-dd
                            break; // Thoát vòng lặp nếu date hợp lệ
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid date format! Use yyyy-MM-dd.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            dateField.requestFocusInWindow(); // Đưa con trỏ về ô date
                            int retry = JOptionPane.showConfirmDialog(frame, message, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);
                            if (retry != JOptionPane.OK_OPTION) return; // Hủy bỏ nếu chọn Cancel
                        }
                    }
                
                    // Kiểm tra và nhập amount
                    while (true) {
                        try {
                            amount = Double.parseDouble(amountField.getText().trim());
                            break;
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid amount format! Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            amountField.requestFocusInWindow();
                            int retry = JOptionPane.showConfirmDialog(frame, message, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);
                            if (retry != JOptionPane.OK_OPTION) return;
                        }
                    }
                
                    try {
                        // Thêm dữ liệu vào JTable
                        model.addRow(new Object[]{category, description, date, amount});
                
                        // Thêm dữ liệu vào SQL
                        String insertSQL = "INSERT INTO expenses (category, description, date, amount) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                            pstmt.setString(1, category);
                            pstmt.setString(2, description);
                            pstmt.setDate(3, java.sql.Date.valueOf(date));
                            pstmt.setDouble(4, amount);
                            pstmt.executeUpdate();
                        }
                        JOptionPane.showMessageDialog(frame, "Expense added successfully!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
                
    }
}
