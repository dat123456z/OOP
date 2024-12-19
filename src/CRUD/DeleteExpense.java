package CRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteExpense {
    public static void DeleteExpense(DefaultTableModel model, Connection conn, JFrame frame, JTable table,
            JTextField propertyLabel) {
        int[] selectedRows = table.getSelectedRows(); // Lấy tất cả các dòng được chọn
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(frame, "Please select one or more expenses to delete!", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the selected expenses?",
                "Delete Expenses", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                double totalAmount = 0; // Tổng số tiền bị xóa
                String deleteSQL = "DELETE FROM expenses WHERE category = ? AND description = ? AND date = ? AND amount = ?";

                for (int row : selectedRows) {
                    String category = model.getValueAt(row, 0).toString();
                    String description = model.getValueAt(row, 1).toString();
                    String date = model.getValueAt(row, 2).toString();
                    double amount = Double.parseDouble(model.getValueAt(row, 3).toString());

                    try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                        pstmt.setString(1, category);
                        pstmt.setString(2, description);
                        pstmt.setDate(3, java.sql.Date.valueOf(date));
                        pstmt.setDouble(4, amount);
                        pstmt.executeUpdate();
                    }

                    totalAmount += amount;
                }

                // Cập nhật tài sản người dùng
                String updatePropertySQL = "UPDATE personal SET property = property - ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updatePropertySQL)) {
                    pstmt.setDouble(1, totalAmount);
                    pstmt.executeUpdate();
                }

                // Xóa các dòng khỏi JTable (theo thứ tự giảm dần để tránh lỗi chỉ số)
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    model.removeRow(selectedRows[i]);
                }

                // Cập nhật giao diện
                propertyLabel.setText(String.format("Property: %.2f", getCurrentProperty(conn)));

                JOptionPane.showMessageDialog(frame, "Selected expenses deleted successfully!");
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
