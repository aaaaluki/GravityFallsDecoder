package ciphers;

import analysis.Analyzer;
import analysis.DecryptGuess;
import java.util.ArrayList;
import java.util.List;
import utils.TextHelper;

/**
 * Atbash cipher
 *
 * @see <a href="https://en.wikipedia.org/wiki/Atbash">Atbash Wikipedia</a>
 * @author luki
 */
public class Atbash extends Cipher {

    private static final String NAME = "ATBASH";
    private String cipher_;

    /**
     * Constructor for the Atbash cipher
     *
     * The cipher is calculated here
     *
     * @param alphabet alphabet_ from the language that is going to be used
     */
    public Atbash(String alphabet) {
        super(alphabet);
        StringBuilder rev = new StringBuilder(alphabet);
        cipher_ = rev.reverse().toString();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String encrypt(String text, Key key) {
        StringBuilder sb = new StringBuilder();
        text = TextHelper.normalize(text);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (alphabet_.indexOf(ch) != -1) {
                sb.append(cipher_.charAt(alphabet_.indexOf(ch)));
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, Key key) {
        return encrypt(text, key);
    }

    @Override
    public List<DecryptGuess> decryptWithoutKey(String encryptedText, Analyzer analyzer, DecryptGuess decryptGuess) {
        List<DecryptGuess> guesses = new ArrayList<>();
        Key key = new Key();
        String decryptedText = decrypt(encryptedText, key);
        double error = analyzer.analyze(decryptedText);

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
