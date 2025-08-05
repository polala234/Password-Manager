import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    //main window components:
    //main table with all saved passwords
    private JTable passwordTable;
    //action buttons
    private JButton addButton, deleteButton, saveButton, loadButton, generateButton, editButton, categoryButton;
    //search bar
    private JTextField searchField;
    //label containg last modification date
    private JLabel lastModifiedLabel;
    //sort dropdown
    private JComboBox<String> sortBox;
    //logo icon
    JLabel logoLabel = new JLabel(MyIcons.logo);


    public MainFrame() {
        //main window parameters
        setTitle("My Password Manager"); //name
        setIconImage(MyIcons.logo.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); //size
        setLocationRelativeTo(null); //relative to screen center

        //create password table and make it scrollable in case it overflows
        passwordTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(passwordTable);

        //create all action buttons
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        generateButton = new JButton("Generate");
        editButton = new JButton("Edit");
        categoryButton = new JButton("Manage Categories");
        searchField = new JTextField(20);

        sortBox = new JComboBox<>(new String[] { "Sort by Name", "Sort by Category" });

        //create top left panel with load, save buttons, information on last modification and search bar
        JPanel topPanelL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanelL.add(logoLabel);
        topPanelL.add(loadButton);
        topPanelL.add(saveButton);
        topPanelL.add(new JLabel("Sort by:"));
        sortBox = new JComboBox<>(new String[] { "-", "Name", "Category" });
        topPanelL.add(sortBox);
        lastModifiedLabel = new JLabel("Last Modified: -");
        topPanelL.add(lastModifiedLabel);

        //create top right panel
        JPanel topPanelR = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanelR.add(new JLabel("Search:"));
        topPanelR.add(searchField);

        //create bottom panel with add, edit, delete, generate and categories buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(addButton);
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(generateButton);
        bottomPanel.add(categoryButton);

        //join left and right panel to be at the top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(topPanelL, BorderLayout.WEST);
        topPanel.add(topPanelR, BorderLayout.EAST);

        //define panel placement in window
        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //getters
    public JTable getPasswordTable() { return passwordTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getLoadButton() { return loadButton; }
    public JButton getGenerateButton() { return generateButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getCategoryButton() { return categoryButton; }
    public JTextField getSearchField() { return searchField; }
    public JLabel getLastModifiedLabel() { return lastModifiedLabel; }
    public JComboBox<String> getSortBox() { return sortBox; }

    //show pop up messages
    public void showMessage(String message, String title, int messageType, Icon icon) {
        JOptionPane.showMessageDialog(this, message, title, messageType, icon);
    }

}
