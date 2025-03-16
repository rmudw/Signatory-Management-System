import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SignUp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/final_project";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    public SignUp() {
        setTitle("Sign Up");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // Sign Up Button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signUpButton.getPreferredSize().height));
        signUpButton.setBackground(Color.decode("#40AA5B"));
        signUpButton.setForeground(Color.WHITE);
        panel.add(signUpButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(panel);

        signUpButton.addActionListener(e -> registerUser());
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height)); // Fixed height
        return textField;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setEchoChar((char) 0);
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('\u2022');
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0);
                }
            }
        });
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height)); // Fixed height
        return passwordField;
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();

        // Validate username and password
        if (username.equals("Enter username") || password.isEmpty()) {
            displayErrorMessage("Username and password are required.");
            return;
        }

        if (isUsernameAvailable(username)) {
            String query = "INSERT INTO user (username, password) VALUES (?, ?)";
            try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, username);
                statement.setString(2, password);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    displayStatusMessage("Sign up successful. You can now log in.");
                    dispose(); // Close the SignUp frame after successful registration
                } else {
                    displayErrorMessage("Sign up failed. Please try again.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                displayErrorMessage("Error connecting to database.");
            }
        } else {
            displayErrorMessage("Username already taken.");
        }
    }

    private boolean isUsernameAvailable(String username) {
        String query = "SELECT * FROM user WHERE username = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next(); // If there is no existing username in the database, it's available
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false; // Assume username is not available on error
        }
    }

    private void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void displayStatusMessage(String message) {
        StatusDialog statusDialog = new StatusDialog(this, message);
        statusDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignUp signUp = new SignUp();
            signUp.setVisible(true);
        });
    }
}

class StatusDialog extends JDialog {

    public StatusDialog(JFrame parent, String message) {
        super(parent, "Status", true);
        setSize(300, 100);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(messageLabel);

        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setBackground(Color.decode("#40AA5B"));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dispose());

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(okButton);

        add(panel);
    }
}
