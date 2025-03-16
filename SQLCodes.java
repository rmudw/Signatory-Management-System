import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SQLCodes extends JPanel
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/final_project";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";
    
    int WIDTH = 1200;
    int HEIGHT = 300;
    int i = 0; //tracks components to be displayed
    
    JTextArea problemTextArea = new JTextArea();
    JTextArea sqlCodeText = new JTextArea();
    JLabel difficulty =  new JLabel();

    JTable table = new JTable();

    public SQLCodes()
    {   
        this.setBackground(new Color(34, 178, 76));
        changeProblemDisplayed(); //Display for initial start up

        // Problem statement
        JPanel problemPanel = new JPanel(new GridBagLayout());
        problemPanel.setBackground(Color.WHITE);
        problemTextArea.setFont(new Font("Arial", Font.BOLD, 18));
        problemTextArea.setEditable(false);
        problemTextArea.setWrapStyleWord(true);
        problemTextArea.setLineWrap(true);
        problemTextArea.setPreferredSize(new Dimension(400, 150));
        problemPanel.add(problemTextArea);

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent object) {
                i--;
                if(i == -1) {
                    i = 9; }
                changeProblemDisplayed();
            } 
        });

        JButton next = new JButton("Next");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent object) {
                i++;
                if(i == 10) {
                    i = 0; }
                changeProblemDisplayed();
            } 
        });
        
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel problemLabel = new JLabel("PROBLEM STATEMENT", SwingConstants.CENTER);
        problemLabel.setPreferredSize(new Dimension(250, 35));
        problemLabel.setFont(new Font("Arial", Font.BOLD, 20));
        problemLabel.setForeground(Color.WHITE);
        problemLabel.setBackground(new Color(34, 178, 76));
        problemLabel.setOpaque(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        problemPanel.add(problemLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 50, 0, 0);
        problemPanel.add(problemTextArea, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 50, 15, 0);
        problemPanel.add(back, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 15, 72);
        problemPanel.add(next, gbc);

        //SQL code section
        JPanel sqlPanel = new JPanel(new GridBagLayout());
        JLabel codeLabel = new JLabel("SQL CODE", SwingConstants.CENTER);
        codeLabel.setPreferredSize(new Dimension(125, 35));
        codeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        codeLabel.setForeground(Color.WHITE);
        codeLabel.setBackground(new Color(34, 178, 76));
        codeLabel.setOpaque(true);
        
        sqlCodeText.setFont(new Font("Arial", Font.BOLD, 18));
        sqlCodeText.setEditable(false);
        sqlPanel.setBackground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 440);
        sqlPanel.add(codeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        sqlPanel.add(sqlCodeText, gbc);

        //Display result
        JPanel outputPanel = new JPanel(new GridBagLayout());
        outputPanel.setMaximumSize(new Dimension(WIDTH + 5, HEIGHT + 50));
        outputPanel.setBackground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false); // Disable column reordering
        table.setFillsViewportHeight(true);

        JLabel outputLabel = new JLabel("OUTPUT", SwingConstants.CENTER);
        outputLabel.setPreferredSize(new Dimension(105, 35));
        outputLabel.setFont(new Font("Arial", Font.BOLD, 20));
        outputLabel.setForeground(Color.WHITE);
        outputLabel.setBackground(new Color(34, 178, 76));
        outputLabel.setOpaque(true);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        tableScroll.setViewportView(table);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 10, 0);
        outputPanel.add(outputLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        outputPanel.add(tableScroll, gbc);        
                    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(1365, 773));
        mainPanel.setBackground(Color.WHITE);

        difficulty.setFont(new Font("Arial", Font.BOLD, 30));
        difficulty.setForeground(new Color(34, 178, 76));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 20, 380);
        mainPanel.add(difficulty, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 100);
        mainPanel.add(problemPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        mainPanel.add(sqlPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(outputPanel, gbc);
        gbc.gridwidth = 0;

        JScrollPane sqlScroll = new JScrollPane(mainPanel);
        
        this.add(sqlScroll);
    }

    private void populateTable(String command)
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement query = connection.createStatement();

            ResultSet rs = query.executeQuery(command);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Get column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

             // Create table model
             DefaultTableModel model = new DefaultTableModel(columnNames, 0);
             model.setRowCount(0);
             table.setModel(model);

            // Get data
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
            model.addRow(rowData);
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void changeProblemDisplayed()
    {
        //Problem Statement
        if(i == 0) {
            problemTextArea.setText("Retrieve all customer who live in \"Quezon City\". Sort their Birthdate in ascending order.");
        } else if(i == 1) {
            problemTextArea.setText("Display customer Personal Information born on or before 2003. Sort by customer number.");
        } else if(i == 2) {
            problemTextArea.setText("Show all the records in Customer of those who were born on or before 2000. The youngest should be displayed first.");
        } else if(i == 3) {
            problemTextArea.setText("Display the occupation and the average monthly income for each occupation, showing only those with an average monthly income greater than 50000.");
        } else if(i == 4) {
            problemTextArea.setText("Find the total number of customer for each occupation where their total monthly income exceeds 150000.");
        } else if(i == 5) {
            problemTextArea.setText("Display the sum of the monthly gross income for each occupation. Only display those that have a sum of or more than 20000. Show the occupation with the highest sum first.");
        } else if(i == 6) {
            problemTextArea.setText("Show the count for each nature of business code. Only include those that are two or more and sort them by descending order.");
        } else if(i == 7) {
            problemTextArea.setText("Get the total monthly income for each nationality, but only include nationalities that have a total over 100000. Sort the result from highest to lowest.");
        } else if(i == 8) {
            problemTextArea.setText("Display the employment positions, their average monthly income, and the count for each position for employees working 'Full-time'.");
        } else if(i == 9) {
            problemTextArea.setText("Display the average income relating to each nature of business. Show the code for the nature of business along with its description when displaying the results. Sort the average from lowest to highest.");
        } else {
            problemTextArea.setText("Error");
        }

        //SQL Code
        if(i == 0) {
            sqlCodeText.setText("SELECT * \n"+
                            "FROM customer \n"+
                            "WHERE PresentAddress LIKE '%Quezon City%' \n"+
                            "OR PresentAddress LIKE '%Q.C.%' \n"+
                            "ORDER BY YEAR(DateOfBirth);");
        } else if(i == 1) {
            sqlCodeText.setText("SELECT * \n"+
                            "FROM customer \n"+
                            "WHERE YEAR(dateofbirth) <= 2003 \n"+
                            "ORDER BY customerno;");
        } else if(i == 2) {
            sqlCodeText.setText("SELECT * FROM customer\n" + 
                            "WHERE YEAR(DateOfBirth) <= 2000\n" + 
                            "ORDER BY YEAR(DateOfBirth) DESC;"); 
        } else if(i == 3) {
            sqlCodeText.setText("SELECT Occupation, AVG(MonthlyIncome) AS AverageMonthlyIncome\n" +
                            "FROM financial\n" +
                            "GROUP BY Occupation\n" +
                            "HAVING AVG(MonthlyIncome) > 50000;"); 
        } else if(i == 4) {
            sqlCodeText.setText("SELECT Occupation, COUNT(*) AS TotalCustomer, \nSUM(MonthlyIncome) AS TotalIncome\n" +
                            "FROM financial\n" +
                            "GROUP BY Occupation\n" +
                            "HAVING TotalIncome > 150000;");
        } else if(i == 5) {
            sqlCodeText.setText("SELECT Occupation, SUM(MonthlyIncome)\n" +
                            "FROM financial\n" +
                            "GROUP BY Occupation\n" +
                            "HAVING SUM(MonthlyIncome) >= 20000\n" +
                            "ORDER BY SUM(MonthlyIncome) DESC;"); 
        } else if(i == 6) {
            sqlCodeText.setText("SELECT BusinessCode, COUNT(BusinessCode)\n" +
                            "FROM employment\n" +
                            "GROUP BY BusinessCode\n" +
                            "HAVING COUNT(BusinessCode) >= 2\n" +
                            "ORDER BY COUNT(BusinessCode) DESC;");
        } else if(i == 7) {
            sqlCodeText.setText("SELECT Nationality, SUM(MonthlyIncome) AS TotalMonthlyIncome\n" +
                            "FROM customer AS c, financial AS f\n" +
                            "WHERE c.CustomerNo = f.CustomerNo\n" +
                            "GROUP BY Nationality\n" +
                            "HAVING SUM(MonthlyIncome) > 100000\n" +
                            "ORDER BY TotalMonthlyIncome DESC;");
        } else if(i == 8) {
            sqlCodeText.setText("SELECT E.position, AVG(MonthlyIncome) AS average_income,\nCOUNT(*) AS num_positions\n" +
                            "FROM employment as E, financial as F\n" +
                            "WHERE E.customerno = F.customerno\n" +
                            "AND appointmenttype = 'Full-time'\n" +
                            "GROUP BY E.position;");
            
        } else if(i == 9) {
            sqlCodeText.setText("SELECT employment.BusinessCode, CodeDescription, AVG(MonthlyIncome)\n" +
                            "FROM employment, financial, businessnature\n" +
                            "WHERE employment.BusinessCode = businessnature.BusinessCode\nAND financial.CustomerNo = employment.CustomerNo\n" +
                            "GROUP BY employment.BusinessCode\n" +
                            "ORDER by AVG(MonthlyIncome) ASC;\n");
        } else {
            sqlCodeText.setText("Error. This should not have been displayed.");
        }

        //SQL Output
        String commands[] = {"SELECT * FROM customer WHERE PresentAddress LIKE '%Quezon City%' OR PresentAddress LIKE '%Q.C.%' ORDER BY YEAR(DateOfBirth);", 
                            "SELECT * FROM customer WHERE YEAR(DateofBirth) <= 2003 ORDER BY CustomerNo;",
                            "SELECT * FROM customer WHERE YEAR(DateOfBirth) <= 2000 ORDER BY YEAR(DateOfBirth) DESC",
                            "SELECT Occupation, AVG(MonthlyIncome) AS  AverageMonthlyIncome FROM financial GROUP BY occupation HAVING AVG(MonthlyIncome) > 50000;",
                            "SELECT Occupation, COUNT(*) AS TotalCustomer, SUM(MonthlyIncome) AS TotalIncome FROM financial GROUP BY Occupation HAVING TotalIncome > 150000;",
                            "SELECT Occupation, SUM(MonthlyIncome) FROM financial GROUP BY Occupation HAVING SUM(MonthlyIncome) >= 20000 ORDER BY SUM(MonthlyIncome) DESC;",
                            "SELECT BusinessCode, COUNT(BusinessCode) FROM employment GROUP BY BusinessCode HAVING COUNT(BusinessCode) >= 2 ORDER BY COUNT(BusinessCode) DESC;",
                            "SELECT Nationality, SUM(MonthlyIncome) AS TotalMonthlyIncome FROM customer AS c, financial AS f WHERE c.CustomerNo = f.CustomerNo GROUP BY Nationality HAVING SUM(MonthlyIncome) > 100000 ORDER BY TotalMonthlyIncome DESC;",
                            "SELECT E.position, AVG(MonthlyIncome) AS average_income, COUNT(*) AS num_positions FROM employment as E, financial as F WHERE E.customerno = F.customerno AND appointmenttype = 'Full-time' GROUP BY E.position;",
                            "SELECT employment.BusinessCode, CodeDescription, AVG(MonthlyIncome) FROM employment, financial, businessnature WHERE employment.BusinessCode = businessnature.BusinessCode AND financial.CustomerNo = employment.CustomerNo GROUP BY employment.BusinessCode ORDER by AVG(MonthlyIncome) ASC;"
        };
        populateTable(commands[i]);

        if(i <= 2) {
            difficulty.setText("SIMPLE");
        } else if(i > 2 && i < 7) {
            difficulty.setText("MODERATE");
        } else if(i > 6) {
            difficulty.setText("DIFFICULT");
        }
    }
}