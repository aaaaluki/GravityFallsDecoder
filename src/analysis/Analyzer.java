package analysis;

/**
 * Analysis tool used for breaking the ciphers, each {@link ciphers.Cipher} will
 * need a different tool.
 *
 * @author luki
 */
public interface Analyzer {

    /**
     * This method gets some text and outputs a number, the lower the number the
     * higher the probability that this text is from the language specified in
     * {@link utils.Config}.
     *
     * @param text text to analyze
     * @return returns a double, the larger the number the less probable the
     * text is the language defined in conf
     */
    public double analyze(String text);

    /**
     * Getter for the analyzer name
     *
     * @return name of the analyzer
     */
    public String getName();
}
