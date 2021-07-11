package main;

import ciphers.A1Z26;
import ciphers.Atbash;
import ciphers.Caesar;
import ciphers.Cipher;
import utils.Config;
import utils.ExitCodes;
import utils.IO;

/**
 *
 * @author luki
 */
public class Main {

    public static void main(String[] args) {
        // help                                --help, -h
        boolean debug = true;               // --debug, -d
        boolean verbose = false;            // --verbose, v
        String language = "ENG";            // --language, -l
        String headerFile = "header.txt";   // --header-file
        
        IO.init();
        IO.setDebug(debug);
        IO.setVerbose(verbose);
        
        IO.printHeader(headerFile);

        // Load Configuration
        Config conf = new Config(language);
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
