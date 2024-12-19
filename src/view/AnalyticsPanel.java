package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyticsPanel extends JPanel {
    private Connection conn;
    private JPanel mainArea;

    public AnalyticsPanel(Connection conn,JPanel mainArea) {
        this.conn = conn;
        this.mainArea= mainArea;
        setLayout(new BorderLayout());
    }

    // Hiển thị tùy chọn phân tích
    public void showAnalyticsOptions() {
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
        
        // Tạo định dạng cho phần trăm và số tiền
        DecimalFormat dollarFormat = new DecimalFormat("#,##0"); // Định dạng số tiền (không hiển thị .00)
        DecimalFormat percentFormat = new DecimalFormat("0.00%"); // Định dạng phần trăm

        // Tùy chỉnh cách hiển thị nhãn
        plot.setLabelGenerator(new PieSectionLabelGenerator() {
            @Override
            public String generateSectionLabel(PieDataset dataset, Comparable key) {
                Number value = dataset.getValue(key);
                double percentage = value.doubleValue() / getDatasetTotal(dataset) * 100;
                // Hiển thị số tiền trên dòng đầu tiên, phần trăm trên dòng thứ hai
                return String.format("%s\n(%s)", dollarFormat.format(value), percentFormat.format(percentage / 100));
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
                return null; // Không cần sử dụng phương thức này trong trường hợp này
            }
        });

        plot.setSimpleLabels(true);
        plot.setInteriorGap(0.04);
        plot.setLabelFont(new Font("Roboto", Font.BOLD, 14));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 255));
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);

        // Dãy màu mới bạn đã cung cấp
        List<Color> colors = new ArrayList<>(Arrays.asList(
            new Color(0xDC8665), new Color(0x138086), new Color(0x534666),
            new Color(0xF9C449),  new Color(0x3C4CAD),new Color(0x536976),
            new Color(0xC31432) ,new Color(0x297B8F) ,new Color(0x6F4F61) ,new Color(0xF2B500) ,
            new Color(0x4F6D9B),new Color(0x607D8B) ,new Color(0xD14032) ,new Color(0x9E5D47) 
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

// Phương thức tính tổng giá trị của tất cả các phần trong dataset
private double getDatasetTotal(PieDataset dataset) {
    double total = 0;
    for (int i = 0; i < dataset.getItemCount(); i++) {
        total += dataset.getValue(i).doubleValue();
    }
    return total;
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