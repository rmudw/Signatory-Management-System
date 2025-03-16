import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class FinancialInformation extends JPanel {
    private ButtonGroup grpOccupation;
    private ButtonGroup grpSourcesOfWealth = new ButtonGroup();

    private JCheckBox chkEmployed;
    private JCheckBox chkOFW;
    private JCheckBox chkFarmer;
    private JCheckBox chkUnemployed;
    private JCheckBox chkLawyers;
    private JCheckBox chkSelfEmployed;
    private JCheckBox chkRetired;
    private JCheckBox chkStudent;
    private JCheckBox chkHousewife;
    private JCheckBox chkGovtOfficial;

    private JCheckBox chkSalary;
    private JCheckBox chkRemittance;
    private JCheckBox chkGrant;
    private JCheckBox chkFees;
    private JCheckBox chkInterest;
    private JCheckBox chkDonations;
    private JCheckBox chkRoyalties;
    private JCheckBox chkTaxes;
    private JCheckBox chkBusiness;
    private JCheckBox chkAllowance;
    private JCheckBox chkSaleOfAssets;
    private JCheckBox chkWithDocuments;
    private JCheckBox chkPension;
    private JCheckBox chkProfessionalFees;
    private JCheckBox chkLoans;
    private JCheckBox chkWithoutDocuments;
    private JCheckBox chkRegularRemittance;
    private JCheckBox chkLawyerFees;
    private JCheckBox chkGovtAppropriations;

    int WIDTH = 1200;
    int HEIGHT = 30;
    int sideBorder = 40;
    GridBagConstraints gbc = new GridBagConstraints();

    JTextField incomeText = new JTextField();
    JTextField txtTIN = new JTextField("", 37);
    String occupation, incomeSource;

    //SQL connection and authentication
    String URL = "jdbc:mysql://localhost:3306/final_project";
    String user = "root";
    String pass = "1234";

    public FinancialInformation() {
        this.setLayout(new GridBagLayout());

        // Section label
        JLabel financeLabel = new JLabel("FINANCIAL INFORMATION", SwingConstants.CENTER);
        financeLabel.setFont(new Font("Arial", Font.BOLD, 15));
        financeLabel.setBackground(new Color(34, 178, 76));
        financeLabel.setForeground(Color.WHITE);
        financeLabel.setOpaque(true);
        financeLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(financeLabel, gbc);

        occupationSection(); // Occupation
        TINSection(); // TIN
        sourceOfWealthSection(); // Source of Wealth
        incomeSection(); // Income
    }

    // Occupation section
    private void occupationSection() {
        JPanel occupationPanel = new JPanel(new GridLayout(2, 1)); // Main panel for Occupation
        occupationPanel.setBackground(Color.WHITE);

        JLabel occupationTitle = new JLabel("Occupation");
        occupationPanel.setPreferredSize(new Dimension(WIDTH, 70));
        occupationPanel.setBorder(new EmptyBorder(0, sideBorder, 5, sideBorder));

        JPanel bttnContainer = new JPanel(new GridLayout(2, 5));
        grpOccupation = new ButtonGroup();

        chkEmployed = new JCheckBox("Employed");
        chkOFW = new JCheckBox("OFW/Overseas Filipino");
        chkFarmer = new JCheckBox("Farmer/Fisher");
        chkUnemployed = new JCheckBox("Unemployed");
        chkLawyers = new JCheckBox("Lawyers/Notary/Independent Legal Professional/Accountant");
        chkSelfEmployed = new JCheckBox("Self-employed");
        chkRetired = new JCheckBox("Retired");
        chkStudent = new JCheckBox("Student/Minor");
        chkHousewife = new JCheckBox("Housewife");
        chkGovtOfficial = new JCheckBox("Government Official");

        JCheckBox[] occupationCheckBoxes = {chkEmployed, chkOFW, chkFarmer, chkUnemployed, chkLawyers, chkSelfEmployed, chkRetired, 
            chkStudent, chkHousewife, chkGovtOfficial};

        for (JCheckBox chkOccupation : occupationCheckBoxes) {
            chkOccupation.setBackground(Color.WHITE);
            grpOccupation.add(chkOccupation);
            bttnContainer.add(chkOccupation);
        }

        occupationPanel.add(occupationTitle);
        occupationPanel.add(bttnContainer);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(occupationPanel, gbc);
    }

    // Create TIN section with placeholder text
    private void TINSection() {
        JPanel panelTIN = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTIN.setBackground(Color.WHITE);
        panelTIN.setBorder(new EmptyBorder(0, sideBorder, 5, sideBorder));
        panelTIN.setPreferredSize(new Dimension(WIDTH, 50));

        JLabel titleTIN = new JLabel("Tax Identification Number (TIN) ");
        txtTIN.setFont(new Font("Arial", Font.ITALIC, 20));
        txtTIN.setPreferredSize(new Dimension(320, 35));
        txtTIN.setForeground(Color.GRAY);

        txtTIN.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtTIN.getText().equals("Input TIN number")) {
                    txtTIN.setText("");
                    txtTIN.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtTIN.getText().isEmpty()) {
                    txtTIN.setForeground(Color.GRAY);
                    txtTIN.setText("Input TIN number");
                }
            }
        });

        panelTIN.add(titleTIN);
        panelTIN.add(txtTIN);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(panelTIN, gbc);
    }

    // Primary Source of Wealth section
    private void sourceOfWealthSection() {
        JPanel wealthSourcePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Source of wealth main panel
        wealthSourcePanel.setBorder(new EmptyBorder(0, sideBorder, 5, sideBorder));
        wealthSourcePanel.setPreferredSize(new Dimension(WIDTH, 150));
        wealthSourcePanel.setBackground(Color.WHITE);

        JPanel pnlSourceOfWealth = new JPanel(new GridLayout(5, 4));
        pnlSourceOfWealth.setBackground(Color.WHITE);

        JLabel wealthSourceTitle = new JLabel("Source of Wealth");

        chkSalary = new JCheckBox("Salary/Honoraria");
        chkRemittance = new JCheckBox("Other Remittance");
        chkGrant = new JCheckBox("Grant/Scholarship/Awards/Prizes/Benefits");
        chkFees = new JCheckBox("Fees and Charges");
        chkInterest = new JCheckBox("Interest/Commission");
        chkDonations = new JCheckBox("Donations/Inheritance");
        chkRoyalties = new JCheckBox("Royalties/Commission");
        chkTaxes = new JCheckBox("Taxes and Licenses");
        chkBusiness = new JCheckBox("Business");
        chkAllowance = new JCheckBox("Allowance");
        chkSaleOfAssets = new JCheckBox("Sale of Assets");
        chkWithDocuments = new JCheckBox("Others - with documents");
        chkPension = new JCheckBox("Pension");
        chkProfessionalFees = new JCheckBox("Professional Fees - Others");
        chkLoans = new JCheckBox("Loans");
        chkWithoutDocuments = new JCheckBox("Others - without documents");
        chkRegularRemittance = new JCheckBox("Regular Remittance");
        chkLawyerFees = new JCheckBox("Professional Fees - Lawyers/Notary/Independent");
        chkGovtAppropriations = new JCheckBox("Government Appropriations");

        JCheckBox[] wealthSourceCheckBoxes = {chkSalary, chkRemittance, chkGrant, chkFees, chkInterest, chkDonations, chkRoyalties, 
            chkTaxes, chkBusiness, chkAllowance, chkSaleOfAssets, chkWithDocuments, chkPension, chkProfessionalFees, chkLoans, 
            chkWithoutDocuments, chkRegularRemittance, chkLawyerFees, chkGovtAppropriations};

        for (JCheckBox chkSourceOfWealth : wealthSourceCheckBoxes) {
            chkSourceOfWealth.setBackground(Color.WHITE);
            grpSourcesOfWealth.add(chkSourceOfWealth);
            pnlSourceOfWealth.add(chkSourceOfWealth);
        }

        wealthSourcePanel.add(wealthSourceTitle);
        wealthSourcePanel.add(pnlSourceOfWealth);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(wealthSourcePanel, gbc);
    }

    // Income section
    private void incomeSection() {
        JPanel incomePanel = new JPanel(new GridBagLayout()); //Main panel
        incomePanel.setBackground(Color.WHITE);
        incomePanel.setBorder(new EmptyBorder(0, sideBorder, 5, sideBorder));

        JLabel incomeTitle = new JLabel("Monthly Gross Income/Pension/Allowance");
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        incomePanel.add(incomeTitle, gbc);

        incomeText.setPreferredSize(new Dimension(500, HEIGHT));
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        incomePanel.add(incomeText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 5, 0);
        this.add(incomePanel, gbc);
    }

    public void save(String custNo)
    {
        if(txtTIN.getText().equals("Input TIN number")) {
            txtTIN.setText("");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, user, pass);
            Statement db = con.createStatement();

            incomeSource = getIncomeSource();
            occupation = getOccupation();

            //SQL statement for adding record to FINANCIAL
            String query = "INSERT INTO final_project.financial VALUES ('" + custNo + "', '" + occupation + "', '" + txtTIN.getText() + 
                "', '" + incomeSource + "', '" + incomeText.getText() + "')"; 
            
            db.executeUpdate(query);
        } catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public String getOccupation() {
        String occupation = "";
    
        // Iterate through all checkboxes in the grpOccupation ButtonGroup
        for (Enumeration<AbstractButton> buttons = grpOccupation.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            
            // Check if the checkbox is selected
            if (button.isSelected()) {
                occupation = button.getText();
                break; // No need to iterate further once occupation is found
            }
        }
        
        return occupation;
    }
    
    public void setOccupation(String occupation) {
        // Iterate through all checkboxes in the grpOccupation ButtonGroup
        for (Enumeration<AbstractButton> buttons = grpOccupation.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            
            // Check if the checkbox text matches the provided occupation
            if (button.getText().equals(occupation)) {
                button.setSelected(true); // Select the checkbox
                break; // No need to iterate further once checkbox is selected
            }
        }
    }
    

    public String getTIN() {
        if(txtTIN.getText().equals("Input TIN number")) {
            txtTIN.setText("");
        }
        return txtTIN.getText();
    }

    public void setTIN(String tin) {
        txtTIN.setText(tin);
    }

    public String getIncomeSource() {
        StringBuilder incomeSources = new StringBuilder();
        
        // Iterate through all checkboxes in the grpSourcesOfWealth ButtonGroup
        for (Enumeration<AbstractButton> buttons = grpSourcesOfWealth.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            
            // Check if the checkbox is selected
            if (button.isSelected()) {
                // Append the text of the selected checkbox to the StringBuilder
                incomeSources.append(button.getText()).append(", "); // Modify formatting as needed
            }
        }
        
        // Remove trailing comma and space if there are selected sources
        if (incomeSources.length() > 0) {
            incomeSources.setLength(incomeSources.length() - 2);
        }
        
        return incomeSources.toString();
    }
    
    public void setIncomeSource(String incomeSource) {
        // Split the income sources string into individual sources
        String[] sources = incomeSource.split(", ");
        
        // Iterate through sources and set corresponding checkboxes as selected
        for (String source : sources) {
            // Iterate through all checkboxes in the grpSourcesOfWealth ButtonGroup
            for (Enumeration<AbstractButton> buttons = grpSourcesOfWealth.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                
                // Check if the checkbox text matches the current source
                if (button.getText().equals(source)) {
                    button.setSelected(true); // Select the checkbox
                    break; // Move to the next source
                }
            }
        }
    }
    
    public String getMonthlyIncome() {
        return incomeText.getText();
    }

    public void setMonthlyIncome(String monthlyIncome) {
        incomeText.setText(monthlyIncome);
    }

    public void clear()
    {
        txtTIN.setText("");
        incomeText.setText("");
        grpOccupation.clearSelection();
        grpSourcesOfWealth.clearSelection();
    }

    public boolean validateForm() {
        boolean isValid = true;

        if (grpOccupation.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please select your occupation.", "Error", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        if (grpSourcesOfWealth.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Please select your primary source of wealth.", "Error", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        if (incomeText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter your monthly income.", "Error", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        } else {

            try {
                Float.parseFloat(incomeText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid monthly income format. Please enter a numerical value.", "Error", JOptionPane.ERROR_MESSAGE);
                isValid = false;
            }
        }

        return isValid;
    }
}