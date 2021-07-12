package utils;

/**
 * Interface containing exit codes for some methods to use
 *
 * @author luki
 */
public interface ExitCodes {

    /**
     * No problems encountered during execution
     */
    public static int OK = 0;

    /**
     * File not found
     */
    public static int FILE_NOT_FOUND = 1;

    /**
     * Error reading file
     */
    public static int ERROR_READING_FILE = 2;
}
