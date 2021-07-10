package ciphers;

/**
 *
 * @author luki
 */
public class Atbash extends Cipher {

    String name = "ATBASH";

    public Atbash(String alphabet) {
        super(alphabet);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String encrypt(String text, int key) {
        // This caesar cipher uses right rotation
        StringBuilder sb = new StringBuilder();
        StringBuilder rev = new StringBuilder(alphabet);
        String cipher = rev.reverse().toString();

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
