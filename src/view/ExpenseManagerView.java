package view;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.ExpenseManagerModel;








public class ExpenseManagerView {
    private JPanel mainArea; // Main Area chứa nội dung chính
    private JScrollPane tableScrollPane; // Bảng Recent Transactions
    private JPanel actionPanel; // Panel chứa các nút chức năng
    private DefaultTableModel model; // Table Model dùng để cập nhật dữ liệu
    private Connection conn; // Kết nối CSDL
   
    

    public void createUI() throws SQLException {
        // Kết nối CSDL
        conn = SQLConnection.getSQLServerConnection();

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

        // Menu bên trái
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(44, 62, 80));
        sidePanel.setPreferredSize(new Dimension(220, 750));

        String[] menuItems = { "Home", "Dashboard", "Expenses", "Analytics", "Calendar", "Categories", "Settings" };
        String[] icons = { "\uD83C\uDFE0", "\uD83D\uDCCA", "\uD83D\uDCB8", "\uD83D\uDCCA", "\uD83D\uDCC5", "\uD83D\uDDC3",
                "\u2699" };

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

            tableScrollPane = new JScrollPane(table);
            tableScrollPane.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

            // Tạo Action Panel
            actionPanel = new JPanel();
            actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
            actionPanel.setBackground(new Color(236, 240, 241));
            actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            String[] buttons = { "Add Expense", "Edit", "Delete" };
            Color[] btnColors = { new Color(41, 128, 185), new Color(39, 174, 96), new Color(192, 57, 43) };

            for (int i = 0; i < buttons.length; i++) {
                JButton btn = createActionButton(buttons[i], btnColors[i]);
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
