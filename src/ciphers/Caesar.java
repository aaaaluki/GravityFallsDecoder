package ciphers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luki
 */
public class Caesar extends Cipher {

    static final String NAME = "CAESAR";
    Map<Integer, String> cipher;

    public Caesar(String alphabet) {
        super(alphabet);
        super.NAME = NAME;
        
        // Calculate all ciphers
        // This caesar cipher uses right rotation
        cipher = new HashMap<>();
        for (int key = 0; key < alphabet.length(); key++) {
            cipher.put(key, alphabet.substring(key).concat(alphabet.substring(0, key)));
        }
    }

    @Override
    public String encrypt(String text, int key) {
        StringBuilder sb = new StringBuilder();
        text = normalize(text);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (alphabet.indexOf(ch) != -1) {
                sb.append(cipher.get(key).charAt(alphabet.indexOf(ch)));
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, int key) {
        return encrypt(text, alphabet.length() - key);
    }
}
