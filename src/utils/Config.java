package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class has all the configuration parameters.
 * To load the parameters the loadConfig() method is used.
 * 
 * @author luki
 */
public class Config {
    /**
     * Folder were the configuration files are stored
     */
    public static String configFolder = "config";
    
    private String language;
    private String alphabet;
    private Map<String, Double> frequencies_mono;

    /**
     *Constructor for config
     * 
     * @param language this will select which config file to load.
     */
    public Config(String language) {
        this.language = language;
        alphabet = null;
        frequencies_mono = null;
    }

    /**
     * Language getter
     *
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Alphabet getter
     *
     * @return alphabet
     */
    public String getAlphabet() {
        return alphabet;
    }

    /**
     * Monogram frequencies getter
     *
     * @return frequencies
     */
    public Map<String, Double> getMonoFrequencies() {
        return frequencies_mono;
    }

    /**
     * Loads the alphabet and frequencies form the lang.config file. If the
     * configuration file is not valid returns.
     *
     * @return Returns an {@link utils.ExitCodes}, returns OK if no problems where found
     */
    public int loadConfig() {
        File configFile = new File(String.format("%s/%s.config", configFolder, language));
        int id = IO.openReadFile(configFile);

        IO.debug("Config File: " + configFile.getAbsolutePath());

        if (id == -1) {
            // No need to close file here because it cannot be opened
            return ExitCodes.FILE_NOT_FOUND;
        }

        String line = IO.readLineFile(id);
        int lineCount = 1;
        while (line != null) {
            if (line.startsWith("#")) {
                // ignore comments
                line = IO.readLineFile(id);
                lineCount++;
                continue;
            }

            String[] args = line.split(": ");

            if (args.length != 2) {
                IO.warn(String.format("(%s:%d) Does not have a <Key: Value> pair!", configFile.getName(), lineCount));
                IO.closeReadFile(id);
                return ExitCodes.ERROR_READING_FILE;
            }

            switch (args[0]) {
                case "language":
                    if (!language.equals(args[1])) {
                        IO.warn(String.format("Filename does not match languague in file %s", configFile.getName()));
                        IO.closeReadFile(id);
                        return ExitCodes.ERROR_READING_FILE;
                    }
                    break;

                case "alphabet":
                    alphabet = args[1];
                    break;

                case "frequencies_mono":
                    frequencies_mono = new HashMap<>();

                    int argCount = 1;
                    for (String kvPair : args[1].split(";")) {
                        String[] kv = kvPair.split("=");
                        if (kv.length != 2) {
                            IO.warn(String.format("(%s:%d) <Key=Value> pair %d can not be split!", configFile.getName(), lineCount, argCount));
                            IO.closeReadFile(id);
                            return ExitCodes.ERROR_READING_FILE;
                        }

                        if (checkDouble(kv[1])) {
                            frequencies_mono.put(kv[0], Double.valueOf(kv[1]));
                        } else {
                            IO.warn(String.format("(%s:%d) <Key=Value> pair %s=%s Value is not a number!", configFile.getName(), lineCount, kv[0], kv[1]));
                            IO.closeReadFile(id);
                            return ExitCodes.ERROR_READING_FILE;
                        }

                        argCount++;
                    }
                    break;

                default:
                    IO.warn(String.format("(%s:%d) Key %s is not a valid one!", configFile.getName(), lineCount, args[0]));
                    IO.closeReadFile(id);
                    return ExitCodes.ERROR_READING_FILE;
            }

            line = IO.readLineFile(id);
            lineCount++;
        }

        // Check args
        if (alphabet == null || frequencies_mono == null) {
            if (alphabet == null) {
                IO.warn(String.format("alphabet not defined on the config file: %s", configFile.getName()));
            }
            if (frequencies_mono == null) {
                IO.warn(String.format("frequencies_mono not defined on the config file: %s", configFile.getName()));
            }
            IO.closeReadFile(id);

            return ExitCodes.ERROR_READING_FILE;
        }

        IO.closeReadFile(id);

        return ExitCodes.OK;
    }

    /**
     * Returns true if str can be converted to Double, false otherwise.
     *
     * @param str string to be checked
     * @return true if can be converted to Double, false otherwise
     */
    private boolean checkDouble(String str) {
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
}
