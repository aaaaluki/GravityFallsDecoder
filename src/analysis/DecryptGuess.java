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
public class DecryptGuess implements Cloneable, Comparable<DecryptGuess> {

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
     * Private constructor used for cloning this object
     *
     * @param cipherNames cipher names
     * @param keys keys
     * @param error error
     * @param decryptedText decrypted text
     */
    private DecryptGuess(List<String> cipherNames, List<Key> keys, Double error, String decryptedText) {
        cipherNames_ = cipherNames;
        keys_ = keys;
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

    /**
     * Getter for cipherNames_
     *
     * @return cipherNames_
     */
    public List<String> getCipherNames() {
        return cipherNames_;
    }

    /**
     * Getter for error_
     *
     * @return error_
     */
    public Double getError() {
        return error_;
    }

    /**
     * Getter for decryptedText_
     *
     * @return decryptedText_
     */
    public String getDecryptedText() {
        return decryptedText_;
    }

    @Override
    public DecryptGuess clone() {
        List<String> newNames = new ArrayList<>();
        for (String name : cipherNames_) {
            newNames.add(name);
        }

        List<Key> newKeys = new ArrayList<>();
        for (Key key : keys_) {
            newKeys.add(new Key(key.get()));
        }

        return new DecryptGuess(newNames, newKeys, error_, decryptedText_);
    }

    @Override
    public int compareTo(DecryptGuess t) {
        return error_.compareTo(t.error_);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DecryptGuess)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        DecryptGuess other = (DecryptGuess) obj;
        return error_.equals(other.error_) && decryptedText_.equals(other.decryptedText_);
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
