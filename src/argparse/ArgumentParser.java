package argparse;

import java.util.Map;

/**
 * <p>
 * This interface defines how the parser should work.
 * </p>
 * <p>
 * To use it add arguments with {@link #addArgument(String...)} and then call
 * {@link #parseArgs(String[])} to parse the arguments, this method returns a
 * {@link Namespace} with all the arguments.
 * </p>
 *
 * @author luki
 */
public interface ArgumentParser {
    
    public final static String HELP_EXCEPTION = "HELP";

    /**
     * Creates a new Argument and adds it to this parser and returns it.
     *
     * The first flag on the list will be the argument name
     *
     * @param flags List of flags for the new argument
     * @return Argument object
     */
    Argument addArgument(String... flags);

    /**
     * Parses arguments from the command line and returns them as
     * {@link Namespace} object
     *
     * @param args Arguments from the command line
     * @return {@link Namespace} containing arguments
     * @throws ArgumentException if some error occurs while parsing
     */
    Namespace parseArgs(String[] args) throws ArgumentException;

    /**
     * Parses arguments from the command line and stores them in
     * {@link Namespace} object
     *
     * @param args Arguments from the command line
     * @param attr {@link Namespace} to store the attributes
     * @throws ArgumentException if some error occurs while parsing
     */
    void parseArgs(String[] args, Map<String, Object> attr) throws ArgumentException;

    // Usage and helpers    ####################################################
    /**
     * Prints help
     *
     */
    void printHelp();

    /**
     * Sets the text at the usage line
     *
     * @param text usage text
     * @return this
     */
    ArgumentParser usage(String text);

    /**
     * Prints the usage
     *
     */
    void printUsage();

    /**
     * Sets the description text
     *
     * @param text description text
     * @return this
     */
    ArgumentParser description(String text);

    /**
     * Prints the description
     *
     */
    void printDescription();

    /**
     * Sets the text to be printed at the end
     *
     * @param text epilog text
     * @return this
     */
    ArgumentParser epilog(String text);

    /**
     * Prints the epilog
     *
     */
    void printEpilog();
}
