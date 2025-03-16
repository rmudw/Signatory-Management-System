import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

public class EmploymentInformation extends JPanel
{
    String BusinessType[] = {"", "1700 - Private Household with Employed Persons", "1800 - Extra-territorial Organizations and Bodies", "1204 - Jewelry and Precious Stones Dealer", "1005 - Foreign Exchange Dealer/Money Changer/Remittance Agent", "A0103 - Agriculture, Forestry, and Fishing", "B0509 - Mining and Quarrying", "C1033 - Manufacturing", "D3500 - Electricity, Gas, Steam, and Air-conditioning Supply", "E3639 - Water Supply, Sewerage, Waste Management and Remediation Activities", "F4143 - Construction", "G4547 - Wholesale and Retail Trade, Repair of Motor Vehicles and Motorcycles", "H4953 - Transportation and Storage", "I5556 - Accommodation and Food Service Activities", "J5863 - Information and Communication", "K6466 - Financial and Insurance Activities", "L800 - Real Estate Activities", "M6975 - Professional, Scientific, and Technical Activities", "N7782 - Administrative and Support Service Activities", "O8400 - Public Administrative and Defense; Compulsory Social Security", "P8500 - Education", "Q8688 - Human Health and Social Work Activities", "R9093 - Arts, Entertainment, and Recreation", "S9496 - Other Service Activities (Activities of Membership Organizations)", "T9798 - Activities of Private Households as Employers and Undifferentiated Goods and Services and Producing Activities of Households for own use", "U9900 - Activities of Extraterritorial Organizations, and Bodies", "V0000 - Others", "V0001 - Others - Student/Minor/Retiree/Pensioner", "V0002 - Others - Unemployed/Housewife", "9200 - Gambling and Betting Activities", "9210 - OGOs and OGO Service Providers", "9220 - Casinos"};

    //SQL connection and authentication
    String URL = "jdbc:mysql://localhost:3306/final_project";
    String user = "root";
    String pass = "1234";

    int WIDTH = 1200;
    int HEIGHT = 30;
    int SIDEBORDER = 40;
    int gridY = 0;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel rowPanel = new JPanel(new GridBagLayout());
    JPanel addBttnPanel = new JPanel();

    JTextField EmpName_txt = new JTextField(25);
    JTextField Position_txt = new JTextField(25);
    JTextField AppointmentType_txt = new JTextField(25);
    JDateChooser EmpDate = new JDateChooser();
    JComboBox<String> BusinessNature_list = new JComboBox<>(BusinessType);
    JCheckBox notRelated = new JCheckBox("Not related to any official of the government/international organization");
    JCheckBox notEmployed = new JCheckBox("Not employed");
    JButton addRowBttn = new JButton("Add row");
    JButton deleteRowBttn = new JButton("Delete row");

    //Used to track added rows
    private List<JTextField> nameField = new ArrayList<>();
    private List<JTextField> relationField = new ArrayList<>();
    private List<JTextField> positionField = new ArrayList<>();
    private List<JTextField> branchField = new ArrayList<>(); 
    private List<JButton> deleteButtons = new ArrayList<>();

    public EmploymentInformation()
    {
        this.setLayout(new GridBagLayout());

        //Employment Information label bar
        JLabel empLabel = new JLabel("EMPLOYMENT INFORMATION", SwingConstants.CENTER);
        empLabel.setFont(new Font("Arial", Font.BOLD, 15));
        empLabel.setForeground(Color.WHITE);
        empLabel.setBackground(new Color(34, 178, 76));
        empLabel.setOpaque(true);
        empLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(empLabel, gbc);

        notEmployed.setBackground(Color.WHITE);
        notEmployed.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent check) {
                if(notEmployed.isSelected() == true) {
                    EmpName_txt.setEnabled(false);
                    Position_txt.setEnabled(false);
                    AppointmentType_txt.setEnabled(false);
                    EmpDate.setEnabled(false);
                    BusinessNature_list.setEnabled(false);
                } else{
                    EmpName_txt.setEnabled(true);
                    Position_txt.setEnabled(true);
                    AppointmentType_txt.setEnabled(true);
                    EmpDate.setEnabled(true);
                    BusinessNature_list.setEnabled(true);
                }
            }
        });  

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(15, SIDEBORDER - 1, 0, 0);
        this.add(notEmployed, gbc);

        generalInfo();
        businessNature();
        govRelationship();
    }
    
    private void generalInfo()
    {
        //EmpName, EmpDate, Postion, AppointmentType attributes layout
        JPanel empPanel = new JPanel();
        empPanel.setBackground(Color.WHITE);
        empPanel.setLayout(new GridLayout(4, 2));
        empPanel.setBorder(new EmptyBorder(0, 40, 5, 40));
        empPanel.setPreferredSize(new Dimension(WIDTH, 125));

        //Employer labels
        JLabel EmpName_lbl = new JLabel("Employer's Name:");
        JLabel EmpDate_lbl = new JLabel("Employment Date:");
        JLabel Position_lbl = new JLabel("Position:");
        JLabel AppointmentType_lbl = new JLabel("Type of Appointment:");

        EmpDate.setDateFormatString("yyyy-MM-dd");
        EmpDate.setPreferredSize(new Dimension(230, HEIGHT));

        empPanel.add(EmpName_lbl);
        empPanel.add(EmpDate_lbl);
        empPanel.add(EmpName_txt);
        empPanel.add(EmpDate);

        empPanel.add(Position_lbl);
        empPanel.add(AppointmentType_lbl);
        empPanel.add(Position_txt);
        empPanel.add(AppointmentType_txt);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(empPanel, gbc);        
    }

    private void businessNature()
    {
        //BusinessNature section
        JPanel busPanel = new JPanel();
        busPanel.setBackground(Color.WHITE);
        busPanel.setPreferredSize(new Dimension(WIDTH, 55));
        busPanel.setLayout(new GridLayout(2, 1));
        busPanel.setBorder(new EmptyBorder(0, 40, 5, 40));

        //Business nature selection
        JLabel BusinessNature_lbl = new JLabel("Nature of Business/Economic Activity");
        BusinessNature_list.setBackground(Color.WHITE);
        
        busPanel.add(BusinessNature_lbl);
        busPanel.add(BusinessNature_list);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(busPanel, gbc);
    }

    private void govRelationship()
    {
        //Government official relationship section
        JPanel govPanel = new JPanel();
        govPanel.setLayout(new GridBagLayout());
        govPanel.setBackground(Color.WHITE);

        //Relationship labels
        JLabel GovOfficialName_lbl = new JLabel("Full Name");
        JLabel GovOfficialRelation_lbl = new JLabel("Relationship");
        JLabel GovOfficialPosition_lbl = new JLabel("Position");
        JLabel GovBranch_lbl = new JLabel("Government Branch/Organization Name");
        
        notRelated.setBackground(Color.WHITE);
        notRelated.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent check) {
                if(notRelated.isSelected() == true) {
                    addRowBttn.setEnabled(false);
                } else{
                    addRowBttn.setEnabled(true);
                }
            }
        });  

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(15, SIDEBORDER - 1, 0, 0);
        this.add(notRelated, gbc);
        
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBackground(Color.WHITE);
        
        JLabel relationTitle = new JLabel("Relationship to Official of Government/International Organization");
        relationTitle.setFont(new Font("Arial", Font.BOLD, 15));
        relationTitle.setPreferredSize(new Dimension(WIDTH - SIDEBORDER, HEIGHT));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(3, SIDEBORDER, 5, 0);
        govPanel.add(relationTitle, gbc);

        //Add labels and text fields
        gbc.gridx = 0; //Full name label
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 150, 0, 0);
        govPanel.add(GovOfficialName_lbl, gbc);

        gbc.gridx = 1; //Relationship label
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 220, 0, 0);
        govPanel.add(GovOfficialRelation_lbl, gbc);

        gbc.gridx = 2; //Position label
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 220, 0, 0);
        govPanel.add(GovOfficialPosition_lbl, gbc);

        gbc.gridx = 3; //Government Branch label
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 70);
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        govPanel.add(GovBranch_lbl, gbc);

        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        govPanel.add(rowPanel, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(govPanel, gbc);

        //Add row button
        addRowBttn.addActionListener(this::addRow);
        addRowBttn.setBackground(new Color(64, 170, 91));  // Set button background color to #40AA5B
        addRowBttn.setForeground(Color.WHITE);

        // Add button to addBttnPanel
        addBttnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Ensure proper layout for button
        addBttnPanel.setBackground(Color.WHITE);  // Set panel background color to #40AA5B
        addBttnPanel.add(addRowBttn);  // Add button to panel

        // Positioning and adding addBttnPanel to your container
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(10, SIDEBORDER - 7, 100, 0);
        this.add(addBttnPanel, gbc);
    }

    private void addRow(ActionEvent click)
    {   
        rowPanel.setBackground(Color.WHITE);

        JTextField addGovOfficialName_txt = new JTextField();
        JTextField addGovOfficialRelation_txt = new JTextField();
        JTextField addGovOfficialPosition_txt = new JTextField();
        JTextField addGovBranch_txt = new JTextField();
        JButton deleteButton = new JButton("Delete row");

        addGovOfficialName_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));
        addGovOfficialRelation_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));
        addGovOfficialPosition_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));
        addGovBranch_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));

        //Add new text fields into array list
        nameField.add(addGovOfficialName_txt);
        relationField.add(addGovOfficialRelation_txt);
        positionField.add(addGovOfficialPosition_txt);
        branchField.add(addGovBranch_txt);

        deleteButtons.add(deleteButton);
        deleteButton.setBackground(new Color(64, 170, 91));  // Set button background color to #40AA5B
        deleteButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.insets = new Insets(0, SIDEBORDER, 0, 0);
        rowPanel.add(addGovOfficialName_txt, gbc);

        gbc.gridx = 1;
        gbc.gridy = gridY;
        gbc.insets = new Insets(0, 0, 0, 0);
        rowPanel.add(addGovOfficialRelation_txt, gbc);

        gbc.gridx = 2;
        gbc.gridy = gridY;
        rowPanel.add(addGovOfficialPosition_txt, gbc);

        gbc.gridx = 3;
        gbc.gridy = gridY;
        rowPanel.add(addGovBranch_txt, gbc);

        gbc.gridx = 4;
        gbc.gridy = gridY;
        rowPanel.add(deleteButton, gbc);

        // Attach action listener to delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = deleteButtons.indexOf(deleteButton);
                removeRow(index);
            }
        });

        //Moves the next added row to next Y coordinate
        gridY++; 
        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(rowPanel, gbc);

        //Updates the add button location
        gbc.gridx = 0;
        gbc.gridy = gridY + 1;
        gbc.insets = new Insets(10, SIDEBORDER - 7, 100, 0);
        rowPanel.add(addBttnPanel, gbc);

        rowPanel.revalidate();
        rowPanel.repaint();
    }

    private void removeRow(int index) {
        if (index >= 0 && index < nameField.size()) {
            // Remove components from panel
            rowPanel.remove(nameField.get(index));
            rowPanel.remove(relationField.get(index));
            rowPanel.remove(positionField.get(index));
            rowPanel.remove(branchField.get(index));
            rowPanel.remove(deleteButtons.get(index));

            // Remove components from lists
            nameField.remove(index);
            relationField.remove(index);
            positionField.remove(index);
            branchField.remove(index);
            deleteButtons.remove(index);

            // Decrement gridY
            gridY--;

            // Refresh the panel to update the layout
            rowPanel.revalidate();
            rowPanel.repaint();
        }
    }

    public void save(String custNo)
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, user, pass);
            Statement db = con.createStatement();

            if(notEmployed.isSelected() == false)
            {
                String empDate = dateFormat.format(EmpDate.getDate());
                
                //SQL statement for adding record to EMPLOYMENT
                String query = "INSERT INTO final_project.employment VALUES ('" + custNo + "', '" + EmpName_txt.getText() + "', '" + empDate + 
                    "', '" + Position_txt.getText() + "', '" + AppointmentType_txt.getText() + "', '" + BusinessNature_list.getSelectedItem().toString().substring(0, 5) + "')"; 
                    db.executeUpdate(query);
            }
            
            if(notRelated.isSelected() == false)
            {
                //If there are rows in the array list for govrelation, iterate through the list and add them into the database
                for(int i = 0; i < nameField.size(); i++) {
                    String query = "INSERT INTO final_project.governmentrelationship (GovOfficialName, GovOfficialRelationship, GovOfficialPosition, GovBranch, CustomerNo) VALUES ('" + 
                        nameField.get(i).getText() + "', '" + relationField.get(i).getText()  + "', '" + positionField.get(i).getText() + 
                        "', '" + branchField.get(i).getText() + "', '" + custNo + "')";
                        db.executeUpdate(query);
                }
            }
            rowPanel.removeAll();
            rowPanel.revalidate();
            rowPanel.repaint();

        } catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void clear()
    {
        EmpName_txt.setText("");
        EmpDate.setDate(null);
        Position_txt.setText("");
        AppointmentType_txt.setText("");
        BusinessNature_list.setSelectedItem("");

        //Removes added rows
        rowPanel.removeAll();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(10, SIDEBORDER - 7, 100, 0);
        this.add(addBttnPanel, gbc);
        rowPanel.revalidate();
        rowPanel.repaint();

        //Removes all elements in list
        nameField.clear();
        relationField.clear();
        positionField.clear();
        branchField.clear();
    }

    public boolean validateFields()
    {
        boolean isValid = true;

        if(notEmployed.isSelected() == false)
        {
            // Employer's Name: REQ, VARCHAR 50
            if (EmpName_txt.getText().trim().isEmpty() || EmpName_txt.getText().length() > 50) {
                EmpName_txt.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Employer's Name must be filled.");
                isValid = false;
            } else {
                EmpName_txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            // Employment Date: REQ,  DATE
            if (EmpDate.getDate() == null) {
                EmpDate.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Employment Date must be filled.");
                isValid = false;
            } else {
                EmpDate.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            // Position:REQ, VARCHAR 30
            if (Position_txt.getText().trim().isEmpty() || Position_txt.getText().length() > 30) {
                Position_txt.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Position must be filled.");
                isValid = false;
            } else {
                Position_txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            // Type of Appointment: REQ, VARCHAR 30 
            if (AppointmentType_txt.getText().trim().isEmpty() || AppointmentType_txt.getText().length() > 30) {
                AppointmentType_txt.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Type of Appointment must be filled.");
                isValid = false;
            } else {
                AppointmentType_txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            if (BusinessNature_list.getSelectedIndex() == 0) {
                BusinessNature_list.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Business nature must be selected.");
                isValid = false;
            } else {
                BusinessNature_list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        }

        if(notRelated.isSelected() == false)
        {
            // Each official's full name, relationship, position, and government branch fields validation
            for (int i = 0; i < nameField.size(); i++) {
                JTextField currentNameField = nameField.get(i);
                JTextField currentRelationField = relationField.get(i);
                JTextField currentPositionField = positionField.get(i);
                JTextField currentBranchField = branchField.get(i);

            // Full Name: REQ, VARCHAR 50
            if (currentNameField.getText().trim().isEmpty() || currentNameField.getText().length() > 50) {
                currentNameField.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Government Official's Full Name must be filled.");
                isValid = false;
            } else {
                currentNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            // Relationship: REQ, VARCHAR 20
            if (currentRelationField.getText().trim().isEmpty() || currentRelationField.getText().length() > 20) {
                currentRelationField.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Relationship to Government Official must be filled.");
                isValid = false;
            } else {
                currentRelationField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            // Position: REQ, VARCHAR 50        
            if (currentPositionField.getText().trim().isEmpty() || currentPositionField.getText().length() > 50) {
                currentPositionField.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Government Official's Position must be filled.");
                isValid = false;
            } else {
                currentPositionField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

                // Government Branch: REQ, VARCHAR 100 
            if (currentBranchField.getText().trim().isEmpty() || currentBranchField.getText().length() > 100) {
                currentBranchField.setBorder(BorderFactory.createLineBorder(Color.RED));
                JOptionPane.showMessageDialog(this, "Government Branch/Organization Name must be filled.");
                isValid = false;
            } else {
                currentBranchField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
            }
        }

        return isValid;
    }

    public String getEmpName() {
        return EmpName_txt.getText();
    }

    public void setEmpName(String empName) {
        EmpName_txt.setText(empName);
    }

    public String getEmpDate() {
        return dateFormat.format(EmpDate.getDate());
    }

    public void setEmpDate(Date empDate) {
        EmpDate.setDate(empDate);
    }

    public String getPosition() {
        return Position_txt.getText();
    }

    public void setPosition(String position) {
        Position_txt.setText(position);
    }

    public String getAppointmentType() {
        return AppointmentType_txt.getText();
    }

    public void setAppointmentType(String appointmentType) {
        AppointmentType_txt.setText(appointmentType);
    }

    public String getBusinessCode() {
        // Ensure to handle the case where no item is selected
        if (BusinessNature_list.getSelectedItem() != null) {
            return BusinessNature_list.getSelectedItem().toString().substring(0, 5);
        }
        return "";
    }
    
    public void setBusinessCode(String businessCode) {
        if (businessCode != null && !businessCode.isEmpty()) {
            // Iterate through items in the JComboBox to find and set the selected item
            for (int i = 0; i < BusinessNature_list.getItemCount(); i++) {
                String item = BusinessNature_list.getItemAt(i);
                if (item != null && item.startsWith(businessCode)) {
                    BusinessNature_list.setSelectedIndex(i);
                    return;
                }
            }
        } else {
            // If businessCode is null or empty, set to the first item (assuming it's an empty selection)
            BusinessNature_list.setSelectedIndex(0);
        }
    }

    public void setGovRelationships(String govName, String govRelation, String govPosition, String govBranch)
    {   
        rowPanel.setBackground(Color.WHITE);

        JTextField addGovOfficialName_txt = new JTextField(govName);
        JTextField addGovOfficialRelation_txt = new JTextField(govRelation);
        JTextField addGovOfficialPosition_txt = new JTextField(govPosition);
        JTextField addGovBranch_txt = new JTextField(govBranch);
        JButton deleteButton = new JButton("Delete row");

        addGovOfficialName_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));
        addGovOfficialRelation_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));
        addGovOfficialPosition_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));
        addGovBranch_txt.setPreferredSize(new Dimension((WIDTH - SIDEBORDER * 2) / 4, HEIGHT));

        //Add new text fields into array list
        nameField.add(addGovOfficialName_txt);
        relationField.add(addGovOfficialRelation_txt);
        positionField.add(addGovOfficialPosition_txt);
        branchField.add(addGovBranch_txt);

        deleteButtons.add(deleteButton);
        deleteButton.setBackground(new Color(64, 170, 91));  // Set button background color to #40AA5B
        deleteButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.insets = new Insets(0, SIDEBORDER, 0, 0);
        rowPanel.add(addGovOfficialName_txt, gbc);

        gbc.gridx = 1;
        gbc.gridy = gridY;
        gbc.insets = new Insets(0, 0, 0, 0);
        rowPanel.add(addGovOfficialRelation_txt, gbc);

        gbc.gridx = 2;
        gbc.gridy = gridY;
        rowPanel.add(addGovOfficialPosition_txt, gbc);

        gbc.gridx = 3;
        gbc.gridy = gridY;
        rowPanel.add(addGovBranch_txt, gbc);

        gbc.gridx = 4;
        gbc.gridy = gridY;
        rowPanel.add(deleteButton, gbc);

        // Attach action listener to delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = deleteButtons.indexOf(deleteButton);
                removeRow(index);
            }
        });

        //Moves the next added row to next Y coordinate
        gridY++; 
        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(rowPanel, gbc);

        //Updates the add button location
        gbc.gridx = 0;
        gbc.gridy = gridY + 1;
        gbc.insets = new Insets(10, SIDEBORDER - 7, 100, 0);
        rowPanel.add(addBttnPanel, gbc);

        rowPanel.revalidate();
        rowPanel.repaint();
    }

    public List<String> getGovOfficialNames() {
        List<String> names = new ArrayList<>();
        for (JTextField textField : nameField) {
            names.add(textField.getText());
        }
        return names;
    }
    
    public List<String> getGovOfficialRelations() {
        List<String> relations = new ArrayList<>();
        for (JTextField textField : relationField) {
            relations.add(textField.getText());
        }
        return relations;
    }
    
    public List<String> getGovOfficialPositions() {
        List<String> positions = new ArrayList<>();
        for (JTextField textField : positionField) {
            positions.add(textField.getText());
        }
        return positions;
    }
    
    public List<String> getGovBranches() {
        List<String> branches = new ArrayList<>();
        for (JTextField textField : branchField) {
            branches.add(textField.getText());
        }
        return branches;
    }
    
}