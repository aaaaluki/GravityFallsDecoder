package ciphers;

import java.util.HashMap;
import java.util.Map;
import utils.TextHelper;

/**
 * Caesar cipher
 * https://en.wikipedia.org/wiki/Caesar_cipher
 *
 * @author luki
 */
public class Caesar extends Cipher {

    private static final String NAME = "CAESAR";
    private Map<Integer, String> cipher;

    /**
     * Constructor for the Caesar cipher
     * 
     * All the possible ciphers are calculated here.
     * 
     * @param alphabet alphabet from the language that is going to be used
     */
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
        text = TextHelper.normalize(text);

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
