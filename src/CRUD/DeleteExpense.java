package CRUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteExpense {
    public static void DeleteExpense(DefaultTableModel model, Connection conn, JFrame frame, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) { // Check if a row is selected
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this expense?", "Delete Expense", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String category = model.getValueAt(selectedRow, 0).toString();
                    String description = model.getValueAt(selectedRow, 1).toString();
                    String date = model.getValueAt(selectedRow, 2).toString();
                    String amount = model.getValueAt(selectedRow, 3).toString();

                    // Remove from database
                    String deleteSQL = "DELETE FROM expenses WHERE category = ? AND description = ? AND date = ? AND amount = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                        pstmt.setString(1, category);
                        pstmt.setString(2, description);
                        pstmt.setDate(3, java.sql.Date.valueOf(date));
                        pstmt.setDouble(4, Double.parseDouble(amount));
                        pstmt.executeUpdate();
                    }

                    // Remove from table model
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Expense deleted successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an expense to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
