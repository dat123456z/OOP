
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ExpenseManager {

    private JFrame frame;
    private JTextField descriptionField, amountField, dateField;
    private JComboBox<String> categoryComboBox;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private List<Expense> expenses = new ArrayList<>();

    public ExpenseManager() {
        initialize();
    }

    public void initialize() {
        // Thiết lập giao diện FlatLaf
        frame = new JFrame("Expense Manager");
        frame.setBounds(100, 100, 700, 550);  // Tăng kích thước cửa sổ một chút
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Background màu sáng cho cửa sổ chính
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        // Panel nhập thông tin chi tiêu
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255));
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các component

        // Mô tả chi tiêu
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(descriptionLabel, gbc);

        descriptionField = new JTextField();
        descriptionField.setPreferredSize(new Dimension(200, 30));
        descriptionField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);

        // Số tiền chi tiêu
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountLabel.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(amountLabel, gbc);

        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 30));
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);

        // Loại chi tiêu
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryLabel.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(categoryLabel, gbc);

        categoryComboBox = new JComboBox<>(new String[]{"Food", "Transport", "Shopping", "Entertainment"});
        categoryComboBox.setPreferredSize(new Dimension(200, 30));
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        inputPanel.add(categoryComboBox, gbc);

        // Ngày chi tiêu (dd/MM/yyyy)
        JLabel dateLabel = new JLabel("Date (dd/MM/yyyy):");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(dateLabel, gbc);

        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(200, 30));
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);

        // Nút thêm chi tiêu
        JButton addButton = new JButton("Add Expense");
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(150, 35));
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        inputPanel.add(addButton, gbc);

        // Panel hiển thị danh sách chi tiêu
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(new Color(255, 255, 255));
        frame.getContentPane().add(tablePanel, BorderLayout.CENTER);

        // Tạo table để hiển thị các chi tiêu
        String[] columns = {"Description", "Amount", "Category", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(30); // Tăng chiều cao dòng
        expenseTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        tablePanel.add(scrollPane);

        // Panel tính tổng chi tiêu
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(new Color(255, 255, 255));
        frame.getContentPane().add(summaryPanel, BorderLayout.SOUTH);

        // Label tổng số tiền chi tiêu
        JLabel totalLabel = new JLabel("Total Expenses: ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        summaryPanel.add(totalLabel);

        JLabel totalAmountLabel = new JLabel("0");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalAmountLabel.setForeground(new Color(0, 122, 255));
        summaryPanel.add(totalAmountLabel);
        frame.setVisible(true);
    }
// Lớp đại diện cho mỗi chi tiêu
class Expense {
    private String description;
    private double amount;
    private String category;
    private Date date;

    public Expense(String description, double amount, String category, Date date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }
}

// Phương thức thêm chi tiêu vào bảng và danh sách
private void addExpense() {
    // Lấy dữ liệu từ các trường nhập liệu
    String description = descriptionField.getText().trim();
    String amountText = amountField.getText().trim();
    String category = (String) categoryComboBox.getSelectedItem();
    String dateText = dateField.getText().trim();

    // Kiểm tra đầu vào
    if (description.isEmpty() || amountText.isEmpty() || dateText.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Kiểm tra xem số tiền có hợp lệ không
    double amount;
    try {
        amount = Double.parseDouble(amountText);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(frame, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Kiểm tra và chuyển đổi ngày theo định dạng dd/MM/yyyy
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.setLenient(false); // Không cho phép nhập ngày sai định dạng
    Date date = null;
    try {
        date = sdf.parse(dateText);
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(frame, "Please enter a valid date in dd/MM/yyyy format", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Tạo đối tượng Expense mới và thêm vào danh sách
    Expense expense = new Expense(description, amount, category, date);
    expenses.add(expense);

    // Thêm vào bảng
    tableModel.addRow(new Object[]{
        expense.getDescription(),
        expense.getAmount(),
        expense.getCategory(),
        sdf.format(expense.getDate()) // Hiển thị ngày ở định dạng dd/MM/yyyy
    });

    // Cập nhật tổng chi tiêu
    updateTotalExpenses();

    // Xóa các trường nhập liệu sau khi thêm chi tiêu
    descriptionField.setText("");
    amountField.setText("");
    dateField.setText("");
}

// Phương thức cập nhật tổng chi tiêu
private void updateTotalExpenses() {
    double total = 0;
    for (Expense expense : expenses) {
        total += expense.getAmount();
    }
    // Cập nhật label tổng chi tiêu
    JLabel totalAmountLabel = (JLabel) ((JPanel) frame.getContentPane().getComponent(2)).getComponent(1);
    totalAmountLabel.setText(String.format("%.2f", total));
}

}
