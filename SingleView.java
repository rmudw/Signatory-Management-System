import java.awt.BorderLayout;
import java.awt.Dimension;
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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class SingleView extends JPanel {
    JTable dbTable = new JTable();
    DefaultTableModel dbModel = (DefaultTableModel) dbTable.getModel();

    String URL = "jdbc:mysql://localhost:3306/final_project";
    String USER = "root";
    String PASS = "1234";
    String username = new String();

    int WIDTH = 1200;
    int HEIGHT = 500;

    public SingleView() {
        refresh();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(WIDTH + 5, HEIGHT + 50));
        dbTable.getTableHeader().setReorderingAllowed(false); // Disable column reordering
        dbTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dbTable.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(dbTable);
        scroll.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scroll.setViewportView(dbTable);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll, BorderLayout.CENTER);

        this.add(panel);
    }

    public void refresh() {
        dbModel.setRowCount(0);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            Statement db = con.createStatement();

            String query = "SELECT customer.CustomerNo, DateCreatedUpdated, CustomerName, DateOfBirth, Nationality, Citizenship, PresentAddress, PresentZip, PermanentAddress, PermanentZip, MobileNo, Occupation, TIN, IncomeSource, MonthlyIncome, EmpName, EmpDate, Position, AppointmentType, employment.BusinessCode, CodeDescription, GovOfficialName, GovOfficialRelationship, GovOfficialPosition, GovBranch " +
                    "FROM customer " +
                    "LEFT JOIN financial ON customer.CustomerNo = financial.CustomerNo " +
                    "LEFT JOIN employment ON customer.CustomerNo = employment.CustomerNo " +
                    "LEFT JOIN governmentrelationship ON customer.CustomerNo = governmentrelationship.CustomerNo " +
                    "LEFT JOIN businessnature ON employment.BusinessCode = businessnature.BusinessCode;"; // SQL query
            ResultSet rs = db.executeQuery(query);
            ResultSetMetaData rsMetaData = rs.getMetaData();

            int columns = rsMetaData.getColumnCount();
            String columnName[] = new String[columns];
            for (int i = 0; i < columns; i++)
                columnName[i] = rsMetaData.getColumnName(i + 1);
            dbModel.setColumnIdentifiers(columnName);

            String CustNo, DateUpdate, CustomerName, DateOfBirth, Nationality, Citizenship, PresentAddress, PresentZip, PermanentAddress, PermanentZip, MobileNo;
            String Occupation, TIN, IncomeSource, MonthlyIncome, EmpName, EmpDate, Position, AppointmentType, BusinessCode, CodeDescription;
            String GovOfficialName, GovOfficialRelation, GovOfficialPosition, GovBranch;
            while (rs.next()) {
                // CUSTOMER
                CustNo = rs.getString("customer.CustomerNo");
                DateUpdate = rs.getString("DateCreatedUpdated");
                CustomerName = rs.getString("CustomerName");
                DateOfBirth = rs.getString("DateOfBirth");
                Nationality = rs.getString("Nationality");
                Citizenship = rs.getString("Citizenship");
                PresentAddress = rs.getString("PresentAddress");
                PresentZip = rs.getString("PresentZip");
                PermanentAddress = rs.getString("PermanentAddress");
                PermanentZip = rs.getString("PermanentZip");
                MobileNo = rs.getString("MobileNo");

                // FINANCIAL
                Occupation = rs.getString("Occupation");
                TIN = rs.getString("TIN");
                IncomeSource = rs.getString("IncomeSource");
                MonthlyIncome = rs.getString("MonthlyIncome");

                // EMPLOYMENT
                EmpName = rs.getString("EmpName");
                EmpDate = rs.getString("EmpDate");
                Position = rs.getString("Position");
                AppointmentType = rs.getString("AppointmentType");
                BusinessCode = rs.getString("employment.BusinessCode");
                CodeDescription = rs.getString("CodeDescription");

                // GOVERNMENT RELATIONSHIP
                GovOfficialName = rs.getString("GovOfficialName");
                GovOfficialRelation = rs.getString("GovOfficialRelationship");
                GovOfficialPosition = rs.getString("GovOfficialPosition");
                GovBranch = rs.getString("GovBranch");

                String row[] = {CustNo, DateUpdate, CustomerName, DateOfBirth, Nationality, Citizenship, PresentAddress, PresentZip, PermanentAddress, PermanentZip, MobileNo,
                        Occupation, TIN, IncomeSource, MonthlyIncome, EmpName, EmpDate, Position, AppointmentType, BusinessCode, CodeDescription, GovOfficialName, GovOfficialRelation, GovOfficialPosition, GovBranch};
                dbModel.addRow(row);
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void deleteRecord() {
        int[] selectedRows = dbTable.getSelectedRows();
        Set<String> toDelete = new HashSet<>();

        for (int row : selectedRows) {
            String customerNo = dbTable.getValueAt(row, 0).toString();
            toDelete.add(customerNo);
        }

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String deleteCustomerQuery = "DELETE FROM customer WHERE CustomerNo = ?";
            String deleteFinancialQuery = "DELETE FROM financial WHERE CustomerNo = ?";
            String deleteEmploymentQuery = "DELETE FROM employment WHERE CustomerNo = ?";
            String deleteGovernmentQuery = "DELETE FROM governmentrelationship WHERE CustomerNo = ?";

            PreparedStatement deleteCustomerStmt = con.prepareStatement(deleteCustomerQuery);
            PreparedStatement deleteFinancialStmt = con.prepareStatement(deleteFinancialQuery);
            PreparedStatement deleteEmploymentStmt = con.prepareStatement(deleteEmploymentQuery);
            PreparedStatement deleteGovernmentStmt = con.prepareStatement(deleteGovernmentQuery);

            for (String customerNo : toDelete) {
                deleteCustomerStmt.setString(1, customerNo);
                deleteFinancialStmt.setString(1, customerNo);
                deleteEmploymentStmt.setString(1, customerNo);
                deleteGovernmentStmt.setString(1, customerNo);

                deleteCustomerStmt.executeUpdate();
                deleteFinancialStmt.executeUpdate();
                deleteEmploymentStmt.executeUpdate();
                deleteGovernmentStmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void editRecord(String username) {
        int selectedRow = dbTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to edit.", "Edit Record", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String customerNo = dbTable.getValueAt(selectedRow, 0).toString();
        Window window = SwingUtilities.windowForComponent(this);
        window.dispose();
        openUpdate(customerNo, username);
    }

    public void searchRecord(String input) {
        String query = "SELECT customer.CustomerNo, DateCreatedUpdated, CustomerName, DateOfBirth, Nationality, Citizenship, PresentAddress, PresentZip, PermanentAddress, PermanentZip, MobileNo, Occupation, TIN, IncomeSource, MonthlyIncome, EmpName, EmpDate, Position, AppointmentType, employment.BusinessCode, CodeDescription, GovOfficialName, GovOfficialRelationship, GovOfficialPosition, GovBranch " +
                    "FROM customer " +
                    "LEFT JOIN financial ON customer.CustomerNo = financial.CustomerNo " +
                    "LEFT JOIN employment ON customer.CustomerNo = employment.CustomerNo " +
                    "LEFT JOIN governmentrelationship ON customer.CustomerNo = governmentrelationship.CustomerNo " +
                    "LEFT JOIN businessnature ON employment.BusinessCode = businessnature.BusinessCode " +
                    "WHERE customer.CustomerNo LIKE ? OR customer.CustomerName LIKE ?;";

        dbModel.setRowCount(0);
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement stmt = con.prepareStatement(query);
            Class.forName("com.mysql.cj.jdbc.Driver");

            stmt.setString(1, "%" + input + "%");
            stmt.setString(2, "%" + input + "%");

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();

            int columns = rsMetaData.getColumnCount();
            String columnName[] = new String[columns];
            for (int i = 0; i < columns; i++)
                columnName[i] = rsMetaData.getColumnName(i + 1);
            dbModel.setColumnIdentifiers(columnName);

            String CustNo, DateUpdate, CustomerName, DateOfBirth, Nationality, Citizenship, PresentAddress, PresentZip, PermanentAddress, PermanentZip, MobileNo;
            String Occupation, TIN, IncomeSource, MonthlyIncome, EmpName, EmpDate, Position, AppointmentType, BusinessCode, CodeDescription;
            String GovOfficialName, GovOfficialRelation, GovOfficialPosition, GovBranch;
            while (rs.next()) {
                // CUSTOMER
                CustNo = rs.getString("customer.CustomerNo");
                DateUpdate = rs.getString("DateCreatedUpdated");
                CustomerName = rs.getString("CustomerName");
                DateOfBirth = rs.getString("DateOfBirth");
                Nationality = rs.getString("Nationality");
                Citizenship = rs.getString("Citizenship");
                PresentAddress = rs.getString("PresentAddress");
                PresentZip = rs.getString("PresentZip");
                PermanentAddress = rs.getString("PermanentAddress");
                PermanentZip = rs.getString("PermanentZip");
                MobileNo = rs.getString("MobileNo");

                // FINANCIAL
                Occupation = rs.getString("Occupation");
                TIN = rs.getString("TIN");
                IncomeSource = rs.getString("IncomeSource");
                MonthlyIncome = rs.getString("MonthlyIncome");

                // EMPLOYMENT
                EmpName = rs.getString("EmpName");
                EmpDate = rs.getString("EmpDate");
                Position = rs.getString("Position");
                AppointmentType = rs.getString("AppointmentType");
                BusinessCode = rs.getString("employment.BusinessCode");
                CodeDescription = rs.getString("CodeDescription");

                // GOVERNMENT RELATIONSHIP
                GovOfficialName = rs.getString("GovOfficialName");
                GovOfficialRelation = rs.getString("GovOfficialRelationship");
                GovOfficialPosition = rs.getString("GovOfficialPosition");
                GovBranch = rs.getString("GovBranch");

                String row[] = {CustNo, DateUpdate, CustomerName, DateOfBirth, Nationality, Citizenship, PresentAddress, PresentZip, PermanentAddress, PermanentZip, MobileNo,
                        Occupation, TIN, IncomeSource, MonthlyIncome, EmpName, EmpDate, Position, AppointmentType, BusinessCode, CodeDescription, GovOfficialName, GovOfficialRelation, GovOfficialPosition, GovBranch};
                dbModel.addRow(row);
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void openUpdate(String selectedCustomerNo, String username) {
        SwingUtilities.invokeLater(() -> new Update(selectedCustomerNo, username).setVisible(true));
    }
}
