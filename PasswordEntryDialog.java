import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PasswordEntryDialog extends JDialog {
    //password parameters
    private JTextField nameField, passwordField, loginField, websiteField, locationField;
    //categories as drop-down box
    private JComboBox<String> categoryBox;
    //check flag if user submited data
    private boolean confirmed = false;

    public PasswordEntryDialog(JFrame parent, String title, Password existing, List<String> categories) {
        super(parent, title, true);
        setLayout(new GridLayout(7, 2, 5, 5));

        //required parameters
        nameField = new JTextField();
        passwordField = new JTextField();
        categoryBox = new JComboBox<>();
        categoryBox.setEditable(true);
        categories.forEach(categoryBox::addItem);

        //additional parameters
        loginField = new JTextField();
        websiteField = new JTextField();
        locationField = new JTextField();

        //prefill data if we are working on an existing password
        if (existing != null) {
            nameField.setText(existing.getName());
            passwordField.setText(existing.getPassword());
            categoryBox.setSelectedItem(existing.getCategory());
            loginField.setText(existing.getLogin());
            websiteField.setText(existing.getWebsite());
            locationField.setText(existing.getLocation());
        }

        //add labels and fields
        add(new JLabel("Name:")); add(nameField);
        add(new JLabel("Password:")); add(passwordField);
        add(new JLabel("Category:")); add(categoryBox);
        add(new JLabel("Login:")); add(loginField);
        add(new JLabel("Website:")); add(websiteField);
        add(new JLabel("Location:")); add(locationField);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        add(ok); add(cancel);

        ok.addActionListener(e -> {
            //make sure required fields are not empty
            if (nameField.getText().trim().isEmpty() ||
                    passwordField.getText().trim().isEmpty() ||
                    categoryBox.getSelectedItem().toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Name, Password, and Category are required.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE, MyIcons.errorIcon);
            } else {
                confirmed = true;
                setVisible(false);
            }
        });

        cancel.addActionListener(e -> setVisible(false));

        pack(); //automatic sizing
        setLocationRelativeTo(parent); //location of the pop up dialog
    }

    //return flag
    public boolean isConfirmed() {
        return confirmed;
    }

    //return password parameters
    public Password getPasswordData() {
        return new Password(
                nameField.getText(),
                passwordField.getText(),
                categoryBox.getSelectedItem().toString(),
                loginField.getText(),
                websiteField.getText(),
                locationField.getText()
        );
    }

    //set password to generated value
    public void setGeneratedPassword(String generated) {
        passwordField.setText(generated);
    }

}
