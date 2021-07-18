package main;

import ciphers.A1Z26;
import ciphers.Atbash;
import ciphers.Caesar;
import ciphers.Cipher;
import utils.Config;
import utils.IO;

/**
 *
 * @author luki
 */
public class Controller {
    private final Config conf_;
    
    /**
     * Controller constructor
     * 
     * @param conf {@link Config} object
     */
    public Controller(Config conf) {
        conf_ = conf;
    }
    
    public void start() {
        // TODO ...
        IO.todo("Everything!");

        // Testing ciphers
        // Caesar
        String sample = "ALICE'S ADVENTURES IN WONDERLAND";
        int key = 13;

        Cipher cipher = new Caesar(conf_.getAlphabet());
        cipher.test(sample, key);

        // Atbash
        cipher = new Atbash(conf_.getAlphabet());
        cipher.test(sample, key);

        // A1Z26
        cipher = new A1Z26(conf_.getAlphabet());
        cipher.test(sample, key);
    }
}
