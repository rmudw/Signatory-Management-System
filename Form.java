import javax.swing.*;
import java.awt.*;

// Displays the form
public class Form extends JPanel
{
    PersonalInformation customerSection = new PersonalInformation();
    FinancialInformation financeSection = new FinancialInformation();
    EmploymentInformation employmentSection = new EmploymentInformation();

    public Form()
    {
        customerSection.setBackground(Color.WHITE);
        financeSection.setBackground(Color.WHITE);
        employmentSection.setBackground(Color.WHITE);

        JPanel placeHolder =  new JPanel(); //Used to fix some background issues
        placeHolder.setLayout(new BoxLayout(placeHolder, BoxLayout.Y_AXIS));
        placeHolder.add(customerSection);
        placeHolder.add(financeSection);
        placeHolder.add(employmentSection);

        //Make the panel scrollable
        JPanel mainPanel = new JPanel();
        mainPanel.add(placeHolder);
        mainPanel.setBackground(Color.WHITE);
        JScrollPane scrollFrame = new JScrollPane(mainPanel);
        scrollFrame.setPreferredSize(new Dimension(1200, 500));
        scrollFrame.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollFrame.getVerticalScrollBar().setUnitIncrement(10); //Scroll speed

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(scrollFrame);
    }

    public void saveData() {
        //Data validation
        if(customerSection.validatePersonalForm() == true && financeSection.validateForm() == true && employmentSection.validateFields() == true) {
            customerSection.save();
            financeSection.save(customerSection.getCustNo());
            employmentSection.save(customerSection.getCustNo());
            clearText();
            JOptionPane.showMessageDialog(null, "Customer record has been saved.");
        } else return;
    }

    public void clearText() {
        customerSection.clear();
        financeSection.clear();
        employmentSection.clear();
    }
}