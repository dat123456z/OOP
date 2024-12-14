import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
public class tempCodeRunnerFile {
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
        ImageIcon imageIcon = new ImageIcon("img-login.jpg");

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

    // JTextField usernameField = new JTextField();
    // JPasswordField passwordField = new JPasswordField();
    // JPasswordField confirmPasswordField = new JPasswordField();
    // JButton signUpButton = new JButton("Sign Up");

    // signUpButton.addActionListener(new ModernLoginForm$2(passwordField, confirmPasswordField, signUpFrame, usernameField));


        // Sự kiện cho nút SIGN UP
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
    
                // Kiểm tra nếu mật khẩu và xác nhận mật khẩu khớp
                if (password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(signUpFrame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    signUpFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(signUpFrame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        signUpFrame.setVisible(true);
    }
    
}
