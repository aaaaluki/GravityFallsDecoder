package analysis;

import ciphers.A1Z26;
import ciphers.Atbash;
import ciphers.Caesar;
import ciphers.Cipher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Config;

/**
 * This class manages all the decryption process, just create an instance and
 * pass the encrypted text to the {@link #decrypt(String)} method. This method
 * will return a list of sorted {@link DecryptGuess}, the first ones being the
 * most probable original text.
 * 
 * @author luki
 */
public class Decrypter {

    private final Config config_;
    private final Map<Cipher, Analyzer> analysisTools_;

    /**
     * Decrypter constructor, initializes the analysisTools attribute putting
     * all the implemented ciphers and their respective analysis tool.
     * 
     * @param conf Config object
     */
    public Decrypter(Config conf) {
        config_ = conf;

        Analyzer fa = new FrequencyAnalysis(conf);
        
        analysisTools_ = new HashMap<>();
        analysisTools_.put(new Caesar(config_.getAlphabet()), fa);
        analysisTools_.put(new Atbash(config_.getAlphabet()), fa);
        analysisTools_.put(new A1Z26(config_.getAlphabet()), fa);
    }
    
    /**
     * Receives an encrypted text and tries to decrypt it using the implemented
     * ciphers, then returns a list of {@link DecryptGuess} ascending error,
     * lower first.
     * 
     * @param encryptedText text to decrypt
     * @return list of {@link DecryptGuess} in ascending order
     */
    public List<DecryptGuess> decrypt(String encryptedText) {
        List<DecryptGuess> decryptGuesses = new ArrayList<>();
        
        for (Cipher cipher : analysisTools_.keySet()) {
            decryptGuesses.addAll(cipher.decryptWithoutKey(encryptedText, analysisTools_.get(cipher)));
        }

        Collections.sort(decryptGuesses);
        return decryptGuesses;
    }
}
