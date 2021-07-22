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

    public Binary(String alphabet) {
        super(alphabet);
        super.NAME = NAME;
    }

    @Override
    public String encrypt(String text, Key key) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (alphabet_.indexOf(ch) == -1) {
                sb.append(ch);
                continue;
            }

            sb.append(Integer.toBinaryString(ch));
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, Key key) {
        if (!text.matches("[01 ]+")) {
            // It's not a binary string, return the same text
            return text;
        }

        text = text.replace(" ", "");
        if (text.length() % 8 != 0) {
            // If the number of 0 and 1 is not divisable by 8 return the same text
            return text;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i += 8) {
            String ch = text.substring(i, i + 8);
            int val = Integer.valueOf(ch, 2);
            
            sb.append((char) val);
        }
        
        return sb.toString();
    }

    @Override
    public List<DecryptGuess> decryptWithoutKey(String encryptedText, Analyzer analyzer) {
        List<DecryptGuess> guesses = new ArrayList<>();
        Key key = new Key();
        String decryptedText = decrypt(encryptedText, key);
        double error = analyzer.analyze(decryptedText);

        guesses.add(new DecryptGuess(NAME, key, error, decryptedText));

        return guesses;
    }
}
