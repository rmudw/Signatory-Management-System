import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainClass {
    JFrame mainFrame = new JFrame("Land Bank of the Philippines Authorized Signatory Management System");
    JTabbedPane actionTab = new JTabbedPane(JTabbedPane.LEFT); // Create tabbed panels
    JPanel addRecordPanel = new JPanel();
    JPanel sqlCodesPanel = new JPanel();
    JPanel userInfoPanel = new JPanel();
    JPanel menu = new JPanel();
    GridBagConstraints gbc = new GridBagConstraints();

    Records viewRecordPanel = new Records();
    Form form = new Form();
    SQLCodes sqlCode = new SQLCodes();

    public MainClass(String username) {
        userInfo(username);
        viewRecordPanel.user = username;

        ImageIcon logo = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\logo.png");
        ImageIcon user = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\user.png");
        ImageIcon sql = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\sqlcode.png");
        ImageIcon add = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\add.png");
        ImageIcon view = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\view.png");

        // Buttons panel on form section
        buttonMenu(menu);
        menu.setBackground(Color.WHITE);
        menu.setPreferredSize(new Dimension(800, 70));
        menu.setMaximumSize(new Dimension(800, 70));

        JPanel menuLayout = new JPanel(new GridBagLayout());
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 1135);
        menuLayout.setMaximumSize(new Dimension(1400, 150));
        menuLayout.setBackground(Color.WHITE);
        menuLayout.add(menu, gbc);

        // Customer form
        addRecordPanel.setLayout(new BoxLayout(addRecordPanel, BoxLayout.Y_AXIS));
        addRecordPanel.setBackground(Color.WHITE);
        addRecordPanel.add(menuLayout);
        addRecordPanel.add(form);

        // Tabs
        actionTab.addTab("Add Record", add, addRecordPanel, "Add a new record");
        actionTab.addTab("View Records", view, viewRecordPanel, "View database records");
        actionTab.addTab("SQL Codes", sql, sqlCode, "Show embedded SQL codes");
        actionTab.addTab("User", user, userInfoPanel, "Show user info");
        actionTab.setSelectedIndex(3); // Set default tab to "User"
        actionTab.setBackground(Color.WHITE);

        // Set green background color for the tabs
        for (int i = 0; i < actionTab.getTabCount(); i++) {
            actionTab.setBackgroundAt(i, Color.decode("#40AA5B")); // Set background color to green
            actionTab.setForegroundAt(i, Color.WHITE); // Set text color to white
        }

        // ChangeListener to handle tab selection change
        actionTab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = actionTab.getSelectedIndex();
                for (int i = 0; i < actionTab.getTabCount(); i++) {
                    if (i == selectedIndex) {
                        actionTab.setBackgroundAt(i, Color.WHITE); // Set background color to white for selected tab
                        actionTab.setForegroundAt(i, Color.BLACK); // Set text color to black for selected tab
                    } else {
                        actionTab.setBackgroundAt(i, Color.decode("#40AA5B")); // Reset background color for other tabs
                        actionTab.setForegroundAt(i, Color.WHITE); // Reset text color for other tabs
                    }
                }
            }
        });

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setBackground(Color.WHITE);
        mainFrame.setIconImage(logo.getImage());
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1350, 750);

        // Center the tabbed pane within the frame
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(actionTab, BorderLayout.CENTER);

        mainFrame.add(centerPanel, BorderLayout.CENTER);
        mainFrame.pack();
    }

    private void buttonMenu(JPanel panel) {
        int HEIGHT = 50;
        int WIDTH = 110;

        ImageIcon save = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\save.png");
        ImageIcon clear = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\clear.png");

        panel.setLayout(new GridBagLayout());

        // SAVE RECORD
        JButton saveButton = new JButton("Save", save);
        saveButton.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        saveButton.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(Color.decode("#40AA5B")); // Set background color
        saveButton.setForeground(Color.WHITE); // Set foreground (text) color
        saveButton.addActionListener(this::saveRecord);

        // CLEAR FORM
        JButton clearButton = new JButton("Clear", clear);
        clearButton.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        clearButton.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setBackground(Color.decode("#40AA5B")); // Set background color
        clearButton.setForeground(Color.WHITE); // Set foreground (text) color
        clearButton.addActionListener(this::clearForm);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel.add(saveButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel.add(clearButton, gbc);

        panel.setBackground(Color.WHITE); // Set background color to white
        panel.setForeground(Color.BLACK); // Set foreground (text) color
    }

    private void userInfo(String username) {
        JLabel systemName = new JLabel("Land Bank of the Philippines Authorized Signatory Management System", SwingConstants.CENTER);
        systemName.setFont(new Font("Arial", Font.BOLD, 30));
        systemName.setForeground(Color.decode("#40AA5B"));
        
        JLabel welcome = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
       
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
        labelsPanel.setBackground(Color.WHITE);
        labelsPanel.add(systemName);
        labelsPanel.add(welcome);
    
        // Create a new panel with BoxLayout to center align vertically
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for precise positioning
        centerPanel.setBackground(Color.WHITE);
    
        // Set constraints to center align vertically and horizontally
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // 100% horizontal space
        gbc.weighty = 1.0; // 100% vertical space
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        centerPanel.add(labelsPanel, gbc);
    
        userInfoPanel.setLayout(new BorderLayout()); // Use BorderLayout for center alignment
        userInfoPanel.setBackground(Color.WHITE);
        userInfoPanel.add(centerPanel, BorderLayout.CENTER); // Add the centerPanel to the center of BorderLayout
    }
    
    private void saveRecord(ActionEvent click) {
        form.saveData();
        viewRecordPanel.refresh();
    }

    private void clearForm(ActionEvent click) {
        form.clearText();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.showLogin();
        });
    }
}
