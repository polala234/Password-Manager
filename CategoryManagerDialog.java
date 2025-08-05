import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class CategoryManagerDialog extends JDialog {
    //parameters
    private JList<String> categoryList;
    private DefaultListModel<String> model;
    private JButton addButton, deleteButton, closeButton;
    private List<String> deletedCategories = new ArrayList<>();

    public CategoryManagerDialog(JFrame parent, List<String> categories) {
        super(parent, "Manage Categories", true);
        setLayout(new BorderLayout());

        //create a category list of passed categories
        model = new DefaultListModel<>();
        categories.forEach(model::addElement);
        categoryList = new JList<>(model);
        add(new JScrollPane(categoryList), BorderLayout.CENTER);

        //action buttons
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        closeButton = new JButton("Close");

        //add new category
        addButton.addActionListener(e -> {
            String newCategory = (String) JOptionPane.showInputDialog(
                    this,
                    "Enter new category:",
                    "New Category",
                    JOptionPane.QUESTION_MESSAGE,
                    MyIcons.newFolder,
                    null,
                    null
            );
            if (newCategory != null && !newCategory.trim().isEmpty() && !model.contains(newCategory)) {
                model.addElement(newCategory);
            }
        });

        //delete a category
        deleteButton.addActionListener(e -> {
            String selected = categoryList.getSelectedValue();
            if (selected != null) {
                deletedCategories.add(selected);
                model.removeElement(selected);
            }
        });

        closeButton.addActionListener(e -> setVisible(false));

        //create bottom paenl with action buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(300, 300);
        setLocationRelativeTo(parent);
    }

    //get categories list
    public List<String> getCurrentCategories() {
        return Collections.list(model.elements());
    }

    //get deleted categories
    public List<String> getDeletedCategories() {
        return deletedCategories;
    }
}
