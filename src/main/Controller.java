package main;

import analysis.Analyzer;
import analysis.DecryptGuess;
import analysis.Decrypter;
import analysis.FrequencyAnalysis;
import ciphers.A1Z26;
import ciphers.Atbash;
import ciphers.Binary;
import ciphers.Caesar;
import ciphers.Cipher;
import ciphers.Key;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import menu.MenuController;
import utils.Colour;
import utils.Config;
import utils.IO;
import utils.TextHelper;

/**
 * The Controller class runs the deciphering part of the project
 *
 * @author luki
 */
public class Controller {

    private final Set<Cipher> ciphers_;
    private final Config conf_;
    
    private MenuController menuCont_;

    /**
     * Controller constructor
     *
     * @param conf {@link Config} object
     */
    public Controller(Config conf) {
        conf_ = conf;
        
        ciphers_ = new HashSet<>();
        Analyzer fa = new FrequencyAnalysis(conf_);
        
        ciphers_.add(new A1Z26(conf_.get("lang.alphabet"), fa));
        ciphers_.add(new Atbash(conf_.get("lang.alphabet"), fa));
        ciphers_.add(new Binary(conf_.get("lang.alphabet"), fa));
        ciphers_.add(new Caesar(conf_.get("lang.alphabet"), fa));
        
        Decrypter.init(conf, ciphers_);        
    }

    /**
     * Runs all the decryption code, will read from files and write the
     * decryption result on a new file.
     */
    public void start() {
        if (conf_.get("menu.interactive")) {
            startInteractive();
            return;
        }
        
        List<String> filenames = conf_.<String>getList("decrypt.files");
        for (String filename : filenames) {
            // TODO: Change the way we handle the decoded extension, leave the
            // existing extension and insert DECODED_EXTENSION before the last
            // file extension. Create a REGEX for matching?¿
            if (filename.endsWith(Config.DECODED_EXTENSION)) {
                // If it's a deciphered file, skip
                IO.print("Skipping file: ", Colour.PURPLE_BOLD);
                IO.println(String.format("%s is already decoded", filename));
                continue;
            }

            // I've chosen 92 because it's the width of my console when it
            // occupies half the screen
            int maxWidth = 92 - 2;  // -1 for each | at each side
            
            //The output of below should appear like this:
            //    +--------------------------------- ... -+
            //    | Deciphering: filename                 |
            //    +--------------------------------- ... -+
            IO.println("+" + "-".repeat(maxWidth) + "+");
            IO.print("| ");
            IO.print("Deciphering: ", Colour.PURPLE_BOLD);
            IO.println(TextHelper.padRight(filename, maxWidth - 15) + " |");
            IO.println("+" + "-".repeat(maxWidth) + "+");
            
            decipherFile(filename);
        }
    }
    
    /**
     * Starts interactive mode (Menus)
     * 
     */
    private void startInteractive() {
        menuCont_ = new MenuController(conf_, this);
        boolean run = true;
        
        while (run) {
            run = menuCont_.loop();
        }
        
    }
    
    /**
     * Deciphers the given file line by line, it does not take into account
     * that all the file could be encrypted with the same cipher
     * 
     * @param filename file to decipher
     */
    public void decipherFile(String filename) {
        int readId = IO.openReadFile(filename);
        File writeFile = new File(TextHelper.getNameWithoutExtension(filename) + Config.DECODED_EXTENSION);
        IO.removeFile(writeFile);
        int writeId = IO.openWriteFile(writeFile);

        String line = IO.readLineFile(readId).trim().strip();
        int lineNum = 1;
        while (line != null) {
            IO.printVerbose(String.format("[%3d] Original: ", lineNum), Colour.BLUE_BOLD_BRIGHT);
            IO.printVerbose(line + "\n");
            IO.writeLineFile(writeId, String.format("[%3d] Original: %s", lineNum, line));
            
            List<DecryptGuess> guesses = Decrypter.decrypt(line);

            int guessesToShow = Math.min((int) conf_.get("decrypt.guesses"), guesses.size());
            for (int i = 0; i < guessesToShow; i++) {
                IO.printVerbose("\t" + guesses.get(i).toString() + "\n");
                IO.writeLineFile(writeId, "\t" + guesses.get(i).toString());
            }

            IO.writeLineFile(writeId, "");
            line = IO.readLineFile(readId);
            lineNum++;
        }

        IO.closeReadFile(readId);
        IO.closeWriteFile(writeId);
    }

    public Set<Cipher> getCiphers() {
        return ciphers_;
    }
    
    /**
     * Method to test some functionalities done so far, i should probably learn
     * to do proper tests in Java :)
     */
    private void test() {
        // Testing ciphers
        // Caesar
        String sample = "ALICE'S ADVENTURES IN WONDERLAND";
        Key key = new Key(13);
        Analyzer fa = new FrequencyAnalysis(conf_);

        Cipher cipher = new Caesar(conf_.get("lang.alphabet"), fa);
        cipher.test(sample, key);

        // Atbash
        cipher = new Atbash(conf_.get("lang.alphabet"), fa);
        cipher.test(sample, key);

        // A1Z26
        cipher = new A1Z26(conf_.get("lang.alphabet"), fa);
        cipher.test(sample, key);

        // Decyper test
        Decrypter.init(conf_, ciphers_);
        List<String> encryptedText = new ArrayList<>();
        encryptedText.add("NYVPR'F NQIRAGHERF VA JBAQREYNAQ");
        encryptedText.add("ZORXV'H ZWEVMGFIVH RM DLMWVIOZMW");
        encryptedText.add("1-12-9-3-5'19 1-4-22-5-14-20-21-18-5-19 9-14 23-15-14-4-5-18-12-1-14-4");

        for (String text : encryptedText) {
            IO.print("Original: ", Colour.BLUE_BOLD_BRIGHT);
            IO.println(text);
            List<DecryptGuess> guesses = Decrypter.decrypt(text);

            int guessesToShow = Math.min((int) conf_.get("decrypt.guesses"), guesses.size());
            for (int i = 0; i < guessesToShow; i++) {
                IO.println("\t" + guesses.get(i).toString());
            }

            IO.println();
        }
    }
}
