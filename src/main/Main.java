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
        boolean debug = false;
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

        // Testing ciphers
        // Caesar
        Cipher cipher = new Caesar(conf.getAlphabet());
        String sample = "ALICE'S ADVENTURES IN WONDERLAND";

        int key = 13;
        String encrypted = cipher.encrypt(sample, key);
        String decrypted = cipher.decrypt(encrypted, key);

        io.print(String.format("Cipher: %s", cipher.getName()));
        io.print(String.format("Original:  %s", sample));
        io.print(String.format("Encrypted: %s", encrypted));
        io.print(String.format("Decrypted: %s", decrypted));
        io.print(String.format("Match: %B\n", sample.equals(decrypted)));
        
        // Atbash
        cipher = new Atbash(conf.getAlphabet());
        encrypted = cipher.encrypt(sample, key);
        decrypted = cipher.decrypt(encrypted, key);

        io.print(String.format("Cipher: %s", cipher.getName()));
        io.print(String.format("Original:  %s", sample));
        io.print(String.format("Encrypted: %s", encrypted));
        io.print(String.format("Decrypted: %s", decrypted));
        io.print(String.format("Match: %B\n", sample.equals(decrypted)));
        
        // A1Z26
        cipher = new A1Z26(conf.getAlphabet());
        encrypted = cipher.encrypt(sample, key);
        decrypted = cipher.decrypt(encrypted, key);

        io.print(String.format("Cipher: %s", cipher.getName()));
        io.print(String.format("Original:  %s", sample));
        io.print(String.format("Encrypted: %s", encrypted));
        io.print(String.format("Decrypted: %s", decrypted));
        io.print(String.format("Match: %B", sample.equals(decrypted)));
        
        System.exit(0);
    }
}
