package ciphers;

import utils.TextHelper;

/**
 * Atbash cipher
 * https://en.wikipedia.org/wiki/Atbash
 *
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
        super.NAME = NAME;
        StringBuilder rev = new StringBuilder(alphabet);
        cipher_ = rev.reverse().toString();
    }

    @Override
    public String encrypt(String text, Key key) {
        // This caesar cipher uses right rotation
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
}
