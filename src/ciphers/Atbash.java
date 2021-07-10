package ciphers;

/**
 *
 * @author luki
 */
public class Atbash extends Cipher {

    static final String NAME = "ATBASH";
    String cipher;

    public Atbash(String alphabet) {
        super(alphabet);
        super.NAME = NAME;
        StringBuilder rev = new StringBuilder(alphabet);
        cipher = rev.reverse().toString();
    }

    @Override
    public String encrypt(String text, int key) {
        // This caesar cipher uses right rotation
        StringBuilder sb = new StringBuilder();

        text = normalize(text);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (alphabet.indexOf(ch) != -1) {
                sb.append(cipher.charAt(alphabet.indexOf(ch)));
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    @Override
    public String decrypt(String text, int key) {
        return encrypt(text, key);
    }
}
