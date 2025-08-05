import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PasswordTableModel extends AbstractTableModel {

    //table with password records and all its parameters
    private List<Password> entries;
    private final String[] columns = {"Name", "Password", "Category", "Login", "Website", "Location"};

    public PasswordTableModel(List<Password> entries) {
        this.entries = entries;
    }

    public void setEntries(List<Password> entries) {
        this.entries = entries;
        //refresh the view after change
        fireTableDataChanged();
    }

    public Password getEntryAt(int row) {
        return entries.get(row);
    }

    //override necessairy methods
    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Password p = entries.get(row);
        switch (col) {
            case 0: return p.getName();
            case 1: return p.getPassword();
            case 2: return p.getCategory();
            case 3: return p.getLogin();
            case 4: return p.getWebsite();
            case 5: return p.getLocation();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }
}
