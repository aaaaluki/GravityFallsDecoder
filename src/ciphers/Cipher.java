package ciphers;

import analysis.Analyzer;
import analysis.DecryptGuess;
import java.util.List;
import utils.Color;
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

    /**
     * Name of the cipher
     */
    protected static String NAME;
    
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
     * Getter for NAME
     * 
     * @return NAME
     */
    public String getName() {
        return NAME;
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
        IO.print(String.format("[%s]\n", getName()), Color.PURPLE_BOLD);
        IO.print(String.format("Original:  %s\n", sample));
        IO.print(String.format("Encrypted: %s\n", encrypted));
        IO.print(String.format("Decrypted: %s\n", decrypted));
        IO.print(String.format("Match: %B\n\n", sample.equals(decrypted)));
    }

    /**
     * Encrypts the given text with the given key
     * 
     * @param text text to encrypt
     * @param key key to use during encryption, can be {@code null} in some ciphers
     * @return encrypted text
     */
    public abstract String encrypt(String text, Key key);

    /**
     * Decrypts the given text with the given key
     * 
     * @param text text to decrypt
     * @param key key to use during decryption, can be {@code null} in some ciphers
     * @return decrypted text
     */
    public abstract String decrypt(String text, Key key);

    /**
     * Receives an encrypted text and tries to decrypt it without knowing the key,
     * then returns a list of {@link DecryptGuess} ascending error, lower first.
     * 
     * @param encryptedText text to decrypt
     * @param analyzer analyzer tool
     * @return list of decrypt guesses
     */
    public abstract List<DecryptGuess> decryptWithoutKey(String encryptedText, Analyzer analyzer);
}
