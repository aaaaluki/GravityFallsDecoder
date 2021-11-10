package ciphers;

import analysis.Analyzer;
import analysis.DecryptGuess;
import java.util.ArrayList;
import java.util.List;

/**
 * Binary encoding cipher, read the
 *
 * @author luki
 */
public class Binary extends Cipher {

    private static final String NAME = "BINARY";
    private Analyzer analyzer_;


    /**
     * Constructor for the binary cipher(encoder)
     * 
     * @param alphabet alphabet_ from the language that is going to be used
     * @param analyzer Analysis tool for this cipher
     */
    public Binary(String alphabet, Analyzer analyzer) {
        super(alphabet);
        
        analyzer_ = analyzer;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getKeyClass() {
        return null;
    }
    
    @Override
    public String validateKey(Key key) {
        if (key == null) {
            return ERR_NULL_KEY;
        }
        
        return null;
    }
    
    @Override
    public String encrypt(String text, Key key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == ' ') {
                sb.append(ch);
                continue;
            }

            String str = Integer.toBinaryString(ch);
            if (str.length() < 8) {
                str = "000000000".substring(0, 8 - str.length()).concat(str);
            } else {
                str = str.substring(str.length() - 8);
            }
            
            sb.append(str);
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, Key key) {
        if (!text.matches("[01 ]+")) {
            // It's not a binary string, return the same text
            return text;
        }

        StringBuilder sb = new StringBuilder();
        for (String word : text.split(" ")) {
            if (word.length() % 8 != 0) {
                // If the number of 0 and 1 is not divisable by 8 return the same text
                return text;
            }

            for (int i = 0; i < word.length(); i += 8) {
                String ch = word.substring(i, i + 8);
                int val = Integer.valueOf(ch, 2);

                sb.append((char) val);
            }
            
            sb.append(" ");
        }

        return sb.toString();
    }

    @Override
    public List<DecryptGuess> decryptWithoutKey(String encryptedText, DecryptGuess decryptGuess) {
        List<DecryptGuess> guesses = new ArrayList<>();
        Key key = new Key();
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

        return guesses;
    }
}
