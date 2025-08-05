import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;

public class PasswordGeneratorDialog extends JDialog {

    //parameters
    private JTextField lengthField; //length of desired password
    private JTextField resultField; //th egenerated password
    private JButton generateButton, okButton, useButton; //action buttons
    private boolean confirmed = false; //flag
    private String generatedForUse = null; //password generated that the user is willing to use

    ////checkboxes for password complexity
    private JCheckBox upperCaseBox = new JCheckBox("A–Z", true);
    private JCheckBox lowerCaseBox = new JCheckBox("a–z", true);
    private JCheckBox digitsBox = new JCheckBox("0–9", true);
    private JCheckBox symbolsBox = new JCheckBox("!@#$%^&*", true);

    public PasswordGeneratorDialog(JFrame parent) {
        super(parent, "Generate Password", true);
        setLayout(new BorderLayout(10, 10));

        //define length of generated password
        lengthField = new JTextField("12");
        resultField = new JTextField();
        resultField.setEditable(false);

        //buttons
        generateButton = new JButton("Generate");
        okButton = new JButton("OK");
        useButton = new JButton("Use Password");

        //generate once button clicked
        generateButton.addActionListener(e -> {
            int len = Integer.parseInt(lengthField.getText()); //get length from length filed
            String charSet = buildCharacterSet(); //cheracters to be used according to selection
            //error message if no characters are selected
            if (charSet.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Select at least one character group.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE,
                        MyIcons.errorIcon);
                return;
            }
            //result
            resultField.setText(generatePassword(len, charSet));
        });

        //exit on ok
        okButton.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        //on use button click, go to
        useButton.addActionListener(e -> {
            if (!resultField.getText().isEmpty()) {
                generatedForUse = resultField.getText();
                confirmed = true;
                setVisible(false);
            }
        });

        //create an input panel for generating the password
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inputPanel.add(new JLabel("Length:"));
        inputPanel.add(lengthField);
        inputPanel.add(new JLabel("Character Set:"));
        inputPanel.add(new JLabel());
        inputPanel.add(upperCaseBox);
        inputPanel.add(lowerCaseBox);
        inputPanel.add(digitsBox);
        inputPanel.add(symbolsBox);
        inputPanel.add(new JLabel("Generated:"));
        inputPanel.add(resultField);

        //panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(generateButton);
        buttonPanel.add(useButton);
        buttonPanel.add(okButton);

        //layout
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    //get generated password that is going to be used
    public String getPasswordForUse() {
        return generatedForUse;
    }

    //ranges for selected characters (lower case, upper case, numbers, symbols)
    private String range(int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            sb.append((char) i);
        }
        return sb.toString();
    }

    //build set of characters to be used for generated passwoerd according to selection
    private String buildCharacterSet() {
        StringBuilder sb = new StringBuilder();
        if (upperCaseBox.isSelected()) sb.append(range(65, 90));  //upper case
        if (lowerCaseBox.isSelected()) sb.append(range(97, 122)); //lower case
        if (digitsBox.isSelected()) sb.append(range(48, 57));     //numbers
        if (symbolsBox.isSelected()) sb.append("!@#$%^&*");       //symbols
        return sb.toString();
    }

    //create random password
    private String generatePassword(int length, String charSet) {
        //secure random number, good for generating random passwords
        SecureRandom rand = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(charSet.charAt(rand.nextInt(charSet.length())));
        }
        return sb.toString();
    }
}
