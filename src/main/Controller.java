package main;

import analysis.DecryptGuess;
import analysis.Decrypter;
import ciphers.A1Z26;
import ciphers.Atbash;
import ciphers.Caesar;
import ciphers.Cipher;
import ciphers.Key;
import java.util.ArrayList;
import java.util.List;
import utils.Colour;
import utils.Config;
import utils.IO;

/**
 * The Controller class runs the deciphering part of the project
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

    /**
     * Runs all the decryption code, will read from files and write the
     * decryption result on a new file.
     */
    public void start() {
        // I should learn to do proper test instead of doing these :)
        // Testing ciphers
        // Caesar
        String sample = "ALICE'S ADVENTURES IN WONDERLAND";
        Key key = new Key(13);

        Cipher cipher = new Caesar(conf_.getAlphabet());
        cipher.test(sample, key);

        // Atbash
        cipher = new Atbash(conf_.getAlphabet());
        cipher.test(sample, key);

        // A1Z26
        cipher = new A1Z26(conf_.getAlphabet());
        cipher.test(sample, key);

        // Decyper test
        Decrypter dec = new Decrypter(conf_);
        List<String> encryptedText = new ArrayList<>();
        encryptedText.add("NYVPR'F NQIRAGHERF VA JBAQREYNAQ");
        encryptedText.add("ZORXV'H ZWEVMGFIVH RM DLMWVIOZMW");
        encryptedText.add("1-12-9-3-5'19 1-4-22-5-14-20-21-18-5-19 9-14 23-15-14-4-5-18-12-1-14-4");

        for (String text : encryptedText) {
            IO.print("Original: ", Colour.BLUE_BOLD_BRIGHT);
            IO.print(text + "\n");
            List<DecryptGuess> guesses = dec.decrypt(text);

            //int N = guesses.size();
            int N = 5;
            for (int i = 0; i < N; i++) {
                IO.print("\t" + guesses.get(i).toString() + "\n");
            }

            IO.print("\n");
        }

        // TODO ...
        IO.todo("Everything!");
    }
}
