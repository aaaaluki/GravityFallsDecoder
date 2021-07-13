package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
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
                           debug_;

    private static int id;
    private static Map<Integer, Scanner> fileReaders;
    private static Map<Integer, PrintWriter> printWriters;
    private static Map<Integer, String> filenames;

    /**
     * Because this class is fully static, it does not have a constructor, so
     * this method is used instead
     */
    public static void init() {
        id = 0;
        fileReaders = new HashMap<>();
        printWriters = new HashMap<>();
        filenames = new HashMap<>();
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
     * Sets the debug parameter
     *
     * @param debug debug
     */
    public static void setDebug(boolean debug) {
        debug_ = debug;
    }

    /**
     * This is printed if debug mode is enabled
     *
     * @param obj object to print
     */
    public static void debug(Object obj) {
        //if (debug) System.out.println(Color.YELLOW + String.format("[DEBUG] %s", obj) + Color.RESET);
        if (debug_) {
            System.out.println(Color.YELLOW_BOLD_BRIGHT + "[DEBUG] " + Color.RESET + obj.toString());
        }
    }

    /**
     * This is used for printing errors
     *
     * @param obj object to print
     */
    public static void warn(Object obj) {
        System.err.println(Color.RED_BOLD_BRIGHT + "[ERROR] " + Color.RESET + obj.toString());
    }

    /**
     * If some code is not yet implemented you can use this method to print
     * something
     *
     * @param obj object to print
     */
    public static void todo(Object obj) {
        System.out.println(Color.CYAN_BOLD_BRIGHT + "[TODO] " + Color.RESET + obj.toString());
    }

    /**
     * prints obj on System.out
     *
     * @param obj object to print
     */
    public static void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * prints obj on System.out and colors the output
     *
     * @param obj object to print
     * @param fg_color foreground color from {@link utils.Color}
     */
    public static void print(Object obj, String fg_color) {
        print(fg_color + obj + Color.RESET);
    }

    /**
     * prints obj on System.out and colors the output
     *
     * @param obj object to print
     * @param fg_color foreground color from {@link utils.Color}
     * @param bg_color background color from {@link utils.Color}
     */
    public static void print(Object obj, String fg_color, String bg_color) {
        print(fg_color + bg_color + obj + Color.RESET);
    }

    /**
     * prints obj on System.out if verbose is enabled
     *
     * @param obj object to print
     */
    public static void printVerbose(Object obj) {
        if (verbose_) {
            print(obj);
        }
    }

    /**
     * prints obj on System.out and colors the output if verbose is enabled
     *
     * @param obj object to print
     * @param fg_color foreground color from {@link utils.Color}
     */
    public static void printVerbose(Object obj, String fg_color) {
        if (verbose_) {
            print(obj, fg_color);
        }
    }

    /**
     * prints obj on System.out and colors the output if verbose is enabled
     *
     * @param obj object to print
     * @param fg_color foreground color from {@link utils.Color}
     * @param bg_color background color from {@link utils.Color}
     */
    public static void printVerbose(Object obj, String fg_color, String bg_color) {
        if (verbose_) {
            print(obj, fg_color, bg_color);
        }
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
            fileReaders.put(id, new Scanner(fileIS));
            filenames.put(id, path);

            tmp = id;
            id++;

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
            if (fileReaders.get(id).hasNextLine()) {
                return fileReaders.get(id).nextLine();
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
            fileReaders.remove(id).close();
            debug(String.format("Closing Reading File: %s", filenames.remove(id)));
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
        String path = System.getProperty("user.dir") + File.separator + filename;
        FileOutputStream fileOS = null;

        int tmp = -1;
        try {
            fileOS = new FileOutputStream(path, true);
            printWriters.put(id, new PrintWriter(fileOS, true));
            filenames.put(id, path);

            tmp = id;
            id++;

            debug(String.format("Opening Writing File. %s", path));

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
        printWriters.get(id).println(line);
        debug(String.format("Closing Writing File: %s", filenames.remove(id)));
    }

    /**
     * Closes the file with the given id
     *
     * @param id id of the file
     */
    public static void closeWriteFile(int id) {
        try {
            printWriters.remove(id).close();
        } catch (IllegalStateException ex) {
        }
    }

    // Other file handling mehtods
    /**
     * Prints line to the console and to a text file with the given id
     *
     * @param id id of the file
     * @param line line to be printed
     */
    public static void printAndWrite(int id, String line) {
        print(line);
        printWriters.get(id).println(line);
    }

    /**
     * Prints the header file
     *
     * @param headerFile filename of the header file
     * @return {@link utils.ExitCodes}
     */
    public static int printHeader(String headerFile) {
        File file = new File(headerFile);
        int id = openReadFile(file);
        if (id == ExitCodes.FILE_NOT_FOUND) {
            return ExitCodes.FILE_NOT_FOUND;
        }
        debug("Header File: " + file.getAbsolutePath());

        String line = readLineFile(id);
        while (line != null) {
            System.out.println(Color.GREEN_BOLD_BRIGHT + line + Color.RESET);
            line = readLineFile(id);
        }

        closeReadFile(id);

        return ExitCodes.OK;
    }

    // Printing helper methods
    /**
     * Helper method for printing arrays
     * 
     * @param arr 
     */
    public static void printArray(Object[] arr) {
        System.out.println(Arrays.asList(arr));
    }
}
