package utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Helper class for String related stuff
 * 
 * @author luki
 */
public class TextHelper {

    /**
     * Removes accents from text and converts it to upper case
     * 
     * @param text text to normalize
     * @return normalized text
     */
    public static String normalize(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("[^\\p{ASCII}]", "");
        text = text.toUpperCase();

        return text;
    }

    /**
     * Returns true if str can be converted to Integer, false otherwise
     *
     * @param str string to be checked
     * @return true if can be converted to Integer, false otherwise
     */
    public static boolean checkInteger(String str) {
        // Code from: https://stackoverflow.com/a/15801999/13313449
        return str.matches("[+-]?(0|[1-9]\\d*)");
    }

    /**
     * Returns true if str can be converted to Double, false otherwise.
     *
     * @param str string to be checked
     * @return true if can be converted to Double, false otherwise
     */
    public static boolean checkDouble(String str) {
        // Code from: http://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#valueOf-java.lang.String-

        final String Digits = "(\\p{Digit}+)";
        final String HexDigits = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp = "[eE][+-]?" + Digits;
        final String fpRegex
                = ("[\\x00-\\x20]*"
                + // Optional leading "whitespace"
                "[+-]?("
                + // Optional sign character
                "NaN|"
                + // "NaN" string
                "Infinity|"
                + // "Infinity" string
                // A decimal floating-point string representing a finite positive
                // number without a leading sign has at most five basic pieces:
                // Digits . Digits ExponentPart FloatTypeSuffix
                //
                // Since this method allows integer-only strings as input
                // in addition to strings of floating-point literals, the
                // two sub-patterns below are simplifications of the grammar
                // productions from section 3.10.2 of
                // The Java Language Specification.
                // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|"
                + // . Digits ExponentPart_opt FloatTypeSuffix_opt
                "(\\.(" + Digits + ")(" + Exp + ")?)|"
                + // Hexadecimal strings
                "(("
                + // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                "(0[xX]" + HexDigits + "(\\.)?)|"
                + // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")"
                + ")[pP][+-]?" + Digits + "))"
                + "[fFdD]?))"
                + "[\\x00-\\x20]*");// Optional trailing "whitespace"

        return Pattern.matches(fpRegex, str);
    }

    /**
     * Returns a string of length n padded with spaces on the right side
     *
     * @param str string to pad
     * @param n length of the output string
     * @return padded string
     */
    public static String padRight(String str, int n) {
        // Code from: https://stackoverflow.com/a/391978/13313449
        return String.format("%-" + n + "s", str);
    }

    /**
     * Returns a string of length n padded with spaces on the left side
     *
     * @param str string to pad
     * @param n length of the output string
     * @return padded string
     */
    public static String padLeft(String str, int n) {
        // Code from: https://stackoverflow.com/a/391978/13313449
        
        return String.format("%" + n + "s", str);
    }
}
