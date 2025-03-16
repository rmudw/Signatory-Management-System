import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// Displays all the records in the database
public class Records extends JPanel {
    JPanel menuPanel = new JPanel();
    String user;
    GridBagConstraints gbc = new GridBagConstraints();
    MultipleView multiple = new MultipleView();
    SingleView single = new SingleView();

    JRadioButton bttn1 = new JRadioButton("Multiple Tables");
    JRadioButton bttn2 = new JRadioButton("Single Table");
    JTextField searchInput = new JTextField("Enter customer number or name");

    public Records() {
        menu();
        showMultipleTables(); // Initial start up
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        bttn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent click) {
                showMultipleTables();
            }
        });

        bttn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent click) {
                showSingleTable();
            }
        });
    }

    private void menu() {
        searchInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchInput.getText().equals("Enter customer number or name")) {
                    searchInput.setText("");
                    searchInput.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchInput.getText().isEmpty()) {
                    searchInput.setForeground(Color.GRAY);
                    searchInput.setText("Enter customer number or name");
                }
            }
        });

        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setMaximumSize(new Dimension(1900, 110));
        menuPanel.setBackground(Color.WHITE);
        JPanel container = new JPanel(new GridBagLayout());

        JButton edit = new JButton("Edit");
        edit.setBackground(Color.decode("#40AA5B"));
        edit.setForeground(Color.WHITE);

        JButton delete = new JButton("Delete");
        delete.setBackground(Color.decode("#40AA5B"));
        delete.setForeground(Color.WHITE);

        JButton search = new JButton("Search");
        search.setBackground(Color.decode("#40AA5B"));
        search.setForeground(Color.WHITE);

        JButton clearSearch = new JButton("Clear Search");
        clearSearch.setBackground(Color.decode("#40AA5B"));
        clearSearch.setForeground(Color.WHITE);

        JLabel view = new JLabel("View: ");
        ButtonGroup viewSelection = new ButtonGroup();
        bttn1.setBackground(Color.WHITE);
        bttn2.setBackground(Color.WHITE);
        viewSelection.add(bttn1);
        viewSelection.add(bttn2);
        bttn1.setSelected(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(10, 15, 5, 0);
        gbc.gridwidth = 3;
        searchInput.setPreferredSize(new Dimension(500, 35));
        container.add(searchInput, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 5, 0);
        search.setPreferredSize(new Dimension(90, 35));
        container.add(search, gbc);
        search.addActionListener(this::searchAction);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 2, 5, 0);
        clearSearch.setPreferredSize(new Dimension(108, 35));
        container.add(clearSearch, gbc);
        clearSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent click) {
                searchInput.setText("");
                refresh();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 5, 0);
        container.add(edit, gbc);
        edit.addActionListener(this::editAction);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 2, 5, 0);
        container.add(delete, gbc);
        delete.addActionListener(this::deleteAction);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 15, 10, 0);
        container.add(view, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        container.add(bttn1, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 2, 10, 0);
        container.add(bttn2, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        container.setBackground(Color.WHITE);
        menuPanel.add(container);
    }

    private void showMultipleTables() {
        JScrollPane multipleScroll = new JScrollPane(multiple);
        multipleScroll.getVerticalScrollBar().setUnitIncrement(10);
        multipleScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        multipleScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        multiple.setBackground(Color.WHITE);

        this.removeAll();
        this.add(menuPanel);
        this.add(multipleScroll);
        this.repaint();
        this.revalidate();
    }

    private void showSingleTable() {
        JScrollPane singleScroll = new JScrollPane(single);
        singleScroll.getVerticalScrollBar().setUnitIncrement(10);
        singleScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        singleScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        single.setBackground(Color.WHITE);

        this.removeAll();
        this.add(menuPanel);
        this.add(singleScroll);
        this.repaint();
        this.revalidate();
    }

    public void searchAction(ActionEvent click) {
        if(searchInput.getText().equals("Enter customer number or name") || searchInput.getText().equals("")) {
            return;
        } else {
            multiple.searchRecord(searchInput.getText());
            single.searchRecord(searchInput.getText());
        }
    }

    public void editAction(ActionEvent click) {
        if (bttn1.isSelected()) {
            multiple.editRecord(user);
        } else if (bttn2.isSelected()) {
            single.editRecord(user);
        }
    }

    public void refresh() {
        multiple.refresh();
        single.refresh();
    }

    private void deleteAction(ActionEvent e) {
        if (bttn1.isSelected()) {
            multiple.deleteRecord();
        } else if (bttn2.isSelected()) {
            single.deleteRecord();
        }

        if(searchInput.getText().equals("Enter customer number or name") || searchInput.getText().equals("")) {
            refresh();   
        } else{
            multiple.searchRecord(searchInput.getText());
            single.searchRecord(searchInput.getText());
        }
    }
}
