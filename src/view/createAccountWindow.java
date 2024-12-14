package view;
import java.awt.*;
import javax.swing.*;

public class createAccountWindow {
    public createAccountWindow(JFrame parentFrame) {
    // Tạo cửa sổ đăng ký
    JFrame signUpFrame = new JFrame("Create Account");
    signUpFrame.setSize(600, 500);
    signUpFrame.setLocationRelativeTo(parentFrame);  // Canh giữa cửa sổ đăng ký với cửa sổ cha
    signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    signUpFrame.setLayout(new BorderLayout());

    // Tạo panel bên trái cho hình ảnh hoặc mô tả
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    leftPanel.setBackground(new Color(232, 234, 237));
    leftPanel.setPreferredSize(new Dimension(250, 500));

    // Dòng chữ mô tả
    JLabel descriptionLabel = new JLabel("CREATE A NEW ACCOUNT");
    descriptionLabel.setFont(new Font("Arial", Font.BOLD, 24));
    descriptionLabel.setForeground(new Color(106, 112, 128));

    // Đọc hình ảnh từ file và tạo ImageIcon
    ImageIcon imageIcon = new ImageIcon("signup-image.jpg"); // Thay thế với đường dẫn hình ảnh của bạn

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

    // Tạo panel bên phải (form tạo tài khoản)
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new GridBagLayout());
    rightPanel.setBackground(Color.WHITE);
    rightPanel.setPreferredSize(new Dimension(350, 500));

    // Tiêu đề form tạo tài khoản
    JLabel signUpLabel = new JLabel("Create Account");
    signUpLabel.setFont(new Font("Arial", Font.BOLD, 28));
    signUpLabel.setForeground(new Color(46, 47, 54));

    // Username label và text field
    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();
    usernameField.setPreferredSize(new Dimension(300, 30));
    usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
    usernameField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

    // Email label và text field
    JLabel emailLabel = new JLabel("Email:");
    JTextField emailField = new JTextField();
    emailField.setPreferredSize(new Dimension(300, 30));
    emailField.setFont(new Font("Arial", Font.PLAIN, 14));
    emailField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

    // Password label và password field
    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();
    passwordField.setPreferredSize(new Dimension(300, 30));
    passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
    passwordField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

    // Confirm password label và password field
    JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
    JPasswordField confirmPasswordField = new JPasswordField();
    confirmPasswordField.setPreferredSize(new Dimension(300, 30));
    confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
    confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

    // Nút đăng ký
    JButton signUpButton = new JButton("Sign Up");
    signUpButton.setBackground(new Color(76, 175, 80));
    signUpButton.setForeground(Color.WHITE);
    signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
    signUpButton.setPreferredSize(new Dimension(180, 40));

    // Bo góc cho nút Sign Up
    signUpButton.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
        BorderFactory.createEmptyBorder(10, 20, 10, 20)
    ));

    // Căn chỉnh tiêu đề tạo tài khoản
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(20, 20, 20, 20);
    gbc.anchor = GridBagConstraints.CENTER;
    rightPanel.add(signUpLabel, gbc);

    // Căn chỉnh username
    gbc.gridy = 1;
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(usernameLabel, gbc);
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(usernameField, gbc);

    // Căn chỉnh email
    gbc.gridy = 3;
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(emailLabel, gbc);
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(emailField, gbc);

    // Căn chỉnh password
    gbc.gridy = 5;
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(passwordLabel, gbc);
    gbc.gridy = 6;
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(passwordField, gbc);

    // Căn chỉnh confirm password
    gbc.gridy = 7;
    gbc.insets = new Insets(10, 20, 10, 20);
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(confirmPasswordLabel, gbc);
    gbc.gridy = 8;
    gbc.anchor = GridBagConstraints.WEST;
    rightPanel.add(confirmPasswordField, gbc);

    // Căn chỉnh nút đăng ký
    gbc.gridy = 9;
    gbc.insets = new Insets(20, 20, 20, 20);
    gbc.anchor = GridBagConstraints.CENTER;
    rightPanel.add(signUpButton, gbc);

    // Thêm panel bên trái và bên phải vào cửa sổ đăng ký
    signUpFrame.add(leftPanel, BorderLayout.WEST);
    signUpFrame.add(rightPanel, BorderLayout.CENTER);

    // Hiển thị cửa sổ đăng ký
    signUpFrame.setVisible(true);
}
}

