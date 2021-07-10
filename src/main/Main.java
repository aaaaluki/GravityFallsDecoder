package main;

import utils.Config;
import utils.ExitCodes;
import utils.IO;

/**
 *
 * @author luki
 */
public class Main {

    public static void main(String[] args) {
        boolean debug = true;
        boolean verbose = true;
        boolean warn = true;
        String language = "ENG";
        String headerFile = "header.txt";

        IO io = new IO(debug, verbose, warn);
        io.printHeader(headerFile);

        // Load Configuration
        Config conf = new Config(io, language);
        int returnCode = conf.loadConfig();
        io.debug(String.format("Load Configuration exit(%d)", returnCode));
        if (returnCode != ExitCodes.OK) {
            System.exit(1);
        }

        // TODO ...
        io.todo("Everything!");

        System.exit(0);
    }
}
