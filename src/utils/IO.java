package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class in charge of all the Input and Output operation
 *
 * @author luki
 */
public class IO {

    private static boolean verbose_,
                           debug_,
                           noColour_;

    private static Scanner userInput_;
    
    private static int id_;
    private static Map<Integer, Scanner> fileReaders_;
    private static Map<Integer, PrintWriter> printWriters_;
    private static Map<Integer, String> filenames_;

    /**
     * Because this class is fully static, it does not have a constructor, so
     * this method is used instead
     */
    public static void init() {
        userInput_ = new Scanner(System.in);
        
        id_ = 0;
        fileReaders_ = new HashMap<>();
        printWriters_ = new HashMap<>();
        filenames_ = new HashMap<>();
    }

    /**
     * Sets the colour parameter, if true prints will display colour
     *
     * @param noColour colour
     */
    public static void setColour(boolean noColour) {
        noColour_ = noColour;
    }

    /**
     * Sets the debug parameter
     *
     * @param debug debug
     */
    public static void setDebug(boolean debug) {
        debug_ = debug;
    }

    /**
     * Set the verbose parameter
     *
     * @param verbose verbose
     */
    public static void setVerbose(boolean verbose) {
        verbose_ = verbose;
    }

    /**
     * This is printed if debug mode is enabled
     *
     * @param obj object to print
     */
    public static void debug(Object obj) {
        if (debug_) {
            if (noColour_) {
                System.out.println("[DEBUG] " + obj);
            } else {
                System.out.println(Colour.YELLOW_BOLD_BRIGHT + "[DEBUG] " + Colour.RESET + obj);
            }
        }
    }

    /**
     * This is used for printing errors
     *
     * @param obj object to print
     */
    public static void warn(Object obj) {
        if (noColour_) {
            System.err.println("[ERROR] " + obj);
        } else {
            System.err.println(Colour.RED_BOLD_BRIGHT + "[ERROR] " + Colour.RESET + obj);
        }
    }

    /**
     * If some code is not yet implemented you can use this method to print
     * something
     *
     * @param obj object to print
     */
    public static void todo(Object obj) {
        if (noColour_) {
            System.out.println("[TODO] " + obj);
        } else {
            System.out.println(Colour.CYAN_BOLD_BRIGHT + "[TODO] " + Colour.RESET + obj);
        }
    }

    /**
     * Prints the given objects to System.out. If two arguments are given it is
     * supposed that the second one is the text colour. If three are given the
     * third one is the background colour.
     *
     * @param objs object to print
     */
    public static void print(Object... objs) {
        if (noColour_ || objs.length == 1) {
            System.out.print(objs[0]);
        } else {
            switch(objs.length) {
                case 2:
                    System.out.print(objs[1].toString() + objs[0].toString() + Colour.RESET);
                    break;
                case 3:
                    System.out.print(objs[1].toString() + objs[2].toString() + objs[0].toString() + Colour.RESET);
                    break;
                default:
                    for (int i = 0; i < objs.length; i++) {
                        System.out.print(objs[i]);
                    }
            }
        }
    }
    
    /**
     * Prints the given objects on a new line to System.out. If two arguments
     * are given it is supposed that the second one is the text colour. If three
     * are given the third one is the background colour.
     * 
     * @param objs objects to print
     */
    public static void println(Object... objs) {
        if (objs.length == 0) {
            print("\n");
            return;
        }

        objs[0] = objs[0] + "\n";
        print(objs);
    }

    /**
     * prints the objects on System.out if verbose is enabled
     *
     * @param objs objects to print
     */
    public static void printVerbose(Object... objs) {
        if (verbose_) {
            print(objs);
        }
    }
    
    /**
     * Returns the line the user has written
     * 
     * @return user input
     */
    public static String readLine() {
        return userInput_.nextLine();
    }
    
    /**
     * Clears the last N lines
     * 
     * @param N Lines to clear
     */
    public static void clearLines(int N) {
        // Taken from: https://stackoverflow.com/a/22083329/13313449
        
        for (int i = 1; i <= N; i++) {
            // \033 equals 0x1B, the escape char
            System.out.print("\033[1A"); // Move up
            System.out.print("\033[2K"); // Erase line content
        }
    }
    
    /**
     * Clears the terminal/console screen
     */
    public static void clearScreen() {
        // I don't know the difference between console and terminal btw
        // \033 equals 0x1B, the escape char
        System.out.print("\033[H\033[2J\033[3J");
    }

    // INPUT file handling  ##############################################################
    /**
     * Tries to open the file for reading and returns an id for the file, if it
     * can't be done returns -1.
     *
     * @param filename file to open
     * @return id of the file
     */
    public static int openReadFile(String filename) {
        String path = filename;
        FileInputStream fileIS = null;

        int tmp = -1;
        try {
            fileIS = new FileInputStream(path);
            fileReaders_.put(id_, new Scanner(fileIS));
            filenames_.put(id_, path);

            tmp = id_;
            id_++;

            debug(String.format("Opening Reading File: %s", path));
        } catch (FileNotFoundException ex) {
            warn(String.format("File %s not found!", path));
        }

        return tmp;
    }

    /**
     * Tries to open the file for reading and returns an id for the file, if it
     * can't be done returns -1.
     *
     * @param file file to open
     * @return id of the file
     */
    public static int openReadFile(File file) {
        return openReadFile(file.getAbsolutePath());
    }

    /**
     * Returns the next line if it's possible, if not returns null.
     *
     * @param id id of the file
     * @return line read
     */
    public static String readLineFile(int id) {
        try {
            if (fileReaders_.get(id).hasNextLine()) {
                return fileReaders_.get(id).nextLine();
            }
        } catch (NoSuchElementException ex) {
        }

        return null;
    }

    /**
     * Closes the file with the given id
     *
     * @param id id of the file
     */
    public static void closeReadFile(int id) {
        try {
            fileReaders_.remove(id).close();
            debug(String.format("Closing Reading File: %s", filenames_.remove(id)));
        } catch (IllegalStateException ex) {

        }
    }

    // OUTPUT File handling ##############################################################
    /**
     * Tries to open the file for writing and returns an id for the file, if it
     * can't be done returns -1.
     *
     * @param filename file to open
     * @return id of the file
     */
    public static int openWriteFile(String filename) {
        String path = filename;
        FileOutputStream fileOS = null;

        int tmp = -1;
        try {
            fileOS = new FileOutputStream(path, true);
            printWriters_.put(id_, new PrintWriter(fileOS, true));
            filenames_.put(id_, path);

            tmp = id_;
            id_++;

            debug(String.format("Opening Writing File: %s", path));

        } catch (FileNotFoundException e) {
            warn(String.format("File (%s) not found!", path));
        }

        return tmp;
    }

    /**
     * Tries to open the file for writing and returns an id for the file, if it
     * can't be done returns -1.
     *
     * @param file file to open
     * @return id of the file
     */
    public static int openWriteFile(File file) {
        return openWriteFile(file.getAbsolutePath());
    }

    /**
     * Writes the line to the file.
     *
     * @param id id of the file
     * @param line line to write
     */
    public static void writeLineFile(int id, String line) {
        printWriters_.get(id).println(line);
    }

    /**
     * Closes the file with the given id
     *
     * @param id id of the file
     */
    public static void closeWriteFile(int id) {
        try {
            printWriters_.remove(id).close();
        } catch (IllegalStateException ex) {
        }
    }

    // Other file handling mehtods
    /**
     * If file exists it deletes it, it not returns without doing anything
     *
     * @param file file to remove
     */
    public static void removeFile(File file) {
        if (file.exists()) {
            file.delete();
            debug(String.format("Deleting File: %s", file.getAbsolutePath()));
        }
    }

    /**
     * If file exists it deletes it, it not returns without doing anything
     *
     * @param filename filename of file to remove
     */
    public static void removeFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
            debug(String.format("Deleting File: %s", file.getAbsolutePath()));
        }
    }

    /**
     * Prints line to the console and to a text file with the given id
     *
     * @param id id of the file
     * @param line line to be printed
     */
    public static void printAndWrite(int id, String line) {
        print(line);
        printWriters_.get(id).println(line);
    }

    /**
     * Prints the header file
     *
     * @param headerFile filename of the header file
     */
    public static void printHeader(String headerFile) {
        File file = new File(headerFile);
        debug("Header File: " + file.getAbsolutePath());

        int id = openReadFile(file);
        if (id == -1) {
            return;
        }
        
        String line = readLineFile(id);
        while (line != null) {
            println(line, Colour.GREEN_BOLD_BRIGHT);
            line = readLineFile(id);
        }

        closeReadFile(id);
    }
}
