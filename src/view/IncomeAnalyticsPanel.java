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

public class IncomeAnalyticsPanel extends JPanel {
    private Connection conn;
    private JPanel mainArea;

    public IncomeAnalyticsPanel(Connection conn, JPanel mainArea) {
        this.conn = conn;
        this.mainArea = mainArea;
        setLayout(new BorderLayout());
    }

    // Hiển thị tùy chọn phân tích khoản thu
    public void showIncomeAnalyticsOptions() {
        // Bước 1: Chọn năm từ cơ sở dữ liệu
        Integer selectedYear = selectYearFromDatabase();
        if (selectedYear == null) return; // Nếu không chọn năm, thoát

        // Bước 2: Chọn loại hiển thị (Monthly hoặc Yearly)
        String[] options = {"Monthly", "Yearly"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Do you want to view the chart by month or year?",
                "Income Analytics Options",
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
                createIncomeChart("month", Integer.parseInt(month), selectedYear);
            }
        } else if (choice == 1) {
            // Nếu chọn Yearly, hiển thị biểu đồ cả năm
            createIncomeChart("year", 0, selectedYear); // 0 đại diện cho tất cả các tháng trong năm
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
            return (Integer) JOptionPane.showInputDialog(
                    null,
                    "Select a year:",
                    "Select Year",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    years.toArray(),
                    years.get(0)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching years from database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void createIncomeChart(String type, int month, int year) {
        try {
            String query;
            if (type.equals("month")) {
                query = "SELECT category, SUM(amount) AS total " +
                        "FROM expenses WHERE YEAR(date) = ? AND MONTH(date) = ? AND amount > 0 GROUP BY category";
            } else {
                query = "SELECT category, SUM(amount) AS total " +
                        "FROM expenses WHERE YEAR(date) = ? AND amount > 0 GROUP BY category";
            }

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, year);
            if (type.equals("month")) pstmt.setInt(2, month);

            ResultSet rs = pstmt.executeQuery();

            DefaultPieDataset dataset = new DefaultPieDataset();

            while (rs.next()) {
                String category = rs.getString("category");
                double total = rs.getDouble("total"); // Lấy giá trị dương (không cần tuyệt đối)
                dataset.setValue(category, total);
            }

            if (dataset.getItemCount() == 0) {
                JOptionPane.showMessageDialog(null, "No data available for the selected period.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String title = type.equals("month")
                    ? "Income for Month " + month + " in Year " + year
                    : "Income for Year " + year;

            // Tạo biểu đồ tròn
            JFreeChart pieChart = ChartFactory.createPieChart(
                    title,
                    dataset,
                    true,
                    true,
                    false
            );

            PiePlot plot = (PiePlot) pieChart.getPlot();

            // Tạo định dạng cho phần trăm và số tiền
            DecimalFormat dollarFormat = new DecimalFormat("#,##0");
            DecimalFormat percentFormat = new DecimalFormat("0.00%");

            // Tùy chỉnh cách hiển thị nhãn
            plot.setLabelGenerator(new PieSectionLabelGenerator() {
                @Override
                public String generateSectionLabel(PieDataset dataset, Comparable key) {
                    Number value = dataset.getValue(key);
                    double percentage = value.doubleValue() / getDatasetTotal(dataset) * 100;
                    return String.format("%s\n(%s)", dollarFormat.format(value), percentFormat.format(percentage / 100));
                }

                @Override
                public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
                    return null;
                }
            });

            plot.setSimpleLabels(true);
            plot.setInteriorGap(0.04);
            plot.setLabelFont(new Font("Roboto", Font.BOLD, 13));
            plot.setLabelPaint(Color.WHITE); // Màu chữ trắng
            plot.setLabelBackgroundPaint(new Color(0, 0, 0, 0));
            plot.setLabelOutlinePaint(null);
            plot.setLabelShadowPaint(null);

            List<Color> colors = new ArrayList<>(Arrays.asList(
                new Color(0xC31432), new Color(0xF9C449), new Color(0x138086),
                new Color(0xF2B500), new Color(0x3C4CAD), new Color(0xDC8665), new Color(0x6F4F61),
                new Color(0x607D8B), new Color(0xD14032), new Color(0x297B8F), new Color(0x4F6D9B),
                new Color(0x9E5D47), new Color(0x534666), new Color(0x536976)
            ));

            int colorIndex = 0;
            for (int i = 0; i < dataset.getItemCount(); i++) {
                Comparable category = dataset.getKey(i);
                plot.setSectionPaint(category, colors.get(colorIndex % colors.size()));
                colorIndex++;
            }

            // Tùy chỉnh Chú thích (Legend)
            LegendTitle legend = pieChart.getLegend();
            legend.setItemFont(new Font("Roboto", Font.BOLD, 16));
            legend.setPosition(RectangleEdge.RIGHT);

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
