package ciphers;

import analysis.Analyzer;
import analysis.DecryptGuess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.TextHelper;

/**
 * Caesar cipher
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Caesar_cipher">Caesar Wikipedia</a>
 * @author luki
 */
public class Caesar extends Cipher {

    private static final String NAME = "CAESAR";
    private Map<Integer, String> cipher_;

    /**
     * Constructor for the Caesar cipher
     *
     * All the possible ciphers are calculated here.
     *
     * @param alphabet alphabet_ from the language that is going to be used
     */
    public Caesar(String alphabet) {
        super(alphabet);
        super.NAME = NAME;

        // Calculate all ciphers
        // This caesar cipher uses right rotation
        cipher_ = new HashMap<>();
        for (int key = 0; key < alphabet.length(); key++) {
            cipher_.put(key, alphabet.substring(key).concat(alphabet.substring(0, key)));
        }
    }

    @Override
    public String encrypt(String text, Key key) {
        StringBuilder sb = new StringBuilder();
        text = TextHelper.normalize(text);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (alphabet_.indexOf(ch) != -1) {
                sb.append(cipher_.get(key.getInteger()).charAt(alphabet_.indexOf(ch)));
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, Key key) {
        Key decryptKey = new Key(alphabet_.length() - key.getInteger());
        return encrypt(text, decryptKey);
    }

    @Override
    public List<DecryptGuess> decryptWithoutKey(String encryptedText, Analyzer analyzer) {
        List<DecryptGuess> guesses = new ArrayList<>();

        for (int i = 1; i <= alphabet_.length(); i++) {
            Key key = new Key(i);
            String decryptedText = decrypt(encryptedText, key);
            double error = analyzer.analyze(decryptedText);

            guesses.add(new DecryptGuess(NAME, key, error, decryptedText));
        }
        
        return guesses;
    }
}
