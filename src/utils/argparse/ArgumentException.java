package utils.argparse;

/**
 * This exception is thrown when an error is encountered while parsing
 *
 * @author luki
 */
public class ArgumentException extends Exception {
    public ArgumentException() {
        super();
    }
    
    public ArgumentException(String msg) {
        super(msg);
    }    
}
