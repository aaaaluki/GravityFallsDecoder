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
                           color_;

    private static int id_;
    private static Map<Integer, Scanner> fileReaders_;
    private static Map<Integer, PrintWriter> printWriters_;
    private static Map<Integer, String> filenames_;

    /**
     * Because this class is fully static, it does not have a constructor, so
     * this method is used instead
     */
    public static void init() {
        id_ = 0;
        fileReaders_ = new HashMap<>();
        printWriters_ = new HashMap<>();
        filenames_ = new HashMap<>();
    }

    /**
     * Sets the colour parameter, if true prints will display colour
     *
     * @param color colour
     */
    public static void setColor(boolean color) {
        color_ = color;
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
            if (color_) {
                System.out.println(Colour.YELLOW_BOLD_BRIGHT + "[DEBUG] " + Colour.RESET + obj);
            } else {
                System.out.println("[DEBUG] " + obj);
            }
        }
    }

    /**
     * This is used for printing errors
     *
     * @param obj object to print
     */
    public static void warn(Object obj) {
        if (color_) {
            System.err.println(Colour.RED_BOLD_BRIGHT + "[ERROR] " + Colour.RESET + obj);
        } else {
            System.err.println("[ERROR] " + obj);
        }
    }

    /**
     * If some code is not yet implemented you can use this method to print
     * something
     *
     * @param obj object to print
     */
    public static void todo(Object obj) {
        if (color_) {
            System.out.println(Colour.CYAN_BOLD_BRIGHT + "[TODO] " + Colour.RESET + obj);
        } else {
            System.out.println("[TODO] " + obj);
        }
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
     * prints obj on System.out and colours the output
     *
     * @param obj object to print
     * @param fgColor foreground colour from {@link utils.Color}
     */
    public static void print(Object obj, String fgColor) {
        if (color_) {
            print(fgColor + obj + Colour.RESET);
        } else {
            print(obj);
        }
    }

    /**
     * prints obj on System.out and colours the output
     *
     * @param obj object to print
     * @param fgColor foreground colour from {@link utils.Color}
     * @param bgColor background colour from {@link utils.Color}
     */
    public static void print(Object obj, String fgColor, String bgColor) {
        if (color_) {
            print(fgColor + bgColor + obj + Colour.RESET);
        } else {
            print(obj);
        }
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
     * prints obj on System.out and colours the output if verbose is enabled
     *
     * @param obj object to print
     * @param fgColor foreground colour from {@link utils.Color}
     */
    public static void printVerbose(Object obj, String fgColor) {
        if (verbose_) {
            print(obj, fgColor);
        }
    }

    /**
     * prints obj on System.out and colours the output if verbose is enabled
     *
     * @param obj object to print
     * @param fgColor foreground colour from {@link utils.Color}
     * @param bgColor background colour from {@link utils.Color}
     */
    public static void printVerbose(Object obj, String fgColor, String bgColor) {
        if (verbose_) {
            print(obj, fgColor, bgColor);
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
        String path = System.getProperty("user.dir") + File.separator + filename;
        FileOutputStream fileOS = null;

        int tmp = -1;
        try {
            fileOS = new FileOutputStream(path, true);
            printWriters_.put(id_, new PrintWriter(fileOS, true));
            filenames_.put(id_, path);

            tmp = id_;
            id_++;

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
        printWriters_.get(id).println(line);
        debug(String.format("Closing Writing File: %s", filenames_.remove(id)));
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
            print(line + "\n", Colour.GREEN_BOLD_BRIGHT);
            line = readLineFile(id);
        }

        closeReadFile(id);

        return ExitCodes.OK;
    }
}
