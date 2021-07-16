package utils.argparse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.IO;
import utils.TextHelper;

/**
 * Implementation of the {@link ArgumentParser} interface
 *
 * @author luki
 */
public class ArgumentParserImpl implements ArgumentParser {

    private final String programName_;
    private List<Argument> arguments_;
    private Map<Integer, Argument> argIndices_;
    private String usage_;
    private String description_;
    private String epilog_;

    /**
     * Constructor for {@link ArgumentParserImpl}
     *
     * @param programName name of the program that this parser is going to parse
     */
    public ArgumentParserImpl(String programName) {
        programName_ = programName;
        arguments_ = new ArrayList<>();
        argIndices_ = new HashMap<>();
        usage_ = "";
        description_ = "";
        epilog_ = "";

        // Add help argument
        this.addArgument("--help", "-h").nargs(0).setHelp("Shows help and exits");
    }

    @Override
    public Argument addArgument(String... flags) {
        Argument arg = new ArgumentImpl(flags);
        arguments_.add(arg);
        return arg;
    }

    @Override
    public Namespace parseArgs(String[] args) throws ArgumentException {
        Map<String, Object> attr = new HashMap<>();
        parseArgs(args, attr);
        return new Namespace(attr);
    }

    @Override
    public void parseArgs(String[] args, Map<String, Object> attr) throws ArgumentException {
        // First set default values
        for (Argument arg : arguments_) {
            attr.put(arg.getDest(), arg.getDefault());
        }

        // Then check given arguments
        Integer[] indices = findArgIndices(args);

        // If help was parsed exit
        if (argIndices_.values().contains(arguments_.get(0))) {
            throw new ArgumentException(HELP_EXCEPTION);
        }

        // Loop array of indices
        for (int i = 0; i < indices.length; i++) {
            int idx = indices[i];

            int len;
            if (i == indices.length - 1) {
                // If it's the last idx
                len = args.length - idx;
            } else {
                len = indices[i + 1] - idx;
            }
            // remove flag count from len
            len--;

            Argument arg = argIndices_.get(idx);
            int max = arg.getMaxNargs();
            int min = arg.getMinNargs();

            // Check that the number of arguments given for this flag is the 
            // correct amount, if not throw
            if (min <= len && len <= max) {
                String[] buf = new String[len];
                System.arraycopy(args, idx + 1, buf, 0, len);

                arg.action(buf, attr);
            } else {
                if (len < min) {
                    throw new ArgumentException(String.format("Argument \"%s\" needs a minimum of %d values", args[idx], min));
                } else {
                    throw new ArgumentException(String.format("Argument \"%s\" can have a maximum of %d values", args[idx], max));
                }
            }

        }
    }

    /**
     * Gets the array of commands from the command line and populates the map
     * argIndices with the index where the Argument was found and itself.
     *
     * This method returns an Integer array with the indices
     *
     * @param args arguments from the command line
     */
    private Integer[] findArgIndices(String[] args) throws ArgumentException {
        int idx = 0;
        for (String cliArg : args) {
            if (!cliArg.startsWith(ArgumentTextHelper.PREFIX_CHARS)) {
                // If it's not a flag, skip
                idx++;
                continue;
            }

            // Flag found
            boolean found = false;
            for (Argument parseArg : arguments_) {
                if (parseArg.getFlags().contains(cliArg)) {
                    argIndices_.put(idx, parseArg);
                    found = true;
                    break;
                }
            }

            // If it's not a know flag throw
            if (!found) {
                throw new ArgumentException(String.format("Flag \"%s\" does not correspond to any argument", cliArg));
            }

            idx++;
        }

        return argIndices_.keySet().toArray(new Integer[argIndices_.size()]);
    }

    // Usage and helpers    ####################################################
    @Override
    public void printHelp() {
        // Print usage
        printUsage();
        IO.print("\n");

        // Print description
        printDescription();
        IO.print("\n");

        // Print flags
        String[] flags = new String[arguments_.size()];
        int max = 0;
        for (int i = 0; i < arguments_.size(); i++) {
            flags[i] = arguments_.get(i).getFlags().toString().replace("[", "").replace("]", "");
            if (flags[i].length() > max) {
                max = flags[i].length();
            }
        }

        IO.print("Optional arguments:\n");
        for (int i = 0; i < arguments_.size(); i++) {
            IO.print(String.format("    %s\t%s\n", TextHelper.padRight(flags[i], max), arguments_.get(i).getHelp()));
        }
        IO.print("\n");

        // Print epilog
        printEpilog();
    }

    @Override
    public ArgumentParser usage(String text) {
        usage_ = ArgumentTextHelper.nonNull(text);
        return this;
    }

    @Override
    public void printUsage() {
        if (usage_.equals("")) {
            StringBuilder sb = new StringBuilder(programName_ + " ");
            for (Argument arg : arguments_) {
                switch (arg.getMinNargs()) {
                    case 0 -> sb.append(String.format("[%s] ", arg.getShortFlag()));
                    case 1 -> sb.append(String.format("[%s %s] ", arg.getShortFlag(), ArgumentTextHelper.removePrefix(arg.getMainFlag())));
                    default -> sb.append(String.format("[%s %s...] ", arg.getShortFlag(), ArgumentTextHelper.removePrefix(arg.getMainFlag())));
                }
            }

            usage_ = sb.toString();
        }

        IO.print(String.format("usage: %s\n", usage_));
    }

    @Override
    public ArgumentParser description(String text) {
        description_ = ArgumentTextHelper.nonNull(text);
        return this;
    }

    @Override
    public void printDescription() {
        IO.print(description_ + "\n");
    }

    @Override
    public ArgumentParser epilog(String text) {
        epilog_ = ArgumentTextHelper.nonNull(text);
        return this;
    }

    @Override
    public void printEpilog() {
        IO.print(epilog_ + "\n");
    }
}
