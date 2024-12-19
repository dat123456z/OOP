package CRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditExpense {
    public static void EditExpense(DefaultTableModel model, Connection conn, JFrame frame, JTable table,
            JTextField propertyLabel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an expense to edit!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String oldCategory = model.getValueAt(selectedRow, 0).toString();
        String oldDescription = model.getValueAt(selectedRow, 1).toString();
        String oldDate = model.getValueAt(selectedRow, 2).toString();
        double oldAmount = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());

        JTextField categoryField = new JTextField(oldCategory);
        JTextField descriptionField = new JTextField(oldDescription);
        JTextField dateField = new JTextField(oldDate);
        JTextField amountField = new JTextField(String.valueOf(oldAmount));

        Object[] message = { "Category:", categoryField, "Description:", descriptionField, "Date (yyyy-MM-dd):",
                dateField,
                "Amount:", amountField };

        int option = JOptionPane.showConfirmDialog(frame, message, "Edit Expense", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String newCategory = categoryField.getText();
            String newDescription = descriptionField.getText();
            String newDate = dateField.getText();
            double newAmount;

            try {
                newAmount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Cập nhật dữ liệu trong bảng JTable
                model.setValueAt(newCategory, selectedRow, 0);
                model.setValueAt(newDescription, selectedRow, 1);
                model.setValueAt(newDate, selectedRow, 2);
                model.setValueAt(newAmount, selectedRow, 3);

                // Cập nhật dữ liệu trong cơ sở dữ liệu
                String updateSQL = "UPDATE expenses SET category = ?, description = ?, date = ?, amount = ? " +
                        "WHERE category = ? AND description = ? AND date = ? AND amount = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    pstmt.setString(1, newCategory);
                    pstmt.setString(2, newDescription);
                    pstmt.setDate(3, java.sql.Date.valueOf(newDate));
                    pstmt.setDouble(4, newAmount);
                    pstmt.setString(5, oldCategory);
                    pstmt.setString(6, oldDescription);
                    pstmt.setDate(7, java.sql.Date.valueOf(oldDate));
                    pstmt.setDouble(8, oldAmount);
                    pstmt.executeUpdate();
                }

                // Cập nhật tài sản người dùng
                double amountDifference = newAmount - oldAmount;
                String updatePropertySQL = "UPDATE personal SET property = property + ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updatePropertySQL)) {
                    pstmt.setDouble(1, amountDifference);
                    pstmt.executeUpdate();
                }

                // Cập nhật giao diện
                propertyLabel.setText(String.format("Property: %.2f", getCurrentProperty(conn)));

                JOptionPane.showMessageDialog(frame, "Expense updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private static double getCurrentProperty(Connection conn) throws Exception {
        String query = "SELECT property FROM personal";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
                var rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("property");
            }
        }
        throw new Exception("Failed to fetch current property value!");
    }
}
