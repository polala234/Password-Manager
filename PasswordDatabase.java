import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordDatabase {
    //all saved passwordst
    private List<Password> entries;

    public PasswordDatabase() {
        this.entries = new ArrayList<>();
    }
    public PasswordDatabase(List<Password> entries) {
        this.entries = new ArrayList<>(entries);
    }

    //add new password to database
    public void add(Password entry) {
        entries.add(entry);
    }

    //delete a password from database
    public void delete(Password entry) {
        entries.remove(entry);
    }

    //edit an existing password
    public void edit(int index, Password newEntry) {
        if (index >= 0 && index < entries.size()) {
            entries.set(index, newEntry);
        }
    }

    //search passwords by name
    public List<Password> searchByName(String name) {
        return entries.stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Password> sortByName() {
        return entries.stream()
                .sorted((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()))
                .collect(Collectors.toList());
    }

    public List<Password> sortByCategory() {
        return entries.stream()
                .sorted((e1, e2) -> e1.getCategory().compareToIgnoreCase(e2.getCategory()))
                .collect(Collectors.toList());
    }

    //delete a category, all passwords with given category
    public void deleteCategory(String category) {
        entries.removeIf(e -> e.getCategory().equalsIgnoreCase(category));
    }

    //getters
    public List<Password> getAll() {
        return new ArrayList<>(entries);
    }

    //get a list of all categories in database
    public List<String> getAllCategories() {
        return entries.stream()
                .map(Password::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
