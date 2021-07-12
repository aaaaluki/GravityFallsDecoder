package ciphers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author luki
 */
public class A1Z26 extends Cipher {

    static final String NAME = "CAESAR";
    static final String CHAR_SEPARATOR = "-";

    public A1Z26(String alphabet) {
        super(alphabet);
        super.NAME = NAME;
    }

    @Override
    public String encrypt(String text, int key) {
        StringBuilder sb = new StringBuilder();
        text = normalize(text);

        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            boolean first = true;

            for (int j = 0; j < word.length(); j++) {
                char ch = word.charAt(j);
                int idx = alphabet.indexOf(ch);

                if (idx != -1) {
                    // If it's a character in the alphabet
                    if (first) {
                        sb.append(idx + 1);
                        first = false;
                    } else {
                        sb.append(CHAR_SEPARATOR).append(idx + 1);
                    }

                } else {
                    // If it's not a character in the alphabet
                    sb.append(ch);
                    first = true;
                }
            }

            if (i != words.length - 1) sb.append(" ");
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, int key) {
        StringBuilder sb = new StringBuilder();
        text = normalize(text);
        Pattern pattern = Pattern.compile("\\d+|[^" + CHAR_SEPARATOR + "]");
        
        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            Matcher matcher = pattern.matcher(word);
            while (matcher.find()) {
                String match = matcher.group();
                if (match.matches("-?\\d+")) {
                    // Is a number
                    int idx = Integer.valueOf(match);
                    sb.append(alphabet.charAt(idx - 1));
                } else {
                    sb.append(match);
                }
            }
        
            if (i != words.length - 1) sb.append(" ");
        }

        return sb.toString();
    }
}