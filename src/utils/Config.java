package utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class has all the configuration parameters. To load the parameters the
 * loadConfig() method is used.
 *
 * @author luki
 */
public class Config {
    
    /**
     * Folder were the configuration files are stored
     */
    public static final String CONFIG_FOLDER = "gfd";
    
    /**
     * Extension for the decoded files
     */
    public static final String DECODED_EXTENSION = "_decoded.txt";

    private final Namespace ns_;

    /**
     * Constructor for config
     *
     * @param ns {@link Namespace} object containing all the configuration, the
     * one that {@link argparse.ArgumentParser} returns when parsing
     */
    public Config(Namespace ns) {
        ns_ = ns;
    }

    /**
     * Calls the get method from {@link Namespace}
     *
     * @param <T> Type to cast to
     * @param dest name of the argument
     * @return T or null
     */
    public <T> T get(String dest) {
        return ns_.get(dest);
    }

    /**
     * Calls the getList method from {@link Namespace}
     *
     * @param <E> Type of the list
     * @param dest name of the argument
     * @return {@code List<E>}
     */
    public <E> List<E> getList(String dest) {
        return get(dest);
    }

    /**
     * Getter for {@link Namespace} keys
     * 
     * @return a Set of keys from the configuration Namespace
     */
    public Set<String> getKeys() {
        return ns_.getKeys();
    }

    /**
     * Returns the absolute path of the configuration folder
     * 
     * @return configuration folder absolute path
     */
    public String getConfigPath() {
        return System.getProperty("user.home") + File.separator + ".config" +
                File.separator + CONFIG_FOLDER + File.separator;
    }
    
    /**
     * Loads the alphabet and frequencies form the [lang].config file. If the
     * configuration file is not valid returns.
     *
     * @return Returns -1 if an error occurred, 0 otherwise
     */
    public int loadConfig() {
        String configFolder = getConfigPath();
        IO.debug("Config Folder: " + configFolder);
        
        File configFile = new File(String.format("%s%s.config", configFolder, ns_.getString("lang.language")));
        int id = IO.openReadFile(configFile);

        IO.debug("Config File: " + configFile.getAbsolutePath());

        if (id == -1) {
            // No need to close file here because it cannot be opened
            return -1;
        }

        String line = IO.readLineFile(id);
        int lineCount = 1;
        while (line != null) {
            if (line.startsWith("#")) {
                // ignore comments
                line = IO.readLineFile(id);
                lineCount++;
                continue;
            }

            String[] args = line.split(": ");

            if (args.length != 2) {
                IO.warn(String.format("(%s:%d) Does not have a <Key: Value> pair!", configFile.getName(), lineCount));
                IO.closeReadFile(id);
                return -1;
            }

            switch (args[0]) {
                case "language" -> {
                    if (!ns_.getString("lang.language").equals(args[1])) {
                        IO.warn(String.format("Filename does not match languague in file %s", configFile.getName()));
                        IO.closeReadFile(id);
                        return -1;
                    }
                }

                case "alphabet" -> ns_.put("lang.alphabet",args[1]);

                case "frequencies_mono" -> {
                    Map<String, Double> frequenciesMono = new HashMap<>();

                    int argCount = 1;
                    for (String kvPair : args[1].split(";")) {
                        String[] kv = kvPair.split("=");
                        if (kv.length != 2) {
                            IO.warn(String.format("(%s:%d) <Key=Value> pair %d can not be split!", configFile.getName(), lineCount, argCount));
                            IO.closeReadFile(id);
                            return -1;
                        }

                        if (TextHelper.checkDouble(kv[1])) {
                            frequenciesMono.put(kv[0], Double.valueOf(kv[1]));
                        } else {
                            IO.warn(String.format("(%s:%d) <Key=Value> pair %s=%s Value is not a number!", configFile.getName(), lineCount, kv[0], kv[1]));
                            IO.closeReadFile(id);
                            return -1;
                        }

                        argCount++;
                    }
                    
                    ns_.put("lang.frequencies-mono", frequenciesMono);
                }

                default -> {
                    IO.warn(String.format("(%s:%d) Key %s is not a valid one!", configFile.getName(), lineCount, args[0]));
                    IO.closeReadFile(id);
                    return -1;
                }
            }

            line = IO.readLineFile(id);
            lineCount++;
        }

        // Check that the alphabet and frequencies are given, if not return error
        if (ns_.getString("lang.alphabet") == null || ns_.getString("lang.frequencies-mono") == null) {
            if (ns_.getString("lang.alphabet") == null) {
                IO.warn(String.format("alphabet not defined on the config file: %s", configFile.getName()));
            }
            if (ns_.getString("lang.frequencies-mono") == null) {
                IO.warn(String.format("frequencies_mono not defined on the config file: %s", configFile.getName()));
            }
            IO.closeReadFile(id);

            return -1;
        }

        IO.closeReadFile(id);

        return 0;
    }
}
