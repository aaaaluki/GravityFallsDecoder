package main;

import argparse.Argument.Type;
import argparse.ArgumentException;
import argparse.ArgumentParser;
import argparse.ArgumentParserImpl;
import utils.Namespace;
import utils.Config;
import utils.ExitCodes;
import utils.IO;

/**
 *
 * @author luki
 */
public class UserInterface {

    // help                                                            --help, -h
    private static final boolean DEBUG_DEFAULT = false;             // --debug, -d
    private static final boolean VERBOSE_DEFAULT = false;           // --verbose, v
    private static final boolean NO_COLOUR_DEFAULT = false;         // --no-colour, -nc
    private static final String LANGUAGE_DEFAULT = "ENG";           // --language, -l
             
    private static final Integer GUESSES_DEFAULT = 5;               // --guesses, -g
    private static final String FILES_DEFAULT = null;               // --files, -f
    private static final String EXTENSION_DEFAULT = "_decoded.txt"; // --extension, -e

    private static final String HEADER_FILE = "header.txt";
    private static final String DESCRIPTION = "Placeholder for description";
    private static final String EPILOG = "Placeholder for epilog";

    private final ArgumentParser ap_;
    private Controller controller_;

    /**
     * UserInterface constructor, the {@link ArgumentParser} is instantiated
     * here and all the arguments are added
     */
    public UserInterface() {
        // This argument parser is HEAVILY inspired in: (Java) https://argparse4j.github.io/
        //                                              (Python) https://docs.python.org/3/library/argparse.html
        ap_ = new ArgumentParserImpl("Gravity Falls").description(DESCRIPTION).epilog(EPILOG);
        ap_.addArgument("--debug", "-d").setHelp("Sets debug mode").setDefault(DEBUG_DEFAULT).setType(Type.BOOLEAN);
        ap_.addArgument("--verbose", "-v").setHelp("Sets verbose mode").setDefault(VERBOSE_DEFAULT).setType(Type.BOOLEAN);
        ap_.addArgument("--no-colour", "-nc").setHelp("Disable colour output").setDefault(NO_COLOUR_DEFAULT).setType(Type.BOOLEAN);
        ap_.addArgument("--language", "-l").nargs(1).setHelp("Sets the language").setDefault(LANGUAGE_DEFAULT).setType(Type.STRING);

        ap_.addArgument("--files", "-f").nargs("+").setHelp("Files to deciper").setDefault(FILES_DEFAULT).setType(Type.STRING).required();
        ap_.addArgument("--guesses",  "-g").nargs(1).setHelp("Sets the number of decipher guesses to show/save").setDefault(GUESSES_DEFAULT).setType(Type.INTEGER);
        ap_.addArgument("--extension", "-e").nargs(1).setHelp("Sets the extension for the decoded files").setDefault(EXTENSION_DEFAULT).setType(Type.STRING);
    }

    /**
     * Main method
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        IO.init();
        IO.setColour(NO_COLOUR_DEFAULT);
        IO.setDebug(DEBUG_DEFAULT);
        IO.setVerbose(VERBOSE_DEFAULT);

        UserInterface ui = new UserInterface();
        ui.start(args);
    }

    /**
     * <p>
     * This method does the initial configuration and then runs the controller
     * </p>
     * <p>
     * In this method the argument parser is created and then tries to parse the
     * arguments. Then sets all the flags and loads the configuration. Finally
     * creates a new {@link Controller} and runs it.
     * </p>
     *
     * @param args command line arguments
     */
    private void start(String[] args) {
        Namespace ns = null;
        try {
            ns = ap_.parseArgs(args);
        } catch (ArgumentException e) {
            // If it's not help, show exception message
            if (!e.getMessage().equals(ArgumentParser.HELP_EXCEPTION)) {
                IO.warn(e.getMessage());
            }

            ap_.printHelp();
            System.exit(1);
        }

        IO.setDebug(ns.getBoolean("debug"));
        IO.setVerbose(ns.getBoolean("verbose"));
        IO.setColour(ns.getBoolean("no-colour"));

        IO.printHeader(HEADER_FILE);

        // Load Configuration
        Config conf = new Config(ns);
        int returnCode = conf.loadConfig();
        IO.debug(String.format("Load Configuration exit(%d)", returnCode));
        if (returnCode != ExitCodes.OK) {
            System.exit(1);
        }

        controller_ = new Controller(conf);
        controller_.start();

        System.exit(0);
    }
}
