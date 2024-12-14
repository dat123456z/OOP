package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ExpenseManagerView {


    public void createUI() {
        // Tạo JFrame
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // Header (phần trên cùng)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1200, 80));

        JLabel appTitle = new JLabel("Expense Tracker");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("Roboto", Font.BOLD, 28));
        appTitle.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(appTitle, BorderLayout.WEST);

        // Thay nút Profile bằng nút Search
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Roboto", Font.PLAIN, 16));
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.add(searchButton, BorderLayout.EAST);


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

        // Tổng quan (Balance, Income, Expenses)
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(1, 3, 15, 15));
        summaryPanel.setBackground(new Color(236, 240, 241));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] labels = {"Total Balance", "Expenses", "Income"};
        String[] amounts = {"$8,200", "$5,210", "$13,410"};
        Color[][] gradients = {
                {new Color(41, 128, 185), new Color(109, 213, 250)},
                {new Color(192, 57, 43), new Color(239, 100, 100)},
                {new Color(39, 174, 96), new Color(102, 255, 153)}
        };

        for (int i = 0; i < labels.length; i++) {
            final int index = i;
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(0, 0, gradients[index][0], getWidth(), getHeight(), gradients[index][1]);
                    g2d.setPaint(gp);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            };
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel title = new JLabel(labels[index]);
            title.setForeground(Color.WHITE);
            title.setFont(new Font("Roboto", Font.BOLD, 18));

            JLabel value = new JLabel(amounts[index]);
            value.setForeground(Color.WHITE);
            value.setFont(new Font("Roboto", Font.BOLD, 24));
            value.setHorizontalAlignment(SwingConstants.CENTER);

            panel.add(title, BorderLayout.NORTH);
            panel.add(value, BorderLayout.CENTER);

            summaryPanel.add(panel);
        }

        // icon cho bảng Recent Transactions
        // String[] img = {"D:/OOP_PROJECT/src/img/food.png", "D:/OOP_PROJECT/src/img/housing.png", "D:/OOP_PROJECT/src/img/income.png", "D:/OOP_PROJECT/src/img/transport.png", "D:/OOP_PROJECT/src/img/shopping.png"};
        // Phần bảng (Recent Transactions)
        String[] columnNames = {"Category", "Description", "Date", "Amount"};
        Object[][] data = {
            {"Food", "Lunch at restaurant", "2023-10-01", "-$15.00"},
            {"Housing", "Monthly rent", "2023-10-01", "-$1200.00"},
            {"Income", "Salary", "2023-10-01", "+$3000.00"},
            {"Transport", "Bus ticket", "2023-10-01", "-$2.50"},
            {"Shopping", "New shoes", "2023-10-01", "-$50.00"}
        };
        // for (int i = 0; i < data.length; i++) {
        //     ImageIcon icon = new ImageIcon(img[i % img.length]);
        //     data[i][0] = new JLabel(icon);
        // }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);

        DefaultTableCellRenderer amountRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value != null && value.toString().startsWith("-")) {
                    setForeground(Color.RED); // Màu đỏ cho số âm
                } else if (value != null && value.toString().startsWith("+")) {
                    setForeground(new Color(39, 174, 96)); // Màu xanh cho số dương
                } else {
                    setForeground(Color.BLACK);
                }
                super.setValue(value);
            }
        };
        table.getColumnModel().getColumn(3).setCellRenderer(amountRenderer);

        table.setRowHeight(30);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        // Phần nút hành động
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
            actionPanel.add(Box.createVerticalStrut(20));
            actionPanel.add(btn);
        }

        // Main Area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(summaryPanel, BorderLayout.NORTH);
        mainArea.add(tableScrollPane, BorderLayout.CENTER);
        mainArea.add(actionPanel, BorderLayout.EAST);

        // Add components to frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainArea, BorderLayout.CENTER);

        frame.setVisible(true);
    }
} 