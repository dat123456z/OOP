package view;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.ExpenseManagerModel;
import view.DashboardPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;





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
                    showAnalyticsOptions();
                    
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
    private void showAnalyticsOptions() {
        // Bước 1: Chọn năm từ cơ sở dữ liệu
        Integer selectedYear = selectYearFromDatabase();
        if (selectedYear == null) return; // Nếu không chọn năm, thoát
    
        // Bước 2: Chọn loại biểu đồ (theo tháng hoặc cả năm)
        String[] options = { "Monthly", "Yearly" };
        int choice = JOptionPane.showOptionDialog(
            null, 
            "Do you want to view the chart by month or year?", 
            "Analytics Options", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            options, 
            options[0]
        );
    
        if (choice == 0) {
            // Nếu chọn Monthly, yêu cầu nhập tháng
            String month = JOptionPane.showInputDialog(null, "Enter a month (1-12):", "Select Month", JOptionPane.PLAIN_MESSAGE);
            if (month != null && isValidMonth(month)) {
                createChart("month", Integer.parseInt(month), selectedYear);
            }
        } else if (choice == 1) {
            // Nếu chọn Yearly, hiển thị biểu đồ cả năm
            createChart("year", 0, selectedYear); // 0 đại diện cho tất cả các tháng trong năm
        }
    }
    
    private Integer selectYearFromDatabase() {
        try {
            String query = "SELECT DISTINCT YEAR(date) AS year FROM expenses ORDER BY year";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
    
            List<Integer> years = new ArrayList<>();
            while (rs.next()) {
                years.add(rs.getInt("year"));
            }
    
            if (years.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No data available for years.", "No Data", JOptionPane.WARNING_MESSAGE);
                return null;
            }
    
            // Hiển thị danh sách các năm để người dùng chọn
            Integer year = (Integer) JOptionPane.showInputDialog(
                null, 
                "Select a year:", 
                "Select Year", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                years.toArray(), 
                years.get(0)
            );
    
            return year;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching years from database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
  private void createChart(String type, int month, int year) {
    try {
        String query;
        if (type.equals("month")) {
            query = "SELECT category, SUM(amount) AS total "
                  + "FROM expenses WHERE YEAR(date) = ? AND MONTH(date) = ? GROUP BY category";
        } else { // type.equals("year")
            query = "SELECT category, SUM(amount) AS total "
                  + "FROM expenses WHERE YEAR(date) = ? GROUP BY category";
        }

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, year);
        if (type.equals("month")) pstmt.setInt(2, month); // Thêm tháng nếu cần

        ResultSet rs = pstmt.executeQuery();

        DefaultPieDataset dataset = new DefaultPieDataset();

        while (rs.next()) {
            String category = rs.getString("category");
            double total = rs.getDouble("total");
            dataset.setValue(category, total);
        }

        String title = type.equals("month") 
            ? "Expenses for Month " + month + " in Year " + year 
            : "Expenses for Year " + year;

        // Tạo biểu đồ tròn
        JFreeChart pieChart = ChartFactory.createPieChart(
            title,       // Tiêu đề biểu đồ
            dataset,     // Dữ liệu
            true,        // Hiển thị chú thích (legend)
            true,        // Hiển thị tooltips
            false        // URLs không cần thiết
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0}\n{1} ({2})", 
            new java.text.DecimalFormat("#,##0.00"), 
            new java.text.DecimalFormat("0.00%")
        ));
        plot.setSimpleLabels(true);
        plot.setInteriorGap(0.04);
        plot.setLabelFont(new Font("Roboto", Font.BOLD, 14));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 150));
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);

        // Tạo danh sách các màu sắc tùy chỉnh
        List<Color> colors = new ArrayList<>(Arrays.asList(
            new Color(0xDC8665), new Color(0x138086), new Color(0x534666),
            new Color(0xCD7672), new Color(0xEEB462), new Color(0x3C4CAD),
            new Color(0xF04393), new Color(0xF9C449), new Color(0x4ADEDE),
            new Color(0x78D5F5), new Color(0xCFF4D2)
        ));

        // Duyệt qua tất cả các phần trong dataset và gán màu
        int colorIndex = 0;
        for (int i = 0; i < dataset.getItemCount(); i++) {
            Comparable category = dataset.getKey(i);
            plot.setSectionPaint(category, colors.get(colorIndex % colors.size()));
            colorIndex++;
        }

        // Tùy chỉnh Chú thích (Legend)
        LegendTitle legend = new LegendTitle(plot);
        legend.setItemFont(new Font("Roboto", Font.BOLD, 16)); // Chỉnh kích thước font của chú thích
        legend.setPosition(RectangleEdge.RIGHT); // Đặt vị trí của chú thích (ở bên phải)
        
        pieChart.addLegend(legend);

        // Hiển thị biểu đồ trong JPanel
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        mainArea.removeAll();
        mainArea.add(chartPanel, BorderLayout.CENTER);
        mainArea.revalidate();
        mainArea.repaint();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error fetching data for chart: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

     
    private boolean isValidMonth(String month) {
        try {
            int m = Integer.parseInt(month);
            return m >= 1 && m <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }
             

}