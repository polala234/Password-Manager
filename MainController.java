import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.*;

public class MainController {

    //all main elements
    private PasswordDatabase database;
    private MainFrame mainFrame;
    private PasswordTableModel tableModel;
    private File currentFile;
    private String masterPassword;
    private List<String> allCategories;

    public MainController() {
        database = new PasswordDatabase();
        mainFrame = new MainFrame();
        tableModel = new PasswordTableModel(database.getAll());
        mainFrame.getPasswordTable().setModel(tableModel);
        initListeners();
        mainFrame.setVisible(true);
        allCategories = database.getAllCategories();
    }
    //action listeners
    private void initListeners() {
        mainFrame.getAddButton().addActionListener(e -> addEntry());
        mainFrame.getDeleteButton().addActionListener(e -> deleteEntry());
        mainFrame.getSaveButton().addActionListener(e -> saveFile());
        mainFrame.getLoadButton().addActionListener(e -> loadFile());
        mainFrame.getEditButton().addActionListener(e -> editEntry());
        mainFrame.getGenerateButton().addActionListener(e -> generatePassword());
        mainFrame.getCategoryButton().addActionListener(e -> manageCategories());
        mainFrame.getSortBox().addActionListener(e -> sortPasswords());
        mainFrame.getSearchField().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String q = mainFrame.getSearchField().getText();
                List<Password> results = database.searchByName(q);
                tableModel.setEntries(results);
            }
        });
    }

    //add new password entry
    private void addEntry() {
        List<String> categories = allCategories;
        //invoke password entry dialog
        PasswordEntryDialog dialog = new PasswordEntryDialog(mainFrame, "Add Password", null, categories);
        dialog.setVisible(true);

        //when confirmed add entry to database
        if (dialog.isConfirmed()) {
            Password newEntry = dialog.getPasswordData();
            database.add(newEntry);
            tableModel.setEntries(database.getAll());
            refreshAllCategories();
        }
    }

    //delete selected entry
    private void deleteEntry() {
        //get selected password
        int row = mainFrame.getPasswordTable().getSelectedRow();
        if (row >= 0) { //check if row is in correct range
            Password p = tableModel.getEntryAt(row);
            database.delete(p);
            tableModel.setEntries(database.getAll());
            //refresh categories list
            refreshAllCategories();
        }
    }

    //edit selected entry
    private void editEntry() {
        int row = mainFrame.getPasswordTable().getSelectedRow();
        if (row < 0) return;

        Password current = tableModel.getEntryAt(row);
        List<String> categories = database.getAllCategories();
        PasswordEntryDialog dialog = new PasswordEntryDialog(mainFrame, "Edit Password", current, categories);
        dialog.setVisible(true);

        //save updated
        if (dialog.isConfirmed()) {
            //save updated password
            Password updated = dialog.getPasswordData();
            database.edit(row, updated);
            tableModel.setEntries(database.getAll());
            //update categories list
            refreshAllCategories();
        }
    }

    //generate random passwords
    private void generatePassword() {
        PasswordGeneratorDialog dialog = new PasswordGeneratorDialog(mainFrame);
        dialog.setVisible(true);

        //if password is goig to be used as a new entry
        if (dialog.getPasswordForUse() != null) {
            PasswordEntryDialog addDialog = new PasswordEntryDialog(mainFrame, "Add Password", null, allCategories);
            addDialog.setGeneratedPassword(dialog.getPasswordForUse());
            addDialog.setVisible(true);

            //when data is entered add to database
            if (addDialog.isConfirmed()) {
                Password newEntry = addDialog.getPasswordData();
                database.add(newEntry);
                tableModel.setEntries(database.getAll());

                refreshAllCategories();
            }

        }

    }

    //save data to file
    private void saveFile() {
        if (currentFile == null) {
            JFileChooser chooser = new JFileChooser(); //choose file destination
            if (chooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();

                //add .txt if missing to save to a text file
                if (!currentFile.getName().toLowerCase().endsWith(".txt")) {
                    currentFile = new File(currentFile.getAbsolutePath() + ".txt");
                }
            } else {
                return;
            }

        }

        //master password for encrypting the file
        masterPassword = (String) JOptionPane.showInputDialog(
                mainFrame,
                "Enter master password:",
                "Password",
                JOptionPane.PLAIN_MESSAGE,
                MyIcons.passwordIcon,
                null,
                null
        );
        //if no master password si defined-> error message, dont save the file
        if (masterPassword == null || masterPassword.trim().isEmpty()) {
            mainFrame.showMessage("Save cancelled: no master password provided.", "Error", JOptionPane.ERROR_MESSAGE, MyIcons.errorIcon);
            return;
        }

        //write database to file format
        int shift = PasswordCipher.getShift(masterPassword);
        String content = FileManager.encryptDataToFile(database.getAll(), shift);
        //date of modification
        String date = String.valueOf(System.currentTimeMillis());

        //try to save file, else display error message
        try {
            FileManager.saveEncrypted(currentFile, content, date);
            mainFrame.showMessage("Saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE, MyIcons.savedIcon);
        } catch (IOException e) {
            mainFrame.showMessage("Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, MyIcons.errorIcon);
        }
    }

    //load data from file
    private void loadFile() {
        JFileChooser chooser = new JFileChooser(); //choose file
        if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
        } else {
            return;
        }
        //prompt to input master password
        String input = (String) JOptionPane.showInputDialog(
                mainFrame,
                "Enter master password:",
                "Password",
                JOptionPane.PLAIN_MESSAGE,
                MyIcons.passwordIcon,
                null,
                null
        );

        //if a non empty password is provided assign it as master password
        if (input != null && !input.trim().isEmpty()) {
            masterPassword = input;
        } else {
            mainFrame.showMessage("No master password provided.", "Error", JOptionPane.ERROR_MESSAGE, MyIcons.errorIcon);
            return;
        }

        //try to load data from file and decrypt
        try {
            String content = FileManager.loadEncrypted(currentFile);
            String date = FileManager.extractDate(currentFile); //extract first line containing date
            String[] lines = content.split("\n", 2); //split date from content
            String encrypted = lines.length > 1 ? lines[1] : ""; //if there is no content return nothing

            //get shift from master password
            int shift = PasswordCipher.getShift(masterPassword);
            //decrypt and write to list
            database = new PasswordDatabase(FileManager.decryptDataFromFile(encrypted, shift));
            tableModel.setEntries(database.getAll());

            //update categories list
            allCategories = new ArrayList<>(database.getAllCategories());

            //format date
            long timestamp = Long.parseLong(date);
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String formatted = format.format(new java.util.Date(timestamp));
            mainFrame.showMessage("File loaded.\nLast modified: " + formatted, "", JOptionPane.INFORMATION_MESSAGE, MyIcons.fileIcon);
            mainFrame.getLastModifiedLabel().setText("Last Modified: " + formatted);

        } catch (IOException e) {
            mainFrame.showMessage("Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, MyIcons.fileErrorIcon);
        }
        try {
            //update modification date
            FileManager.updateDate(currentFile, String.valueOf(System.currentTimeMillis()));
        } catch (IOException ex) {
            mainFrame.showMessage("Failed to update modification date!: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, MyIcons.fileErrorIcon);
        }
    }

    private void refreshAllCategories() {
        Set<String> merged = new TreeSet<>(allCategories); //keep manually added
        merged.addAll(database.getAllCategories());        //add used categories
        allCategories = new ArrayList<>(merged);    }

    //manage categories functions
    private void manageCategories() {
        CategoryManagerDialog dialog = new CategoryManagerDialog(mainFrame, allCategories);
        dialog.setVisible(true);

        //delete categories and all related passwords
        List<String> deleted = dialog.getDeletedCategories();
        if (!deleted.isEmpty()) {
            for (String category : deleted) {
                database.deleteCategory(category);
            }
            tableModel.setEntries(database.getAll());
        }

        //keep dialog categories, then merge with used ones
        allCategories = new ArrayList<>(dialog.getCurrentCategories());
        refreshAllCategories(); // merges with categories used in database
    }


    //sort passwords by category or by name
    private void sortPasswords() {
        String selected = mainFrame.getSortBox().getSelectedItem().toString();
        if (selected.equals("Name")) {
            tableModel.setEntries(database.sortByName());
        } else if (selected.equals("Category")) {
            tableModel.setEntries(database.sortByCategory());
        } else {
            tableModel.setEntries(database.getAll());
        }
    }


}
