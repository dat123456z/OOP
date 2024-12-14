package view;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import view.ExpenseManagerView;
public class ModernLoginForm {
    public void UI() {
        // Tạo cửa sổ JFrame
        JFrame frame = new JFrame("Modern Login Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);  // Mở rộng kích thước cửa sổ
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Canh giữa màn hình

        // Tạo panel bên trái (mô tả)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        leftPanel.setBackground(new Color(232, 234, 237));
        leftPanel.setPreferredSize(new Dimension(600, 500));  // Mở rộng panel bên trái

        // Dòng chữ mô tả
        Font font = new Font("Arial", Font.BOLD, 24);
        JLabel descriptionLabel = new JLabel("WELCOME");
        descriptionLabel.setFont(font);
        descriptionLabel.setForeground(new Color(106, 112, 128));

        // Đọc hình ảnh từ file và tạo ImageIcon
        ImageIcon imageIcon = new ImageIcon("D:\\OOP_PROJECT\\src\\img\\img-login.jpg");


        // Tạo JLabel và đặt hình ảnh vào
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER); // Căn giữa hình ảnh
        imageLabel.setVerticalAlignment(JLabel.CENTER); // Căn giữa hình ảnh

        // Căn chỉnh mô tả và hình ảnh
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 10, 20); // Lề trên, trái, dưới, phải
        leftPanel.add(descriptionLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 30, 20); // Lề trên, trái, dưới, phải
        leftPanel.add(imageLabel, gbc);

        // Tạo panel bên phải (form đăng nhập)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(400, 500));  // Mở rộng panel bên phải

        // Tiêu đề form đăng nhập
        JLabel loginLabel = new JLabel("User Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 28));
        loginLabel.setForeground(new Color(46, 47, 54));

        // Username label và text field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 30));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        usernameField.setMargin(new Insets(5, 5, 5, 5));

        // Password label và password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        passwordField.setMargin(new Insets(5, 5, 5, 5));

        // Nút đăng nhập
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(180, 40));

        // Bo góc cho nút Login
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80), 2), // Viền nút
            BorderFactory.createEmptyBorder(10, 20, 10, 20) // Bo góc với khoảng cách padding
        ));

        // Căn chỉnh tiêu đề đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(loginLabel, gbc);

        // Căn chỉnh username
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 10, 20); // Lề trên, trái, dưới, phải
        gbc.anchor = GridBagConstraints.WEST; // Căn lề trái
        rightPanel.add(usernameLabel, gbc);
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST; // Căn lề trái
        rightPanel.add(usernameField, gbc);

        // Căn chỉnh password
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST; // Căn lề trái
        rightPanel.add(passwordLabel, gbc);
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST; // Căn lề trái
        rightPanel.add(passwordField, gbc);

        // Căn chỉnh nút login
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa nút login
        rightPanel.add(loginButton, gbc);

        // Tạo một JPanel cho "No account yet?" và "SIGN UP NOW"
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));  // Sử dụng FlowLayout để đặt chúng lên một dòng
        signUpPanel.setOpaque(false);  // Làm cho panel trong suốt

        // Dòng chữ "No account yet?"
        JLabel createAccountLabel = new JLabel("No account yet?");
        createAccountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        createAccountLabel.setForeground(new Color(76, 175, 80));

        // Nút đăng ký với bo góc sử dụng setBorder
        JButton createButton = new JButton("SIGN UP NOW");
        createButton.setBackground(new Color(76, 175, 80));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Arial", Font.BOLD, 16));
        createButton.setPreferredSize(new Dimension(180, 40));

        // Bo góc nút bằng EmptyBorder và LineBorder
        createButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80), 2), // Viền nút
                BorderFactory.createEmptyBorder(10, 20, 10, 20) // Bo góc với khoảng cách padding
        ));

        // Thêm vào panel
        signUpPanel.add(createAccountLabel);
        signUpPanel.add(createButton);

        // Thêm JPanel vào form đăng nhập
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 20, 10, 20);
        rightPanel.add(signUpPanel, gbc);

        // Thêm panel bên trái và bên phải vào cửa sổ
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        // Hiển thị cửa sổ
        frame.setVisible(true);

        // Sự kiện cho nút SIGN UP NOW
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở cửa sổ đăng ký
                createAccountWindow(frame);
            }
        });

        // Sự kiện cho nút Login
loginButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Lấy username và password từ các trường nhập liệu
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();  // Lấy mật khẩu dạng char[]
        
        // Kiểm tra xem người dùng có nhập đủ thông tin không
        if (username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(frame, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Dừng lại nếu thông tin chưa đầy đủ
        }
        
        // Gọi phương thức để xác thực thông tin đăng nhập
        try {
            if (validateLogin(username, new String(password))) {
                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Tiến hành chuyển hướng hoặc các hành động khác sau khi đăng nhập thành công
                // Ví dụ: Mở một cửa sổ mới hoặc ẩn cửa sổ đăng nhập
                frame.dispose();  // Đóng cửa sổ đăng nhập
                // openDashboard(); // Ví dụ mở một cửa sổ chính sau khi đăng nhập thành công
                ExpenseManagerView expenseManager = new ExpenseManagerView();
                expenseManager.createUI(); // Ví dụ mở ứng dụng quản lý chi tiêu
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error connecting to the database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});

    }


    // Phương thức xác thực đăng nhập
private static boolean validateLogin(String username, String password) throws SQLException {
    // Giả sử bạn có một cơ sở dữ liệu với bảng users
    // Kiểm tra xem tên người dùng và mật khẩu có khớp không
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    
    try {
        connection = DatabaseConnection.initializeDatabaze(); // Giả sử bạn đã có lớp DatabaseConnection để kết nối CSDL
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password); // Mật khẩu đã được mã hóa hoặc chưa mã hóa tùy theo yêu cầu
        resultSet = statement.executeQuery();
        
        // Nếu có dòng dữ liệu trả về, tức là người dùng và mật khẩu hợp lệ
        return resultSet.next(); // Trả về true nếu tìm thấy người dùng
    } finally {
        // Đảm bảo đóng các tài nguyên
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }
}

private static void createAccountWindow(JFrame parentFrame) {
    JFrame signUpFrame = new JFrame("Create Account");
    signUpFrame.setSize(600, 500);
    signUpFrame.setLocationRelativeTo(parentFrame);  // Canh giữa cửa sổ
    signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel signUpPanel = new JPanel();
    signUpPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    signUpPanel.setBackground(Color.WHITE);

    // Tạo các label và field cho thông tin đăng ký
    JLabel signUpLabel = new JLabel("Sign Up");
    signUpLabel.setFont(new Font("Arial", Font.BOLD, 28));
    signUpLabel.setForeground(new Color(46, 47, 54));

    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();
    usernameField.setPreferredSize(new Dimension(300, 30));  // Mở rộng trường nhập

    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();
    passwordField.setPreferredSize(new Dimension(300, 30));  // Mở rộng trường nhập

    JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
    JPasswordField confirmPasswordField = new JPasswordField();
    confirmPasswordField.setPreferredSize(new Dimension(300, 30));  // Mở rộng trường nhập

    JButton signUpButton = new JButton("Sign Up");
    signUpButton.setBackground(new Color(76, 175, 80));
    signUpButton.setForeground(Color.WHITE);
    signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
    signUpButton.setPreferredSize(new Dimension(180, 40));

    // Thiết lập GridBagLayout cho các thành phần
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 10, 10, 10);
    signUpPanel.add(signUpLabel, gbc);

    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;  // Căn phải cho label
    signUpPanel.add(usernameLabel, gbc);
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;  // Căn phải cho text field
    signUpPanel.add(usernameField, gbc);

    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;  // Căn phải cho label
    signUpPanel.add(passwordLabel, gbc);
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.WEST;  // Căn phải cho text field
    signUpPanel.add(passwordField, gbc);

    gbc.gridy = 5;
    gbc.anchor = GridBagConstraints.WEST;  // Căn phải cho label
    signUpPanel.add(confirmPasswordLabel, gbc);
    gbc.gridy = 6;
    gbc.anchor = GridBagConstraints.WEST;  // Căn phải cho text field
    signUpPanel.add(confirmPasswordField, gbc);

    gbc.gridy = 7;
    gbc.anchor = GridBagConstraints.CENTER;  // Căn giữa cho nút đăng ký
    signUpPanel.add(signUpButton, gbc);

    signUpFrame.add(signUpPanel);
    signUpFrame.setVisible(true);

    // Sự kiện cho nút SIGN UP
    signUpButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Kiểm tra nếu mật khẩu và xác nhận mật khẩu khớp
        if (password.equals(confirmPassword)) {
            try {
                // Kết nối đến cơ sở dữ liệu
                Connection conn = DatabaseConnection.initializeDatabaze();
                String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(signUpFrame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                signUpFrame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(signUpFrame, "Error creating account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(signUpFrame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    
        signUpFrame.setVisible(true);
}
}