package CRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditExpense {
    public static void EditExpense(DefaultTableModel model, Connection conn, JFrame frame, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) { // Check if a row is selected
            // Danh sách category
            String[] categories = { "Category", "Food", "Rent", "Shopping", "Medical", "Meeting", "Sports", "Services",
                    "Other" };
            JComboBox<String> categoryComboBox = new JComboBox<>(categories);
            categoryComboBox.setSelectedItem(model.getValueAt(selectedRow, 0).toString());

            JTextField descriptionField = new JTextField(model.getValueAt(selectedRow, 1).toString());
            JTextField dateField = new JTextField(model.getValueAt(selectedRow, 2).toString());
            JTextField amountField = new JTextField(model.getValueAt(selectedRow, 3).toString());

            Object[] message = {
                    "Category:", categoryComboBox, // ComboBox thay vì JTextField
                    "Description:", descriptionField,
                    "Date (yyyy-MM-dd):", dateField,
                    "Amount:", amountField
            };

            int option = JOptionPane.showConfirmDialog(
                    frame, message, "Edit Expense", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newCategory = (String) categoryComboBox.getSelectedItem();

                // Kiểm tra category
                while (newCategory.equals("Category")) {
                    JOptionPane.showMessageDialog(frame, "Please select a valid category!", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    categoryComboBox.requestFocusInWindow();
                    int retry = JOptionPane.showConfirmDialog(frame, message, "Edit Expense",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (retry != JOptionPane.OK_OPTION)
                        return;
                    newCategory = (String) categoryComboBox.getSelectedItem();
                }

                String newDescription = descriptionField.getText().trim();
                String newDate;
                double newAmount;

                // Vòng lặp để kiểm tra và yêu cầu nhập lại date
                while (true) {
                    try {
                        newDate = dateField.getText().trim();
                        java.sql.Date.valueOf(newDate); // Kiểm tra format yyyy-MM-dd
                        break; // Thoát nếu date hợp lệ
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid date format! Use yyyy-MM-dd.", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                        dateField.requestFocusInWindow();
                        int retry = JOptionPane.showConfirmDialog(frame, message, "Edit Expense",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (retry != JOptionPane.OK_OPTION)
                            return;
                    }
                }

                // Vòng lặp kiểm tra amount
                while (true) {
                    try {
                        newAmount = Double.parseDouble(amountField.getText().trim());
                        break; // Thoát vòng lặp nếu amount hợp lệ
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid amount format! Please enter a valid number.",
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                        amountField.requestFocusInWindow();
                        int retry = JOptionPane.showConfirmDialog(frame, message, "Edit Expense",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (retry != JOptionPane.OK_OPTION)
                            return;
                    }
                }

                String oldCategory = model.getValueAt(selectedRow, 0).toString();
                String oldDescription = model.getValueAt(selectedRow, 1).toString();
                String oldDate = model.getValueAt(selectedRow, 2).toString();
                double oldAmount = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());

                // Update table model
                model.setValueAt(newCategory, selectedRow, 0);
                model.setValueAt(newDescription, selectedRow, 1);
                model.setValueAt(newDate, selectedRow, 2);
                model.setValueAt(newAmount, selectedRow, 3);

                try {
                    // Update database
                    String updateSQL = "UPDATE expenses SET category = ?, description = ?, date = ?, amount = ? " +
                            "WHERE category = ? AND description = ? AND date = ? AND amount = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                        // Giá trị mới
                        pstmt.setString(1, newCategory);
                        pstmt.setString(2, newDescription);
                        pstmt.setDate(3, java.sql.Date.valueOf(newDate));
                        pstmt.setDouble(4, newAmount);
                        // Giá trị cũ
                        pstmt.setString(5, oldCategory);
                        pstmt.setString(6, oldDescription);
                        pstmt.setDate(7, java.sql.Date.valueOf(oldDate));
                        pstmt.setDouble(8, oldAmount);
                        pstmt.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(frame, "Expense updated successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }

        } else {
            JOptionPane.showMessageDialog(frame, "Please select an expense to edit!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
