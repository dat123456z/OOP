package view;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.ExpenseManagerModel;

public class ExpenseManagerView {

    public void createUI() throws SQLException {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        JLabel appTitle = new JLabel("Expense Tracker");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("Roboto", Font.BOLD, 28));
        headerPanel.add(appTitle, BorderLayout.WEST);

        // Tạo panel bên trái (menu)
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(44, 62, 80));
        sidePanel.setPreferredSize(new Dimension(220, 750));
        String[] menuItems = {"Dashboard", "Expenses", "Analytics", "Calendar", "Categories", "Settings"};
        String[] icons = {"\uD83D\uDCCA", "\uD83D\uDCB8", "\uD83D\uDCCA", "\uD83D\uDCC5", "\uD83D\uDDC3", "\u2699"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = new JButton(icons[i] + "  " + menuItems[i]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 50));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(52, 73, 94));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setFont(new Font("Roboto", Font.PLAIN, 16));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(52, 152, 219));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(52, 73, 94));
                }
            });
            sidePanel.add(Box.createVerticalStrut(10));
            sidePanel.add(btn);
        }

        // Table
        Connection conn = SQLConnection.getSQLServerConnection();
        List<ExpenseManagerModel> expenseList = DBUTills.getExpenses(conn);

        String[] columnNames = {"Category", "Description", "Date", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (ExpenseManagerModel expense : expenseList) {
            model.addRow(new Object[]{expense.getCategory(), expense.getDescription(), expense.getDate(), expense.getAmount()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        // Renderer cho Amount
        DefaultTableCellRenderer amountRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value != null) {
                    double amount = Double.parseDouble(value.toString());
                    setForeground(amount < 0 ? Color.RED : new Color(39, 174, 96));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                super.setValue(value);
            }
        };
        table.getColumnModel().getColumn(3).setCellRenderer(amountRenderer);

        // Căn giữa các cột còn lại
        DefaultTableCellRenderer alignCenterRenderer = new DefaultTableCellRenderer();
        alignCenterRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 3) table.getColumnModel().getColumn(i).setCellRenderer(alignCenterRenderer);
        }

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        // Action Panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(new Color(236, 240, 241));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttons = {"Add Expense", "Edit", "Delete"};
        Color[] btnColors = {new Color(41, 128, 185), new Color(39, 174, 96), new Color(192, 57, 43)};

        for (int i = 0; i < buttons.length; i++) {
            JButton btn = new JButton(buttons[i]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(150, 50));
            btn.setForeground(Color.WHITE);
            btn.setBackground(btnColors[i]);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Roboto", Font.BOLD, 16));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
            final int index = i; // Chỉ số nút
            btn.addActionListener(e -> {
                if (buttons[index].equals("Add Expense")) {
                    // Hiển thị form nhập liệu
                    JTextField categoryField = new JTextField();
                    JTextField descriptionField = new JTextField();
                    JTextField dateField = new JTextField();
                    JTextField amountField = new JTextField();
        
                    Object[] message = {
                        "Category:", categoryField,
                        "Description:", descriptionField,
                        "Date (yyyy-MM-dd):", dateField,
                        "Amount:", amountField
                    };
        
                    int option = JOptionPane.showConfirmDialog(
                            frame, message, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);
        
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            String category = categoryField.getText().trim();
                            String description = descriptionField.getText().trim();
                            String date = dateField.getText().trim();
                            double amount = Double.parseDouble(amountField.getText().trim());
                            String datetoFormat = formatDateString(date);
                            // Kiểm tra dữ liệu đầu vào
                            if (category.isEmpty() || description.isEmpty() || date.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "Please fill in all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
        
                            // Thêm dữ liệu vào JTable
                            model.addRow(new Object[]{category, description, date, amount});
                            
                            // Thêm dữ liệu vào SQL
                            String insertSQL = "INSERT INTO expenses (category, description, date, amount) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                                pstmt.setString(1, category);
                                pstmt.setString(2, description);
                                pstmt.setDate(3, java.sql.Date.valueOf(datetoFormat));
                                pstmt.setDouble(4, amount);
                                pstmt.executeUpdate();
                            }
                            JOptionPane.showMessageDialog(frame, "Expense added successfully!");
        
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid amount format! Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid date format! Use yyyy-MM-dd.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                } 
                // Chức năng Edit và Delete vẫn giữ nguyên
                else if (buttons[index].equals("Edit")) {
                    JOptionPane.showMessageDialog(frame, "Edit Expense clicked!", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else if (buttons[index].equals("Delete")) {
                    JOptionPane.showMessageDialog(frame, "Delete Expense clicked!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        
            actionPanel.add(Box.createVerticalStrut(20));
            actionPanel.add(btn);
        }

        // Main Area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(tableScrollPane, BorderLayout.CENTER);
        mainArea.add(actionPanel, BorderLayout.EAST);

        // Add vào Frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainArea, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Hiển thị form Add Expense
    private void showAddExpenseDialog(DefaultTableModel model, Connection conn, JFrame frame) {
        JTextField categoryField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField amountField = new JTextField();

        Object[] message = {
            "Category:", categoryField,
            "Description:", descriptionField,
            "Date (yyyy-MM-dd):", dateField,
            "Amount:", amountField
        };

        int option = JOptionPane.showConfirmDialog(
                frame, message, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String category = categoryField.getText();
                String description = descriptionField.getText();
                String date = dateField.getText();
                double amount = Double.parseDouble(amountField.getText());

                model.addRow(new Object[]{category, description, date, amount});

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
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Phương thức chuẩn hóa định dạng ngày
    public String formatDateString(String inputDate) throws IllegalArgumentException {
    String[] possibleFormats = {"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", "d/M/yyyy", "M/d/yyyy"};
    for (String format : possibleFormats) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setLenient(false); // Không chấp nhận ngày không hợp lệ
            Date date = sdf.parse(inputDate);

            // Chuyển về định dạng yyyy-MM-dd
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Thử với định dạng tiếp theo
        }
    }
    throw new IllegalArgumentException("Invalid date format!");
}
}
