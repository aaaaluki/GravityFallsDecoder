package analysis;

import ciphers.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    private final Set<Cipher> ciphers_;

    /**
     * Decrypter constructor, initializes the analysisTools attribute putting
     * all the implemented ciphers and their respective analysis tool.
     *
     * @param conf Config object
     * @param ciphers Set of available/implemented ciphers
     */
    public Decrypter(Config conf, Set<Cipher> ciphers) {
        ciphers_ = ciphers;
    }

    /**
     * Receives an encrypted text and tries to decrypt it using the implemented
     * ciphers, then returns a list of {@link DecryptGuess} in ascending error,
     * lower first.
     *
     * @param encryptedText text to decrypt
     * @return list of {@link DecryptGuess} in ascending order
     */
    public List<DecryptGuess> decrypt(String encryptedText) {
        List<DecryptGuess> finalGuesses = new ArrayList<>();

        // First round of deciphering
        for (Cipher cipher : ciphers_) {
            finalGuesses.addAll(cipher.decryptWithoutKey(encryptedText, null));
        }

        int maxDepth = Math.min(ciphers_.size(), Integer.MAX_VALUE) - 1;
        for (int i = 0; i < maxDepth; i++) {
            List<DecryptGuess> clone = cloneList(finalGuesses);
            List<DecryptGuess> toRemove = new ArrayList<>();

            for (Cipher cipher : ciphers_) {
                List<DecryptGuess> guessesToAdd = new ArrayList<>();

                for (int j = 0; j < clone.size(); j++) {
                    DecryptGuess dg = clone.get(j);
                    if (dg.getError().equals(Double.POSITIVE_INFINITY)) {
                        toRemove.add(finalGuesses.get(j));
                    }

                    if (dg.getCipherNames().contains(cipher.getName())) {
                        continue;
                    }

                    guessesToAdd.addAll(cipher.decryptWithoutKey(dg.getDecryptedText(), dg.clone()));
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
