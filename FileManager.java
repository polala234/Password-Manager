import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    //date prefix that allwos to check if timestamp is added in the file
    private static final String datePrefix = "#DATE:";

    //read file from the given path
    public static String loadEncrypted(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), "UTF-8");
    }

    //save encrypted data to file
    public static void saveEncrypted(File file, String encryptedData, String date) throws IOException {
        //add timestamp with date prefix at the beginning of file
        String content = datePrefix + date + "\n" + encryptedData;
        //write to file, create new file if doesn't exist, if exists, clear the file first
        Files.write(file.toPath(), content.getBytes("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    //encrypt password parameters and translate into a string format
    public static String encryptDataToFile(List<Password> list, int shift) {
        StringBuilder sb = new StringBuilder();
        for (Password p : list) {
            sb.append(PasswordCipher.encrypt(p.getName(), shift)).append("|")
                    .append(PasswordCipher.encrypt(p.getPassword(), shift)).append("|")
                    .append(PasswordCipher.encrypt(p.getCategory(), shift)).append("|")
                    .append(PasswordCipher.encrypt(p.getLogin(), shift)).append("|")
                    .append(PasswordCipher.encrypt(p.getWebsite(), shift)).append("|")
                    .append(PasswordCipher.encrypt(p.getLocation(), shift)).append("\n");
        }
        return sb.toString();
    }

    //translate string sequence into password list
    public static List<Password> decryptDataFromFile(String data, int shift) {
        List<Password> list = new ArrayList<>();
        for (String line : data.split("\n")) {
            String[] parts = line.split("\\|", -1); //-1 includes nulls for empty parameters
            if (parts.length == 6) {
                list.add(new Password(
                        PasswordCipher.decrypt(parts[0], shift),
                        PasswordCipher.decrypt(parts[1], shift),
                        PasswordCipher.decrypt(parts[2], shift),
                        PasswordCipher.decrypt(parts[3], shift),
                        PasswordCipher.decrypt(parts[4], shift),
                        PasswordCipher.decrypt(parts[5], shift)
                ));
            } else {
                list.add(new Password("?", "?", "?", "", "", ""));
            }
        }
        return list;
    }

    //read last modification date from file
    public static String extractDate(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String firstLine = reader.readLine();
            //id date is valid
            if (firstLine != null && firstLine.startsWith(datePrefix)) {
                return firstLine.substring(datePrefix.length());
            }
        }
        return "";
    }

    //update last modification date
    public static void updateDate(File file, String newDate) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
        String[] lines = content.split("\n", 2);
        //if the file correctly consists of date + encrypted data
        String encryptedData = lines.length > 1 ? lines[1] : "";
        //save file with new date
        saveEncrypted(file, encryptedData, newDate);
    }
}
