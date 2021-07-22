package argparse;

/**
 * This exception is thrown when an error is encountered while parsing
 *
 * @author luki
 */
public class ArgumentException extends Exception {
    /**
     * Constructor with a message
     * @param msg exception message
     */
    public ArgumentException(String msg) {
        super(msg);
    }    
}
