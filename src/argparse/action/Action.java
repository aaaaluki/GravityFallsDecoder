package argparse.action;

import java.util.Map;
import argparse.Argument;
import argparse.ArgumentException;

/**
 * This object is called when an argument is found on the command line, the
 * actual parsing is made in each implementation of this interface
 *
 * @author luki
 */
public interface Action {

    /**
     * <p>
     * This runs when the Argument arg is found in the command line
     * </p>
     * <p>
     * Here is where the actual parsing is being made
     * </p>
     *
     * @param args a String array of arguments from the command line used in
     * Argument
     * @param arg Argument found in the command line
     * @param attr map where the parsed arguments are stored
     * @param choices possible values for arg
     * @param value value of arg, needed to set
     * @throws ArgumentException if there is some error parsing throw
     * {@code ArgumentException}
     */
    void run(String[] args, Argument arg, Map<String, Object> attr, Object choices, Object value) throws ArgumentException;
}
