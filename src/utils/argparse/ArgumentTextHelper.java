package utils.argparse;

/**
 * String handling helper class for the argument parser
 *
 * @author luki
 */
public class ArgumentTextHelper {

    public final static String PREFIX_CHARS = "-";

    /**
     * Removes the prefix and returns the string
     * 
     * @param flag flag that needs prefix removed
     * @return flag without prefix
     */
    public static String removePrefix(String flag) {
        StringBuilder sb = new StringBuilder(flag);

        while (sb.substring(0, PREFIX_CHARS.length()).equals(PREFIX_CHARS)) {
            sb.replace(0, PREFIX_CHARS.length(), "");
        }

        return sb.toString();
    }

    /**
     * Returns an empty String if str is {@code null}
     * 
     * @param str String that might be null
     * @return str if its not null, empty otherwise
     */
    public static String nonNull(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }
}
