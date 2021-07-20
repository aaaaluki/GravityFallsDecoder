package analysis;

import java.util.HashMap;
import java.util.Map;
import utils.Config;
import utils.TextHelper;

/**
 * This class uses Frequency Analysis to help breaking some simple substitution
 * ciphers
 *
 * @see <a href="https://en.wikipedia.org/wiki/Frequency_analysis">Frequency
 * Analysis Wikipedia</a>
 * @author luki
 */
public class FrequencyAnalysis implements Analyzer {

    /**
     * Name of the Analyzer
     */
    private static final String NAME = "Frequency Analysis";

    private final String alphabet_;
    private final Map<String, Double> freqsMono_;

    /**
     * Constructor for a Frequency Analysis object, here the alphabet and the
     * letter frequencies are saved in the attributes.
     *
     * @param conf Config object
     */
    public FrequencyAnalysis(Config conf) {
        alphabet_ = conf.getAlphabet();
        freqsMono_ = conf.getMonoFrequencies();
    }

    /**
     * Performs a letter frequency analysis on the given String and returns the
     * "error", a way to measure how much of the language is the given text.
     * This error calculation is done in the {@link #calculateError(Map, int)}
     * method.
     *
     * @param text text to analyze
     * @return the error
     */
    @Override
    public double analyze(String text) {
        Map<String, Double> analyzedFreqs = new HashMap<>();
        text = TextHelper.normalize(text);

        String chr;
        int N = 0;
        for (int i = 0; i < text.length(); i++) {
            chr = String.valueOf(text.charAt(i));

            if (!alphabet_.contains(chr)) {
                continue;
            }

            Double val = analyzedFreqs.get(chr);
            if (val == null) {
                analyzedFreqs.put(chr, 1.0);
            } else {
                analyzedFreqs.put(chr, val + 1);
            }

            N++;
        }

        return calculateError(analyzedFreqs, N);
    }

    /**
     * Returns the euclidean distance squared between the analyzed frequencies
     * and the theoretical frequencies
     *
     * @param analyzedFreqs analyzed frequencies
     * @param N total number of characters counted
     * @return euclidean distance (error)
     */
    public Double calculateError(Map<String, Double> analyzedFreqs, int N) {
        double error = Double.POSITIVE_INFINITY;
        if (N == 0) {
            // Division by zero, return +Infinity
            return error;
        }

        error = 0;

        for (String key : freqsMono_.keySet()) {
            Double value = analyzedFreqs.get(key);
            if (value == null) {
                value = 0.0;
            }

            error += Math.pow(freqsMono_.get(key) - 100 * value / N, 2);
        }

        return error;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
