import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Login {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/final_project";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";

    public Login() {
        frame = new JFrame("Login");
        frame.setSize(300, 180);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Username field with placeholder
        usernameField = createTextField("Enter username");
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Password field with placeholder
        passwordField = createPasswordField("Enter password");
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, loginButton.getPreferredSize().height));
        loginButton.setBackground(Color.decode("#40AA5B"));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Sign Up Button
        signUpButton = new JButton("Sign Up");
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signUpButton.getPreferredSize().height));
        signUpButton.setBackground(Color.decode("#40AA5B"));
        signUpButton.setForeground(Color.WHITE);
        panel.add(signUpButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        frame.add(panel);

        loginButton.addActionListener(e -> authenticateUser());
        signUpButton.addActionListener(e -> openSignUpForm());
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height));
        return textField;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setEchoChar((char) 0);
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('\u2022');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        return passwordField;
    }

    private void authenticateUser() {
        String username = usernameField.getText().equals("Enter username") ? "" : usernameField.getText();
        String password = String.valueOf(passwordField.getPassword()).equals("Enter password") ? "" : String.valueOf(passwordField.getPassword());

        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Successful login
                frame.dispose(); // Close the login frame
                openMainClass(username); // Open main GUI with the fetched username
            } else {
                // Failed login
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error connecting to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSignUpForm() {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }

    private void openMainClass(String username) {
        SwingUtilities.invokeLater(() -> new MainClass(username));
    }

    public void showLogin() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.showLogin();
        });
    }
}
