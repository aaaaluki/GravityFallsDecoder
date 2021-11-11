package ciphers;

import analysis.DecryptGuess;
import java.util.List;
import utils.Colour;
import utils.IO;
import utils.TextHelper;

/**
 * Cipher abstract class
 *
 * Has some utilities for helping in each cipher implementation
 *
 * @author luki
 */
public abstract class Cipher {

    protected static final String ERR_NULL_KEY = "The key cannot be null";
    /**
     * alphabet_ of the language being used
     */
    protected String alphabet_;

    /**
     * Constructor for a cipher
     *
     * @param alphabet alphabet_ from the language that is going to be used
     */
    public Cipher(String alphabet) {
        this.alphabet_ = alphabet;
    }

    /**
     * Method used for testing ciphers
     *
     * This method encrypts {@code sample} and then decrypts it. Then compares
     * it with the normalized sample and checks if it's equal. This is done to
     * check that the cipher functions properly.
     *
     * @param sample sample text to use for test
     * @param key key to use for test
     */
    public void test(String sample, Key key) {
        sample = TextHelper.normalize(sample);
        String encrypted = encrypt(sample, key);
        String decrypted = decrypt(encrypted, key);

        IO.print("Cipher: ");
        IO.println(String.format("[%s]", getName()), Colour.PURPLE_BOLD);
        IO.println(String.format("Original:  %s", sample));
        IO.println(String.format("Encrypted: %s", encrypted));
        IO.println(String.format("Decrypted: %s", decrypted));
        IO.println(String.format("Match: %B", sample.equals(decrypted)));
    }
    
    /**
     * Getter for NAME
     *
     * @return NAME
     */
    public abstract String getName();
    
    /**
     * Returns the class of the key used for the cipher, or {@code null} if the
     * cipher doesn't need a key
     * 
     * @return The class or {@code null}
     */
    public abstract Class getKeyClass();
    
    /**
     * Returns {@code null} if the key is valid for encryption ad decryption,
     * and returns an error message if the key is not valid.
     * 
     * @param key key to validate
     * @return null if valid, otherwise error message
     */
    public abstract String validateKey(Key key);
    
    /**
     * Encrypts the given text with the given key
     *
     * @param text text to encrypt
     * @param key key to use during encryption, can be {@code null} in some
     * ciphers
     * @return encrypted text
     */
    public abstract String encrypt(String text, Key key);

    /**
     * Decrypts the given text with the given key
     *
     * @param text text to decrypt
     * @param key key to use during decryption, can be {@code null} in some
     * ciphers
     * @return decrypted text
     */
    public abstract String decrypt(String text, Key key);

    /**
     * Receives an encrypted text and tries to decrypt it without knowing the
     * key, then returns a list of {@link DecryptGuess} ascending error, lower
     * first.
     *
     * @param encryptedText text to decrypt
     * @param decryptGuess previous step on decoding, can be null if it's the first time
     * @return list of decrypt guesses
     */
    public abstract List<DecryptGuess> decryptWithoutKey(String encryptedText, DecryptGuess decryptGuess);
}
