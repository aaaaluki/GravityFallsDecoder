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
 * @see <a href="https://en.wikipedia.org/wiki/Caesar_cipher">Caesar
 * Wikipedia</a>
 * @author luki
 */
public class Caesar extends Cipher {

    private static final String NAME = "CAESAR";
    private final Map<Integer, String> cipher_;
    private Analyzer analyzer_;

    /**
     * Constructor for the Caesar cipher
     *
     * All the possible ciphers are calculated here.
     *
     * @param alphabet alphabet_ from the language that is going to be used
     * @param analyzer Analysis tool for this cipher
     */
    public Caesar(String alphabet, Analyzer analyzer) {
        super(alphabet);

        // Calculate all ciphers
        // This caesar cipher uses right rotation
        cipher_ = new HashMap<>();
        for (int key = 0; key < alphabet.length(); key++) {
            cipher_.put(key, alphabet.substring(key).concat(alphabet.substring(0, key)));
        }
        
        analyzer_ = analyzer;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getKeyClass() {
        return Integer.class;
    }

    @Override
    public String validateKey(Key key) {
        if (key == null) {
            return ERR_NULL_KEY;
        }
        
        if (!(key.get() instanceof Integer)) {
            return "The key must be an Integer!";
        }
        
        Integer val = key.getInteger();
        if (val < 0 || val >= alphabet_.length()) {
            return String.format("The key must be an integer between 0 and %d", alphabet_.length() - 1);
        }
        
        return null;
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
    public List<DecryptGuess> decryptWithoutKey(String encryptedText, DecryptGuess decryptGuess) {
        List<DecryptGuess> guesses = new ArrayList<>();

        // Keys 0 and 26 (in english) can be skipped because we would obtain the same text
        for (int i = 1; i < alphabet_.length(); i++) {
            Key key = new Key(i);
            String decryptedText = decrypt(encryptedText, key);
            double error = analyzer_.analyze(decryptedText);

            if (decryptedText.equals(encryptedText)) {
                return guesses;
            }

            if (decryptGuess == null) {
                guesses.add(new DecryptGuess(NAME, key, error, decryptedText));
            } else {
                guesses.add(decryptGuess.clone().addStep(NAME, key, error, decryptedText));
            }
        }

        return guesses;
    }
}
