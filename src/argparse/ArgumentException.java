package argparse;

/**
 * This exception is thrown when an error is encountered while parsing
 *
 * @author luki
 */
public class ArgumentException extends Exception {
    /**
     * Empty constructor
     */
    public ArgumentException() {
        super();
    }
    
    /**
     * Constructor with a message
     * @param msg exception message
     */
    public ArgumentException(String msg) {
        super(msg);
    }    
}
