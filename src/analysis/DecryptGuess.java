package analysis;

import ciphers.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class holds a step in the decryption process
 * </p>
 * <p>
 * The idea is to have a list of {@code DecryptGuess} each decrypted with a
 * different cipher and key, then sort this list by error_ with the lower one
 * first. The error_ is given by an {@link Analyzer}.
 * </p>
 *
 * @author luki
 */
public class DecryptGuess implements Comparable<DecryptGuess> {

    private List<String> cipherNames_;
    private List<Key> keys_;
    private Double error_;
    private String decryptedText_;

    /**
     * Constructor for DecryptGuess
     *
     * @param cipher cipher name
     * @param key key used
     * @param error error of the decrypted text
     * @param decryptedText decrypted text using the given key
     */
    public DecryptGuess(String cipher, Key key, Double error, String decryptedText) {
        cipherNames_ = new ArrayList<>();
        cipherNames_.add(cipher);

        keys_ = new ArrayList<>();
        keys_.add(key);

        error_ = error;
        decryptedText_ = decryptedText;
    }

    /**
     * Adds a new step to the decryption process, in case the text is encrypted
     * multiple times with various ciphers
     *
     * @param cipher new cipher name
     * @param key new key used
     * @param error new error from the combined cipher
     * @param decryptedText new decrypted text
     * @return this, in case multiple steps need to be added as a one-liner
     */
    public DecryptGuess addStep(String cipher, Key key, Double error, String decryptedText) {
        cipherNames_.add(cipher);
        keys_.add(key);
        error_ = error;
        decryptedText_ = decryptedText;

        return this;
    }

    @Override
    public int compareTo(DecryptGuess t) {
        return error_.compareTo(t.error_);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cipherNames_.size(); i++) {
            String keyString = keys_.get(i).getString();
            if (keyString.equals(Key.NONE)) {
                sb.append(String.format("[%s]", cipherNames_.get(i)));
            } else {
                sb.append(String.format("[%s %s]", cipherNames_.get(i), keyString));
            }
        }

        sb.append(String.format(" \tError: %8.2f;    %s", error_, decryptedText_));

        return sb.toString();
    }
}
