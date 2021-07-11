package ciphers;

import java.text.Normalizer;
import utils.Color;

/**
 *
 * @author luki
 */
public abstract class Cipher {

    protected static String NAME;
    protected String alphabet;
    
    public Cipher(String alphabet) {
        this.alphabet = alphabet;
    }

    public String getName() {
        return Color.PURPLE_BOLD + String.format("[%s]", NAME) + Color.RESET;
    }

    public abstract String encrypt(String text, int key);

    public abstract String decrypt(String text, int key);

    protected String normalize(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("[^\\p{ASCII}]", "");
        text = text.toUpperCase();

        return text;
    }
}
