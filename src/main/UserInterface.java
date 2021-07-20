package main;

import argparse.Argument;
import argparse.ArgumentException;
import argparse.ArgumentParser;
import argparse.ArgumentParserImpl;
import argparse.Namespace;
import utils.Config;
import utils.ExitCodes;
import utils.IO;

/**
 *
 * @author luki
 */
public class UserInterface {

    // help                                                      --help, -h
    private static final boolean DEBUG_DEFAULT = false;       // --debug, -d
    private static final boolean VERBOSE_DEFAULT = false;     // --verbose, v
    private static final boolean COLOR_DEFAULT = true;        // --no-color, -nc
    private static final String LANGUAGE_DEFAULT = "ENG";     // --language, -l

    private static final String HEADER_FILE = "header.txt";
    private static final String DESCRIPTION = "Placeholder for description";
    private static final String EPILOG = "Placeholder for epilog";

    private Controller controller_;

    /**
     * Empty constructor, don't know what to put here yet
     */
    public UserInterface() {

    }

    /**
     * Main method
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        IO.init();
        IO.setColor(COLOR_DEFAULT);
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
        // This argument parser is HEAVILY inspired in: (Java) https://argparse4j.github.io/
        //                                              (Python) https://docs.python.org/3/library/argparse.html
        ArgumentParser ap = new ArgumentParserImpl("Gravity Falls").description(DESCRIPTION).epilog(EPILOG);
        ap.addArgument("--debug", "-d").setHelp("Sets debug mode").setDefault(DEBUG_DEFAULT).setType(Argument.Type.BOOLEAN);
        ap.addArgument("--verbose", "-v").setHelp("Sets verbose mode").setDefault(VERBOSE_DEFAULT).setType(Argument.Type.BOOLEAN);
        ap.addArgument("--no-color", "-nc").setHelp("Disable color output").setDefault(COLOR_DEFAULT).setType(Argument.Type.BOOLEAN);
        ap.addArgument("--language", "-l").nargs(1).setHelp("Sets the language").setDefault(LANGUAGE_DEFAULT).setType(Argument.Type.STRING);

        Namespace ns = null;
        try {
            ns = ap.parseArgs(args);
        } catch (ArgumentException e) {
            // If it's not help, show exception message
            if (!e.getMessage().equals(ArgumentParser.HELP_EXCEPTION)) {
                IO.warn(e.getMessage());
            }

            ap.printHelp();
            System.exit(1);
        }

        IO.setDebug(ns.getBoolean("debug"));
        IO.setVerbose(ns.getBoolean("verbose"));
        IO.setColor(ns.getBoolean("no-color"));

        IO.printHeader(HEADER_FILE);

        // Load Configuration
        Config conf = new Config(ns.getString("language"));
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
