import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Update extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/final_project";
    private static final String USER = "root";
    private static final String PASS = "1234";

    private JButton updateButton;
    private JButton clearButton;
    private JButton cancelButton;
    private String customerNo;
    private String username;

    // Sections for displaying customer information
    PersonalInformation customerSection = new PersonalInformation();
    FinancialInformation financeSection = new FinancialInformation();
    EmploymentInformation employmentSection = new EmploymentInformation();

    public Update(String selectedCustomerNo, String username) {
        this.username = username;
        this.customerNo = selectedCustomerNo;
        setTitle("Update Customer Information");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                openMainClass(username);
            }
        });

        customerSection.setBackground(Color.WHITE);
        financeSection.setBackground(Color.WHITE);
        employmentSection.setBackground(Color.WHITE);

        JPanel placeHolder = new JPanel(); // Used to fix some background issues
        placeHolder.setLayout(new BoxLayout(placeHolder, BoxLayout.Y_AXIS));
        placeHolder.add(customerSection);
        placeHolder.add(financeSection);
        placeHolder.add(employmentSection);

        // Make the panel scrollable
        JPanel mainPanel = new JPanel();
        mainPanel.add(placeHolder);
        mainPanel.setBackground(Color.WHITE);
        JScrollPane scrollFrame = new JScrollPane(mainPanel);
        scrollFrame.setPreferredSize(new Dimension(1200, 500));
        scrollFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollFrame.getVerticalScrollBar().setUnitIncrement(10); // Scroll speed

        add(scrollFrame, BorderLayout.CENTER);

        // Fetch data from database and set in the sections
        fetchData();

        ImageIcon update = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\update.png");
        ImageIcon clear = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\clear_2.png");
        ImageIcon cancel = new ImageIcon("C:\\Users\\RM\\Desktop\\College\\2nd Year\\Information Management\\Database GUI\\v1\\Landbank Customer System\\src\\cancel.png");

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        updateButton = new JButton("Update");
        updateButton.setIcon(update);
        updateButton.setBackground(Color.decode("#40AA5B"));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> updateCustomerInfo());

        clearButton = new JButton("Clear");
        clearButton.setIcon(clear);
        clearButton.setBackground(Color.decode("#40AA5B"));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> clearFields());

        cancelButton = new JButton("Cancel");
        cancelButton.setIcon(cancel);
        cancelButton.setBackground(Color.decode("#40AA5B"));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> cancelFields());

        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void fetchData() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement custStmt = conn.prepareStatement("SELECT * FROM customer WHERE CustomerNo = ?");
                PreparedStatement financeStmt = conn.prepareStatement("SELECT * FROM financial WHERE CustomerNo = ?");
                PreparedStatement empStmt = conn.prepareStatement("SELECT * FROM employment WHERE CustomerNo = ?");
                PreparedStatement govStmt = conn.prepareStatement("SELECT * FROM governmentrelationship WHERE CustomerNo = ?")) {

            // Fetch personal information
            custStmt.setString(1, customerNo);
            try (ResultSet rs = custStmt.executeQuery()) {
                if (rs.next()) {
                    customerSection.setCustomerNo(rs.getString("CustomerNo"));
                    customerSection.setDateCreatedUpdated(rs.getDate("DateCreatedUpdated"));
                    customerSection.setCustomerName(rs.getString("CustomerName"));
                    customerSection.setDateOfBirth(rs.getDate("DateOfBirth"));
                    customerSection.setNationality(rs.getString("Nationality"));
                    customerSection.setCitizenship(rs.getString("Citizenship"));
                    customerSection.setPresentAddress(rs.getString("PresentAddress"));
                    customerSection.setPresentZip(rs.getString("PresentZip"));
                    customerSection.setPermanentAddress(rs.getString("PermanentAddress"));
                    customerSection.setPermanentZip(rs.getString("PermanentZip"));
                    customerSection.setMobileNo(rs.getString("MobileNo"));
                }
            }

            // Fetch financial information
            financeStmt.setString(1, customerNo);
            try (ResultSet rs = financeStmt.executeQuery()) {
                if (rs.next()) {
                    financeSection.setOccupation(rs.getString("Occupation"));
                    financeSection.setTIN(rs.getString("TIN"));
                    financeSection.setIncomeSource(rs.getString("IncomeSource"));
                    financeSection.setMonthlyIncome(rs.getString("MonthlyIncome"));
                }
            }

            // Fetch employment information
            empStmt.setString(1, customerNo);
            try (ResultSet rs = empStmt.executeQuery()) {
                if (rs.next()) {
                    employmentSection.setEmpName(rs.getString("EmpName"));
                    employmentSection.setEmpDate(rs.getDate("EmpDate"));
                    employmentSection.setPosition(rs.getString("Position"));
                    employmentSection.setAppointmentType(rs.getString("AppointmentType"));
                    employmentSection.setBusinessCode(rs.getString("BusinessCode"));
                }

                if(employmentSection.getEmpName().equals("") && employmentSection.getPosition().equals("")) {
                    employmentSection.notEmployed.setSelected(true);
                    employmentSection.EmpName_txt.setEnabled(false);
                    employmentSection.Position_txt.setEnabled(false);
                    employmentSection.AppointmentType_txt.setEnabled(false);
                    employmentSection.EmpDate.setEnabled(false);
                    employmentSection.BusinessNature_list.setEnabled(false);
                }
            }

            // Fetch government relationship information
            govStmt.setString(1, customerNo);
            // Fetch government relationship information
            try (ResultSet rs = govStmt.executeQuery()) {
                while (rs.next()) {
                    employmentSection.setGovRelationships(rs.getString("GovOfficialName"),
                            rs.getString("GovOfficialRelationship"), 
                            rs.getString("GovOfficialPosition"),
                            rs.getString("GovBranch"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCustomerInfo() {
        if(customerSection.validatePersonalForm() == true && financeSection.validateForm() == true && employmentSection.validateFields() == true)
        {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement custStmt = conn.prepareStatement(
                        "UPDATE customer SET DateCreatedUpdated = ?, CustomerName = ?, DateOfBirth = ?, Nationality = ?, Citizenship = ?, PresentAddress = ?, PresentZip = ?, PermanentAddress = ?, PermanentZip = ?, MobileNo = ? WHERE CustomerNo = ?");
                PreparedStatement financeStmt = conn.prepareStatement(
                        "UPDATE financial SET Occupation = ?, TIN = ?, IncomeSource = ?, MonthlyIncome = ? WHERE CustomerNo = ?");
                PreparedStatement empStmt = conn.prepareStatement(
                        "INSERT INTO employment VALUES (?, ?, ?, ?, ?, ?)");
                PreparedStatement deleteEmpStmt = conn.prepareStatement(
                        "DELETE FROM employment WHERE CustomerNo = ?");
                PreparedStatement deleteGovStmt = conn.prepareStatement(
                        "DELETE FROM governmentrelationship WHERE CustomerNo = ?");
                PreparedStatement govStmtInsert = conn.prepareStatement(
                        "INSERT INTO governmentrelationship (GovOfficialName, GovOfficialRelationship, GovOfficialPosition, GovBranch, CustomerNo) VALUES (?, ?, ?, ?, ?)")) {
        
                // Update personal information
                custStmt.setString(1, customerSection.getDateCreatedUpdated());
                custStmt.setString(2, customerSection.getCustomerName());
                custStmt.setString(3, customerSection.getDateOfBirth());
                custStmt.setString(4, customerSection.getNationality());
                custStmt.setString(5, customerSection.getCitizenship());
                custStmt.setString(6, customerSection.getPresentAddress());
                custStmt.setString(7, customerSection.getPresentZip());
                custStmt.setString(8, customerSection.getPermanentAddress());
                custStmt.setString(9, customerSection.getPermanentZip());
                custStmt.setString(10, customerSection.getMobileNo());
                custStmt.setString(11, customerNo);
                custStmt.executeUpdate();
        
                // Update financial information
                financeStmt.setString(1, financeSection.getOccupation());
                financeStmt.setString(2, financeSection.getTIN());
                financeStmt.setString(3, financeSection.getIncomeSource());
                financeStmt.setString(4, financeSection.getMonthlyIncome());
                financeStmt.setString(5, customerNo);
                financeStmt.executeUpdate();
        
                // Update employment information
                if(employmentSection.notEmployed.isSelected() == false)
                {
                    deleteEmpStmt.setString(1, customerNo);
                    deleteEmpStmt.executeUpdate();

                    empStmt.setString(1, customerNo);
                    empStmt.setString(2, employmentSection.getEmpName());
                    empStmt.setString(3, employmentSection.getEmpDate());
                    empStmt.setString(4, employmentSection.getPosition());
                    empStmt.setString(5, employmentSection.getAppointmentType());
                    empStmt.setString(6, employmentSection.getBusinessCode());
                    empStmt.executeUpdate();
                } else if(employmentSection.notEmployed.isSelected() == true) {
                    deleteEmpStmt.setString(1, customerNo);
                    deleteEmpStmt.executeUpdate();
                }
        
                if(employmentSection.notRelated.isSelected() == false)
                {
                    // Delete existing government relationships
                    deleteGovStmt.setString(1, customerNo);
                    deleteGovStmt.executeUpdate();
                    
                    List<String> names = employmentSection.getGovOfficialNames();
                    List<String> relationships = employmentSection.getGovOfficialRelations();
                    List<String> positions = employmentSection.getGovOfficialPositions();
                    List<String> branches = employmentSection.getGovBranches();

                    for(int i = 0; i < names.size(); i++) 
                    {
                        govStmtInsert.setString(1, names.get(i));
                        govStmtInsert.setString(2, relationships.get(i));
                        govStmtInsert.setString(3, positions.get(i));
                        govStmtInsert.setString(4, branches.get(i));
                        govStmtInsert.setString(5, customerNo);
                        govStmtInsert.executeUpdate();
                    }
                } else if(employmentSection.notRelated.isSelected() == true) {
                    deleteGovStmt.setString(1, customerNo);
                    deleteGovStmt.executeUpdate();
                }
        
                JOptionPane.showMessageDialog(this, "Customer information updated successfully.");
        
                // Open the MainClass frame
                openMainClass(username);
                dispose(); // Close the Update frame
        
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating customer information: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearFields() {
        customerSection.clear();
        financeSection.clear();
        employmentSection.clear();
    }

    private void cancelFields() {
        openMainClass(username); // Open MainClass frame
        dispose(); // Close the Update frame
    }

    private void openMainClass(String username) {
        SwingUtilities.invokeLater(() -> new MainClass(username));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Update("username", "selectedCustomerNo").setVisible(true));
    }
}