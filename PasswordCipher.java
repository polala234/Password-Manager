public class PasswordCipher {

    //get shift according to sum of characters in masterPassword
    public static int getShift(String masterPassword) {
        int shift = 0;
        for (char c : masterPassword.toCharArray()) {
            shift += c;
        }
        //the value has to be within number of characters in ascii
        return shift % 256;
    }

    //encrypt text with the defined shift
    public static String encrypt(String text, int shift) {
        //replace special characters used for separation
        text = text.replace("|", "[PIPE]").replace("\n", "[NL]");

        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append((char) (c + shift));
        }
        return sb.toString();
    }

    //decrypt text with the defined shift
    public static String decrypt(String text, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append((char) (c - shift));
        }
        String decrypted = sb.toString();
        //replace any special characters back
        return decrypted.replace("[PIPE]", "|").replace("[NL]", "\n");
    }
}
