package analysis;

import ciphers.*;
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
        analysisTools_.put(new A1Z26(config_.get("lang.alphabet")), fa);
        analysisTools_.put(new Atbash(config_.get("lang.alphabet")), fa);
        analysisTools_.put(new Binary(config_.get("lang.alphabet")), fa);
        analysisTools_.put(new Caesar(config_.get("lang.alphabet")), fa);
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
        List<DecryptGuess> finalGuesses = new ArrayList<>();

        for (Cipher cipher : analysisTools_.keySet()) {
            finalGuesses.addAll(cipher.decryptWithoutKey(encryptedText, analysisTools_.get(cipher), null));
        }

        int maxDepth = Math.min(analysisTools_.size(), Integer.MAX_VALUE) - 1;
        for (int i = 0; i < maxDepth; i++) {
            List<DecryptGuess> clone = cloneList(finalGuesses);
            List<DecryptGuess> toRemove = new ArrayList<>();

            for (Cipher cipher : analysisTools_.keySet()) {
                List<DecryptGuess> guessesToAdd = new ArrayList<>();

                for (int j = 0; j < clone.size(); j++) {
                    DecryptGuess dg = clone.get(j);
                    if (dg.getError().equals(Double.POSITIVE_INFINITY)) {
                        toRemove.add(finalGuesses.get(j));
                    }

                    if (dg.getCipherNames().contains(cipher.getName())) {
                        continue;
                    }

                    guessesToAdd.addAll(cipher.decryptWithoutKey(dg.getDecryptedText(), analysisTools_.get(cipher), dg.clone()));
                }

                finalGuesses.addAll(guessesToAdd);
            }

            finalGuesses.removeAll(toRemove);
        }

        // Remove duplicates from finalGuesses
        List<DecryptGuess> actualFinalGuesses = new ArrayList<>();
        for (DecryptGuess dg : finalGuesses) {
            if (!actualFinalGuesses.contains(dg)) {
                actualFinalGuesses.add(dg);
            }
        }

        Collections.sort(actualFinalGuesses);
        return actualFinalGuesses;
    }

    /**
     * Clones a list of {@link DecryptGuess}
     *
     * @param list list to clone
     * @return clone of list
     */
    private List<DecryptGuess> cloneList(List<DecryptGuess> list) {
        List<DecryptGuess> clone = new ArrayList<>();
        for (DecryptGuess dg : list) {
            clone.add(dg.clone());
        }

        return clone;
    }
}
