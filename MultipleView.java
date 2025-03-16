import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class MultipleView extends JPanel {
    private static final String URL = "jdbc:mysql://localhost:3306/final_project";
    private static final String USER = "root";
    private static final String PASS = "1234";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 200;

    private GridBagConstraints gbc = new GridBagConstraints();
    
    private JTable customerTable = new JTable();
    private DefaultTableModel custModel = (DefaultTableModel) customerTable.getModel();

    private JTable financeTable = new JTable();
    private DefaultTableModel financeModel = (DefaultTableModel) financeTable.getModel();

    private JTable empTable = new JTable();
    private DefaultTableModel empModel = (DefaultTableModel) empTable.getModel();

    private JTable govTable = new JTable();
    private DefaultTableModel govModel = (DefaultTableModel) govTable.getModel();

    public MultipleView() {
        // Initialize panels
        JPanel custPanel = createTablePanel(customerTable, custModel, "CUSTOMER INFORMATION");
        JPanel financePanel = createTablePanel(financeTable, financeModel, "FINANCIAL INFORMATION");
        JPanel empPanel = createTablePanel(empTable, empModel, "EMPLOYMENT INFORMATION");
        JPanel govPanel = createTablePanel(govTable, govModel, "RELATIONSHIP TO GOVERNMENT OFFICIALS");

        this.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 0, 0);
        this.add(custPanel, gbc);

        gbc.gridy = 1;
        this.add(financePanel, gbc);

        gbc.gridy = 2;
        this.add(empPanel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 40, 0);
        this.add(govPanel, gbc);

        refresh();
    }

    private JPanel createTablePanel(JTable table, DefaultTableModel model, String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(WIDTH + 5, HEIGHT + 50));
        panel.setBackground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFillsViewportHeight(true);
        JLabel tableLabel = new JLabel(label);
        tableLabel.setPreferredSize(new Dimension(200, 35));
        panel.add(tableLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refresh() {
        // Clear existing rows
        custModel.setRowCount(0);
        financeModel.setRowCount(0);
        empModel.setRowCount(0);
        govModel.setRowCount(0);

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Statement db = con.createStatement();

            // CUSTOMER TABLE
            populateTable(db, "SELECT * FROM customer", custModel, customerTable);
            //Set column widths
            customerTable.getColumnModel().getColumn(0).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(1).setPreferredWidth(120);
            customerTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            customerTable.getColumnModel().getColumn(3).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(4).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(5).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(6).setPreferredWidth(230);
            customerTable.getColumnModel().getColumn(7).setPreferredWidth(75);
            customerTable.getColumnModel().getColumn(8).setPreferredWidth(230);
            customerTable.getColumnModel().getColumn(9).setPreferredWidth(85);
            customerTable.getColumnModel().getColumn(10).setPreferredWidth(90);

            // FINANCIAL TABLE
            populateTable(db, "SELECT * FROM financial", financeModel, financeTable);
            //Set column widths
            financeTable.getColumnModel().getColumn(0).setPreferredWidth(240);
            financeTable.getColumnModel().getColumn(1).setPreferredWidth(239);
            financeTable.getColumnModel().getColumn(2).setPreferredWidth(235);
            financeTable.getColumnModel().getColumn(3).setPreferredWidth(235);
            financeTable.getColumnModel().getColumn(4).setPreferredWidth(235);

            // EMPLOYMENT TABLE
            String empQuery = "SELECT CustomerNo, EmpName, EmpDate, Position, AppointmentType, employment.BusinessCode, CodeDescription " +
                    "FROM employment " +
                    "JOIN businessnature ON employment.BusinessCode = businessnature.BusinessCode";
            populateTable(db, empQuery, empModel, empTable);

            //Set column widths
            empTable.getColumnModel().getColumn(0).setPreferredWidth(90);
            empTable.getColumnModel().getColumn(1).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(2).setPreferredWidth(90);
            empTable.getColumnModel().getColumn(3).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(4).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(5).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(6).setPreferredWidth(290);

            // GOVERNMENT RELATIONSHIP TABLE
            populateTable(db, "SELECT * FROM governmentrelationship", govModel, govTable);
            govTable.getColumnModel().getColumn(0).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(1).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(2).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(3).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(4).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(5).setPreferredWidth(200);

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateTable(Statement db, String query, DefaultTableModel model, JTable table) throws SQLException {
        ResultSet rs = db.executeQuery(query);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columns = rsMetaData.getColumnCount();
        String[] columnName = new String[columns];
        for (int i = 0; i < columns; i++) {
            columnName[i] = rsMetaData.getColumnName(i + 1);
        }
        model.setColumnIdentifiers(columnName);

        while (rs.next()) {
            String[] row = new String[columns];
            for (int i = 0; i < columns; i++) {
                row[i] = rs.getString(i + 1);
            }
            model.addRow(row);
        }
    }

    public void deleteRecord() {
        Set<String> toDelete = new HashSet<>();

        for (int row : customerTable.getSelectedRows()) {
            toDelete.add(customerTable.getValueAt(row, 0).toString());
        }
        for (int row : financeTable.getSelectedRows()) {
            toDelete.add(financeTable.getValueAt(row, 0).toString());
        }
        for (int row : empTable.getSelectedRows()) {
            toDelete.add(empTable.getValueAt(row, 0).toString());
        }
        for (int row : govTable.getSelectedRows()) {
            toDelete.add(govTable.getValueAt(row, 5).toString());
        }

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String[] queries = {
                "DELETE FROM customer WHERE CustomerNo = ?",
                "DELETE FROM financial WHERE CustomerNo = ?",
                "DELETE FROM employment WHERE CustomerNo = ?",
                "DELETE FROM governmentrelationship WHERE CustomerNo = ?"
            };

            for (String custNo : toDelete) {
                for (String query : queries) {
                    try (PreparedStatement stmt = con.prepareStatement(query)) {
                        stmt.setString(1, custNo);
                        stmt.executeUpdate();
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editRecord(String username) {
        Set<String> selectedCustomerNos = new HashSet<>();

        int custSelectedRow = customerTable.getSelectedRow();
        int financeSelectedRow = financeTable.getSelectedRow();
        int empSelectedRow = empTable.getSelectedRow();
        int govSelectedRow = govTable.getSelectedRow();

        if (custSelectedRow != -1) {
            selectedCustomerNos.add((String) custModel.getValueAt(custSelectedRow, 0));
        }
        if (financeSelectedRow != -1) {
            selectedCustomerNos.add((String) financeModel.getValueAt(financeSelectedRow, 0));
        }
        if (empSelectedRow != -1) {
            selectedCustomerNos.add((String) empModel.getValueAt(empSelectedRow, 0));
        }
        if (govSelectedRow != -1) {
            selectedCustomerNos.add((String) govModel.getValueAt(govSelectedRow, 5));
        }

        if (selectedCustomerNos.size() != 1) {
            JOptionPane.showMessageDialog(this, "Please select exactly one record to edit.");
            return;
        }

        
        String selectedCustomerNo = selectedCustomerNos.iterator().next();
        Window window = SwingUtilities.windowForComponent(this);
        window.dispose();
        openUpdate(selectedCustomerNo, username);
    }

    public void searchRecord(String input) {
        String query;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // CUSTOMER
            query = "SELECT * FROM customer WHERE customer.CustomerNo LIKE ? OR customer.CustomerName LIKE ?;";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, "%" + input + "%");
            stmt.setString(2, "%" + input + "%");

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columns = rsMetaData.getColumnCount();
            String[] columnName = new String[columns];
            for (int i = 0; i < columns; i++) {
                columnName[i] = rsMetaData.getColumnName(i + 1);
            }
            custModel.setRowCount(0);
            custModel.setColumnIdentifiers(columnName);

            HashSet<String> search = new HashSet<>();

            while (rs.next()) {
                String[] row = new String[columns];
                for (int i = 0; i < columns; i++) {
                    row[i] = rs.getString(i + 1);
                }
                search.add(row[0]);
                custModel.addRow(row);
            }       
            
            customerTable.getColumnModel().getColumn(1).setPreferredWidth(120);
            customerTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            customerTable.getColumnModel().getColumn(3).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(4).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(5).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(6).setPreferredWidth(230);
            customerTable.getColumnModel().getColumn(7).setPreferredWidth(75);
            customerTable.getColumnModel().getColumn(8).setPreferredWidth(230);
            customerTable.getColumnModel().getColumn(9).setPreferredWidth(85);
            customerTable.getColumnModel().getColumn(10).setPreferredWidth(90);
            customerTable.getColumnModel().getColumn(0).setPreferredWidth(90);
            
            // FINANCIAL TABLE
            query = "SELECT * FROM financial WHERE CustomerNo LIKE ?;";
            searchResult(query, financeModel, financeTable, search);

            financeTable.getColumnModel().getColumn(0).setPreferredWidth(240);
            financeTable.getColumnModel().getColumn(1).setPreferredWidth(239);
            financeTable.getColumnModel().getColumn(2).setPreferredWidth(235);
            financeTable.getColumnModel().getColumn(3).setPreferredWidth(235);
            financeTable.getColumnModel().getColumn(4).setPreferredWidth(235);

            // EMPLOYMENT TABLE
            query = "SELECT CustomerNo, EmpName, EmpDate, Position, AppointmentType, employment.BusinessCode, CodeDescription " +
                    "FROM employment, businessnature WHERE CustomerNo LIKE ? AND employment.BusinessCode = businessnature.BusinessCode;";
            searchResult(query, empModel, empTable, search);

            empTable.getColumnModel().getColumn(0).setPreferredWidth(90);
            empTable.getColumnModel().getColumn(1).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(2).setPreferredWidth(90);
            empTable.getColumnModel().getColumn(3).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(4).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(5).setPreferredWidth(180);
            empTable.getColumnModel().getColumn(6).setPreferredWidth(290);

            // GOVERNMENT RELATIONSHIP TABLE
            query = "SELECT * FROM governmentrelationship WHERE CustomerNo LIKE ?;";
            searchResult(query, govModel, govTable, search);
 
            govTable.getColumnModel().getColumn(0).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(1).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(2).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(3).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(4).setPreferredWidth(198);
            govTable.getColumnModel().getColumn(5).setPreferredWidth(200);

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchResult(String query, DefaultTableModel model, JTable table, HashSet<String> search) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            model.setRowCount(0);
            PreparedStatement stmt = con.prepareStatement(query);
            for(String custNo : search) 
            {
                stmt.setString(1, "%" + custNo + "%");
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columns = rsMetaData.getColumnCount();
                String[] columnName = new String[columns];
                for (int j = 0; j < columns; j++) {
                    columnName[j] = rsMetaData.getColumnName(j + 1);
                }
                model.setColumnIdentifiers(columnName);

                while (rs.next()) {
                    String[] row = new String[columns];
                    for (int j = 0; j < columns; j++) {
                        row[j] = rs.getString(j + 1);
                    }
                    model.addRow(row);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUpdate(String selectedCustomerNo, String username) {
        SwingUtilities.invokeLater(() -> new Update(selectedCustomerNo, username).setVisible(true));
    }
}
