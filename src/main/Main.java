package main;

import ciphers.A1Z26;
import ciphers.Atbash;
import ciphers.Caesar;
import ciphers.Cipher;
import utils.Config;
import utils.ExitCodes;
import utils.IO;
import utils.argparse.Argument.Type;
import utils.argparse.ArgumentException;
import utils.argparse.ArgumentParser;
import utils.argparse.ArgumentParserImpl;
import utils.argparse.Namespace;

/**
 *
 * @author luki
 */
public class Main {

    public static void main(String[] args) {
        // help                                --help, -h
        boolean debugDefault = false;       // --debug, -d
        boolean verboseDefault = false;     // --verbose, v
        String languageDefault = "ENG";     // --language, -l
        String headerFile = "header.txt";

        IO.init();

        // This argument parser is HEAVILY inspired in: (Java) https://argparse4j.github.io/
        //                                              (Python) https://docs.python.org/3/library/argparse.html
        ArgumentParser ap = new ArgumentParserImpl("Gravity Falls");
        ap.addArgument("--debug", "-d").nargs(0).setHelp("Sets debug mode").setDefault(debugDefault).setType(Type.BOOLEAN);
        ap.addArgument("--verbose", "-v").nargs(0).setHelp("Sets verbose mode").setDefault(verboseDefault).setType(Type.BOOLEAN);
        ap.addArgument("--language", "-l").nargs(1).setHelp("Sets the language").setDefault(languageDefault).setType(Type.STRING);
        
        Namespace ns = null;
        try {
            ns = ap.parseArgs(args);
        } catch (ArgumentException e) {
            IO.warn(e.getMessage());
            ap.printHelp();
            System.exit(1);
        }
        
        IO.setDebug(ns.getBoolean("debug"));
        IO.setVerbose(ns.getBoolean("verbose"));
        
        IO.printHeader(headerFile);

        // Load Configuration
        Config conf = new Config(ns.getString("language"));
        int returnCode = conf.loadConfig();
        IO.debug(String.format("Load Configuration exit(%d)", returnCode));
        if (returnCode != ExitCodes.OK) {
            System.exit(1);
        }

        // TODO ...
        IO.todo("Everything!");

        // Testing ciphers
        // Caesar
        String sample = "ALICE'S ADVENTURES IN WONDERLAND";
        int key = 13;

        Cipher cipher = new Caesar(conf.getAlphabet());
        cipher.test(sample, key);
        
        // Atbash
        cipher = new Atbash(conf.getAlphabet());
        cipher.test(sample, key);
        
        // A1Z26
        cipher = new A1Z26(conf.getAlphabet());
        cipher.test(sample, key);
        
        System.exit(0);
    }
}
