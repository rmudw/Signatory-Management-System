import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField.AbstractFormatter;
import com.toedter.calendar.JDateChooser;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



public class PersonalInformation extends JPanel
{   
    //SQL connection and authentication
    String URL = "jdbc:mysql://localhost:3306/final_project";
    String user = "root";
    String pass = "1234";

    int WIDTH = 1200;
    int HEIGHT = 30;
    int sideBorder = 40;

    JFrame frmWindow;
    JPanel customerPanel = new JPanel(new GridBagLayout()); //Main panel
    GridBagConstraints gbc = new GridBagConstraints();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    JTextField custNoText = new JTextField(25);
    JTextField dateUpdateText = new JTextField(25);
    JTextField CustomerNameField = new JTextField();
    JTextField DateOfBirthField = new JTextField();
    JTextField NationalityField = new JTextField();
    JTextField CitizenshipField = new JTextField();
    JTextField PresentAddressField = new JTextField();
    JTextField PresentZipField = new JTextField();
    JTextField PermanentAddressField = new JTextField();
    JTextField PermanentZipField = new JTextField();
    JTextField MobileNoField = new JTextField();
    JDateChooser dateOfBirth = new JDateChooser();
    JDateChooser dateCreatedUpdated = new JDateChooser();

    public PersonalInformation()
    {   
        this.setLayout(new GridBagLayout());
        customerPanel.setBackground(Color.WHITE);
        createHeader();

        // Create section title
        JLabel titleLabel = new JLabel("PERSONAL INFORMATION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(34, 178, 76)); // Set background color
        titleLabel.setForeground(Color.WHITE); // Set text color
        titleLabel.setPreferredSize(new Dimension(WIDTH, 26));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0 , 7, 0);
        customerPanel.add(titleLabel, gbc);

        //Text field size adjustments
        CustomerNameField.setPreferredSize(new Dimension(WIDTH - sideBorder * 2, HEIGHT));
        DateOfBirthField.setPreferredSize(new Dimension(365, HEIGHT));
        NationalityField.setPreferredSize(new Dimension(380, HEIGHT));
        CitizenshipField.setPreferredSize(new Dimension(375, HEIGHT));
        PresentAddressField.setPreferredSize(new Dimension(820, HEIGHT));
        PresentZipField.setPreferredSize(new Dimension(300, HEIGHT));
        PermanentAddressField.setPreferredSize(new Dimension(820, HEIGHT));
        PermanentZipField.setPreferredSize(new Dimension(300, HEIGHT));
        MobileNoField.setPreferredSize(new Dimension(WIDTH - sideBorder * 2, HEIGHT));


        JLabel CustomerNameLabel = new JLabel("Full Name (First Name, Middle Name, Last Name)");
        JLabel DateOfBirthLabel = new JLabel("Date of Birth (yyyy/mm/dd)");
        JLabel NationalityLabel = new JLabel("Nationality");
        JLabel CitizenshipLabel = new JLabel("Citizenship");
        JLabel PresentAddressLabel = new JLabel("Present Address (No./Street, Subd.,Brgy./Dist./Municipality/City,Province)");
        JLabel PresentZipLabel = new JLabel("ZIP Code");
        JLabel PermanentAddressLabel = new JLabel("Permanent Address (No./Street, Subd.,Brgy./Dist./Municipality/City,Province)");
        JLabel PermanentZipLabel = new JLabel("ZIP Code");
        JLabel MobileNoLabel = new JLabel("Primary Mobile Number");

        //ADDING ALL COMPONENTS TO THE PANEL
        //Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, sideBorder, 5, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        customerPanel.add(CustomerNameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, sideBorder, 5, 0);
        gbc.gridwidth = 3;
        customerPanel.add(CustomerNameField, gbc);

        //Date of birth, nationality, and citizenship
        gbc.gridx = 0;
        gbc.gridy = 4;
        customerPanel.add(DateOfBirthLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 5, 325);
        customerPanel.add(NationalityLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, sideBorder, 5, 350);
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        customerPanel.add(CitizenshipLabel, gbc);

        //Date picker
        dateOfBirth.setDateFormatString("yyyy-MM-dd");
        dateOfBirth.setPreferredSize(new Dimension(364, HEIGHT));

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        customerPanel.add(dateOfBirth, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        customerPanel.add(NationalityField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 5, sideBorder);
        gbc.gridwidth = 3;
        customerPanel.add(CitizenshipField, gbc);

        //Customer address
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, sideBorder, 5, 0);
        customerPanel.add(PresentAddressLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 5, 289);
        customerPanel.add(PresentZipLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, sideBorder, 5, 0);
        customerPanel.add(PresentAddressField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 5, sideBorder);
        customerPanel.add(PresentZipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, sideBorder, 5, 0);
        customerPanel.add(PermanentAddressLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 5, 289);
        customerPanel.add(PermanentZipLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, sideBorder, 5, 0);
        customerPanel.add(PermanentAddressField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 5, sideBorder);
        customerPanel.add(PermanentZipField, gbc);

        //Contact number
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, sideBorder, 5, sideBorder);
        customerPanel.add(MobileNoLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, sideBorder, 10, sideBorder);
        customerPanel.add(MobileNoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        this.add(customerPanel, gbc);
    }

    private void createHeader()
    {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel formHeader = new JLabel();
        formHeader.setIcon(new ImageIcon(new ImageIcon("header.png").getImage().getScaledInstance(1200, 192, Image.SCALE_SMOOTH)));
    
        JLabel custNoLabel = new JLabel("CUSTOMER NO.:");
        JLabel dateUpdateLabel = new JLabel("Date CREATED/UPDATED (yyyy/mm/dd):");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        //headerPanel.add(formHeader, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 0 );
        headerPanel.add(custNoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 3, 5, 0 );
        headerPanel.add(custNoText, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 295, 5, 0 );
        headerPanel.add(dateUpdateLabel, gbc);

        dateCreatedUpdated.setDateFormatString("yyyy-MM-dd");
        dateCreatedUpdated.setPreferredSize(new Dimension(230, 20));

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 3, 5, 0 );
        headerPanel.add(dateCreatedUpdated, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        this.add(headerPanel, gbc);
    }

    public void save()
    {
        String dateBirth = dateFormat.format(dateOfBirth.getDate());
        String dateCreateUpdate = dateFormat.format(dateCreatedUpdated.getDate());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, user, pass);
            Statement db = con.createStatement();

            //SQL statement for adding record to CUSTOMER
            String query = "INSERT INTO final_project.customer VALUES ('" + custNoText.getText() + "', '" + dateCreateUpdate + 
                "', '" + CustomerNameField.getText() + "', '" + dateBirth + "', '" + NationalityField.getText() + 
                "', '" + CitizenshipField.getText() + "', '" + PresentAddressField.getText() + "', '" + PresentZipField.getText() + 
                "', '" + PermanentAddressField.getText() + "', '" + PermanentZipField.getText() + "', '" + MobileNoField.getText() + "')"; 
            
                db.executeUpdate(query);
        } catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void clear()
    {        
        custNoText.setText("");
        dateCreatedUpdated.setDate(null);
        CustomerNameField.setText("");
        dateOfBirth.setDate(null);
        NationalityField.setText("");
        CitizenshipField.setText("");
        PresentAddressField.setText("");
        PresentZipField.setText("");
        PermanentAddressField.setText("");
        PermanentZipField.setText("");
        MobileNoField.setText("");
    }

    public boolean validatePersonalForm() {
        boolean isValid = true;
    
        // Validate customer number
        if (custNoText.getText().isEmpty() || custNoText.getText().length() != 9) {         // Char = 9
            custNoText.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            custNoText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate date updated
        if (dateCreatedUpdated.getDate() == null) {
            dateCreatedUpdated.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            dateCreatedUpdated.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate customer name
        if (CustomerNameField.getText().isEmpty() || CustomerNameField.getText().length() > 50) {    // Varchar = 50 
            CustomerNameField.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            CustomerNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate date of birth
        if (dateOfBirth.getDate() == null) {
            dateOfBirth.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            dateOfBirth.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate nationality
        if (NationalityField.getText().isEmpty() || NationalityField.getText().length() > 20) {      //Varchar = 20 
            NationalityField.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            NationalityField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate citizenship
        if (CitizenshipField.getText().isEmpty() || CitizenshipField.getText().length() > 20) {    // Varchar = 20 
            CitizenshipField.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            CitizenshipField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate present address
        if (PresentAddressField.getText().isEmpty() || PresentAddressField.getText().length() > 100) { // Varchar = 100 
            PresentAddressField.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            PresentAddressField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate present ZIP code
        if (PresentZipField.getText().isEmpty() || PresentZipField.getText().length() > 10) {          // Varchar = 10
            PresentZipField.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            PresentZipField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate permanent address
        if (PermanentAddressField.getText().isEmpty() || PermanentAddressField.getText().length() > 100) {   // Varchar = 100
            PermanentAddressField.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            PermanentAddressField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate permanent ZIP code
        if (PermanentZipField.getText().isEmpty() || PermanentZipField.getText().length() > 10) {        // Varchar = 10 
            PermanentZipField.setBorder(BorderFactory.createLineBorder(Color.RED)); 
            isValid = false;
        } else {
            PermanentZipField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        // Validate mobile number
        if (MobileNoField.getText().isEmpty() || MobileNoField.getText().length() > 15) {           // Varchar = 15
            MobileNoField.setBorder(BorderFactory.createLineBorder(Color.RED));
            isValid = false;
        } else {
            MobileNoField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    
        if (!isValid) {
            JOptionPane.showMessageDialog(this, "All fields are required and must meet size constraints", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    
        return isValid;
    }

    public String getCustomerNo() {
        return custNoText.getText();
    }

    public void setCustomerNo(String customerNo) {
        custNoText.setText(customerNo);
    }

    public String getDateCreatedUpdated() {
        return dateFormat.format(dateCreatedUpdated.getDate());
    }

    public void setDateCreatedUpdated(Date date) {
        dateCreatedUpdated.setDate(date);
    }

    public String getCustomerName() {
        return CustomerNameField.getText();
    }

    public void setCustomerName(String customerName) {
        CustomerNameField.setText(customerName);
    }

    public String getDateOfBirth() {
        return dateFormat.format(dateOfBirth.getDate());
    }

    public void setDateOfBirth(Date dateBirth) {
        dateOfBirth.setDate(dateBirth);
    }

    public String getNationality() {
        return NationalityField.getText();
    }

    public void setNationality(String nationality) {
        NationalityField.setText(nationality);
    }

    public String getCitizenship() {
        return CitizenshipField.getText();
    }

    public void setCitizenship(String citizenship) {
        CitizenshipField.setText(citizenship);
    }

    public String getPresentAddress() {
        return PresentAddressField.getText();
    }

    public void setPresentAddress(String presentAddress) {
        PresentAddressField.setText(presentAddress);
    }

    public String getPresentZip() {
        return PresentZipField.getText();
    }

    public void setPresentZip(String presentZip) {
        PresentZipField.setText(presentZip);
    }

    public String getPermanentAddress() {
        return PermanentAddressField.getText();
    }

    public void setPermanentAddress(String permanentAddress) {
        PermanentAddressField.setText(permanentAddress);
    }

    public String getPermanentZip() {
        return PermanentZipField.getText();
    }

    public void setPermanentZip(String permanentZip) {
        PermanentZipField.setText(permanentZip);
    }

    public String getMobileNo() {
        return MobileNoField.getText();
    }

    public void setMobileNo(String mobileNo) {
        MobileNoField.setText(mobileNo);
    }

    public String getCustNo() {
        return custNoText.getText();
    }

    public class DateLabelFormatter extends AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
    }

}
}