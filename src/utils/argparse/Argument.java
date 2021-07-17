package utils.argparse;

import java.util.List;
import java.util.Map;

/**
 * This interface is a way to specify how the arguments should be parsed by
 * {@link ArgumentParser}
 *
 * @author luki
 */
public interface Argument {

    /**
     * Supported types to parse the arguments as
     */
    public static enum Type {
        BOOLEAN, STRING, INTEGER
    }

    /**
     * Sets the number of arguments to be parsed
     *
     * @param n number of arguments to parse
     * @return this
     */
    Argument nargs(int n);

    /**
     * <p>
     * Sets the number of arguments to be parsed
     * </p>
     * <p>
     * If {@code "?"} is given only one argument will be parsed as {@code T} if
     * possible
     * </p>
     * <p>
     * If {@code "*"} is given all arguments will be parsed as {@code List<T>}
     * (default usage)
     * </p>
     * <p>
     * If {@code "+"} is given all arguments will be parsed as {@code List<T>},
     * but at least one argument has to be given
     * </p>
     *
     * @param n number of arguments to parse
     * @return this
     */
    Argument nargs(String n);

    /**
     * <p>
     * Sets the setType which the argument should be parsed as
     * </p>
     * <p>
     * If type is {@code Type.BOOLEAN} it automatically calls
     * {@code this.nargs(0)}
     * </p>
     *
     * @param type setType to parse as
     * @return this
     */
    Argument setType(Type type);

    /**
     * Sets the default value for the argument
     *
     * @param value default value
     * @return this
     */
    Argument setDefault(Object value);

    /**
     * Sets the name of the argument
     *
     * @param dest Name of the argument
     * @return this
     */
    Argument setDest(String dest);

    /**
     * Sets values allowed for this argument
     *
     * @param <E> The setType of the values
     * @param values Values encountered in the command line
     * @return this
     */
    <E> Argument setChoices(E... values);

    /**
     * Small description of what the argument does
     *
     * @param help setHelp text
     * @return this
     */
    Argument setHelp(String help);

    /**
     * This is executed when the argument is encountered in the command line
     *
     * @param args remaining arguments to parse
     * @param attr a {@link Namespace} will wrap this
     * @return this
     * @throws ArgumentException if there is some error parsing throw
     * {@code ArgumentException}
     */
    Argument action(String[] args, Map<String, Object> attr) throws ArgumentException;

    // Getters  and setters ####################################################
    /**
     * Getter for maxNargs_
     *
     * @return max number of arguments to be parsed
     */
    int getMaxNargs();

    /**
     * Getter for minNargs_
     *
     * @return min number of arguments to be parsed
     */
    int getMinNargs();

    /**
     * Getter for dest_
     *
     * @return dest_
     */
    String getDest();

    /**
     * Getter for default value
     *
     * @return default value
     */
    Object getDefault();

    /**
     * Getter for help value
     *
     * @return help value
     */
    String getHelp();

    /**
     * Getter for the flags
     *
     * @return list of flags
     */
    List<String> getFlags();

    /**
     * Getter for the main flag, the first one given at creation
     *
     * @return main flag
     */
    String getMainFlag();

    /**
     * Getter for the shortest flag
     *
     * @return shortest flag
     */
    public String getShortFlag();

    /**
     * Getter for consumed value
     *
     * @return consumed value
     */
    boolean getConsumed();

    /**
     * Sets the argument as consumed
     *
     */
    void consumed();

}
