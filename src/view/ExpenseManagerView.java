package view;

import CRUD.AddExpense;
import CRUD.DeleteExpense;
import CRUD.EditExpense;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.ExpenseManagerModel;

public class ExpenseManagerView {
    private JPanel mainArea; // Main Area chứa nội dung chính
    private JScrollPane tableScrollPane; // Bảng Recent Transactions
    private JPanel actionPanel; // Panel chứa các nút chức năng
    private DefaultTableModel model; // Table Model dùng để cập nhật dữ liệu
    private Connection conn; // Kết nối CSDL
    private JFrame frame;
    private JTextField propertyField;

    public void createUI() throws SQLException {
        // Kết nối CSDL
        conn = SQLConnection.getSQLServerConnection();

        frame = new JFrame("Expense Tracker");
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

        // Tài sản ("Property")
        propertyField = new JTextField("Property: 0.00"); // Kết hợp cả "Property:" và số tiền
        propertyField.setForeground(Color.WHITE);
        propertyField.setFont(new Font("Roboto", Font.PLAIN, 18));
        propertyField.setBackground(new Color(41, 128, 185)); // Giữ màu nền giống header
        propertyField.setHorizontalAlignment(SwingConstants.RIGHT); // Căn phải
        propertyField.setBorder(BorderFactory.createEmptyBorder()); // Loại bỏ viền
        headerPanel.add(propertyField, BorderLayout.EAST);
        propertyField.setPreferredSize(new Dimension(200, 30)); // Tăng kích thước chiều rộng


        // Lấy giá trị "Property" từ CSDL và hiển thị
        updatePropertyField();
        propertyField.addActionListener(e -> {
            try {
                // Loại bỏ chữ "Property:" trước khi chuyển đổi sang số
                String text = propertyField.getText().replace("Property: ", "").trim();
                double newProperty = Double.parseDouble(text);
                updatePropertyInDatabase(newProperty);
                JOptionPane.showMessageDialog(frame, "Property updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number format. Please enter a valid number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                updatePropertyField(); // Khôi phục giá trị cũ nếu lỗi
            }
        });

        // Menu bên trái
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(44, 62, 80));
        sidePanel.setPreferredSize(new Dimension(220, 750));

        String[] menuItems = { "Home", "Dashboard", "Expense analytics", "Income analytics","Calendar", "Settings" };
        String[] icons = { "\uD83C\uDFE0", "\uD83D\uDCCA", "\uD83D\uDCCA", "\uD83D\uDCCA", "\uD83D\uDCC5", "\uD83D\uDDC3"};


        mainArea = new JPanel(new BorderLayout());
        createRecentTransactionsPanel(); // Hiển thị bảng Recent Transactions mặc định

        // Tạo menu
        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = createMenuButton(menuItems[i], icons[i]);
            String menu = menuItems[i];

            btn.addActionListener(e -> {
                mainArea.removeAll();
                if (menu.equals("Home")) {
                    createRecentTransactionsPanel();
                } else if (menu.equals("Dashboard")) {
                    mainArea.add(new DashboardPanel(conn), BorderLayout.CENTER);
                }
                else if (menu.equals("Analytics")) {
                    
                        AnalyticsPanel analyticsPanel = new AnalyticsPanel(conn, mainArea);
                    
                        mainArea.removeAll(); // Xóa nội dung cũ
                        mainArea.add(analyticsPanel, BorderLayout.CENTER); // Thêm panel vào mainArea
                        mainArea.revalidate();
                        mainArea.repaint();
                    
                        analyticsPanel.showAnalyticsOptions(); // Gọi showAnalyticsOptions sau khi đã hiển thị
                    
                    
                    
                }
                else  {
                    mainArea.add(new JLabel("Feature: " + menu, SwingConstants.CENTER), BorderLayout.CENTER);
                }
                mainArea.revalidate();
                mainArea.repaint();
            });

            sidePanel.add(Box.createVerticalStrut(10));
            sidePanel.add(btn);
        }

        // Thêm các thành phần vào frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainArea, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Tạo bảng Recent Transactions và Action Panel
    private void createRecentTransactionsPanel() {
        try {
            List<ExpenseManagerModel> expenseList = DBUTills.getExpenses(conn);
    
            String[] columnNames = { "Category", "Description", "Date", "Amount" };
            model = new DefaultTableModel(columnNames, 0);
    
            for (ExpenseManagerModel expense : expenseList) {
                model.addRow(new Object[] { expense.getCategory(), expense.getDescription(), expense.getDate(),
                        expense.getAmount() });
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
                if (i != 3)
                    table.getColumnModel().getColumn(i).setCellRenderer(alignCenterRenderer);
            }

            tableScrollPane = new JScrollPane(table);
            tableScrollPane.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));
    
            // Tạo Action Panel
            actionPanel = new JPanel();
            actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
            actionPanel.setBackground(new Color(236, 240, 241));
            actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            String[] buttons = { "Add Expense", "Edit Expense", "Delete Expense" };
            Color[] btnColors = { new Color(41, 128, 185), new Color(39, 174, 96), new Color(192, 57, 43) };

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
                        AddExpense.AddExpense(model, conn, frame, propertyField);
                    } else if (buttons[index].equals("Edit Expense")) {
                        EditExpense.EditExpense(model, conn, frame, table, propertyField);
                    } else if (buttons[index].equals("Delete Expense")) {
                        DeleteExpense.DeleteExpense(model, conn, frame, table, propertyField);
                    }
                });

                actionPanel.add(Box.createVerticalStrut(20));
                actionPanel.add(btn);
            }

            mainArea.add(tableScrollPane, BorderLayout.CENTER);
            mainArea.add(actionPanel, BorderLayout.EAST);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Tạo nút menu
    private JButton createMenuButton(String text, String icon) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFont(new Font("Roboto", Font.PLAIN, 16));
        return btn;
    }

    // Tạo nút chức năng (Add, Edit, Delete)
    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(150, 50));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFont(new Font("Roboto", Font.BOLD, 16));

        if (text.equals("Add Expense")) {
            btn.addActionListener(e -> showAddExpenseDialog());
        } else {
            btn.addActionListener(e -> JOptionPane.showMessageDialog(null, text + " clicked!", "Info",
                    JOptionPane.INFORMATION_MESSAGE));
        }
        return btn;
    }

    private void updatePropertyInDatabase(double newProperty) {
        try {
            String updateSQL = "UPDATE personal SET property = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setDouble(1, newProperty);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to update property in the database.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updatePropertyField() {
        try {
            String query = "SELECT property FROM personal";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (var rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        double property = rs.getDouble("property");
                        propertyField.setText(String.format("Property: %.2f", property));
                    } else {
                        // Nếu không có giá trị nào, gán mặc định là 0
                        propertyField.setText("Property: 0.00");
                        initializeDefaultPropertyInDatabase(); // Gọi hàm thêm giá trị mặc định vào database
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Phương thức thêm giá trị mặc định vào database
    private void initializeDefaultPropertyInDatabase() {
        try {
            String insertSQL = "INSERT INTO personal (property) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setDouble(1, 0.00); // Gán giá trị mặc định là 0
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Hiển thị form Add Expense
    private void showAddExpenseDialog() {
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

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Expense", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String category = categoryField.getText().trim();
                String description = descriptionField.getText().trim();
                String date = dateField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());

                model.addRow(new Object[] { category, description, date, amount });

                String insertSQL = "INSERT INTO expenses (category, description, date, amount) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, category);
                    pstmt.setString(2, description);
                    pstmt.setDate(3, java.sql.Date.valueOf(date));
                    pstmt.setDouble(4, amount);
                    pstmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, "Expense added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}