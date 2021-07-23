package ciphers;

import analysis.Analyzer;
import analysis.DecryptGuess;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.IO;
import utils.TextHelper;

/**
 * A1Z26 cipher, also known as numerical substitution.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Substitution_cipher">Substitution
 * Cipher Wikipedia</a>
 * @author luki
 */
public class A1Z26 extends Cipher {

    private static final String NAME = "A1Z26";
    private static final String CHAR_SEPARATOR = "-";

    /**
     * Constructor for the A1Z26 cipher
     *
     * @param alphabet alphabet_ from the language that is going to be used
     */
    public A1Z26(String alphabet) {
        super(alphabet);
    }
    
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String encrypt(String text, Key key) {
        StringBuilder sb = new StringBuilder();
        text = TextHelper.normalize(text);

        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            boolean first = true;

            for (int j = 0; j < word.length(); j++) {
                char ch = word.charAt(j);
                int idx = alphabet_.indexOf(ch);

                if (idx != -1) {
                    // If it's a character in the alphabet_
                    if (first) {
                        sb.append(idx + 1);
                        first = false;
                    } else {
                        sb.append(CHAR_SEPARATOR).append(idx + 1);
                    }

                } else {
                    // If it's not a character in the alphabet_
                    sb.append(ch);
                    first = true;
                }
            }

            if (i != words.length - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, Key key) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("\\d+|[^" + CHAR_SEPARATOR + "]");

        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            Matcher matcher = pattern.matcher(word);
            while (matcher.find()) {
                String match = matcher.group();
                if (match.matches("-?\\d+")) {
                    // Is a number
                    try {
                        int idx = Integer.valueOf(match);
                        if (idx > alphabet_.length()) {
                            sb.append(match);
                        } else {
                            sb.append(alphabet_.charAt(idx - 1));
                        }

                    } catch (NumberFormatException e) {
                        IO.debug(String.format("Error while parsing %s", match));
                        sb.append(match);
                    }
                } else {
                    sb.append(match);
                }
            }

            if (i != words.length - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
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
