package ciphers;

import java.text.Normalizer;
import utils.Color;
import utils.IO;

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
    
    public void test(String sample, int key) {
        String encrypted = encrypt(sample, key);
        String decrypted = decrypt(encrypted, key);

        IO.print(String.format("Cipher: %s", getName()));
        IO.print(String.format("Original:  %s", sample));
        IO.print(String.format("Encrypted: %s", encrypted));
        IO.print(String.format("Decrypted: %s", decrypted));
        IO.print(String.format("Match: %B", sample.equals(decrypted)));
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
