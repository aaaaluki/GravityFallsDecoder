package argparse;

import argparse.action.Action;
import argparse.action.ActionBoolean;
import argparse.action.ActionInteger;
import argparse.action.ActionString;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the {@link Argument} interface
 *
 * @author luki
 */
public final class ArgumentImpl implements Argument {

    private String name_;
    private int maxNargs_;
    private int minNargs_;
    private List<String> flags_;
    private boolean consumed_;
    private boolean required_;
    private Set<String> disables_;
    private boolean disablesAll_;
    private String[] choices_;
    private Object value_;
    private Object default_;
    private Type type_;

    private String help_;

    /**
     * <p>
     * Constructor for {@link ArgumentImpl}
     * </p>
     * <p>
     * Sets the name of the argument, {@code name_}, as the first flag given
     * without the prefix characters. Sets default type as {@code Type.STRING}
     * </p>
     *
     * @param flags array of flags given at creation
     */
    public ArgumentImpl(String prefix, String... flags) {
        if (flags.length == 0) {
            throw new IllegalArgumentException("flags is not specified");
        }

        name_ = prefix + "." + ArgumentTextHelper.removePrefix(flags[0]);
        flags_ = Arrays.asList(flags);
        consumed_ = false;
        required_ = false;
        type_ = Type.STRING;
    }

    @Override
    public Argument nargs(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("nargs cannot be negative");
        }

        maxNargs_ = n;
        minNargs_ = n;

        return this;
    }

    @Override
    public Argument nargs(String n) {
        switch (n) {
            case "?":
                minNargs_ = 0;
                maxNargs_ = 1;
                break;
            case "*":
                minNargs_ = 0;
                maxNargs_ = Integer.MAX_VALUE;
                break;
            case "+":
                minNargs_ = 1;
                maxNargs_ = Integer.MAX_VALUE;
                break;
            default:
                throw new IllegalArgumentException("?, * or + has to be given");
        }

        return this;
    }

    @Override
    public Argument setType(Type type) {
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (type == Type.BOOLEAN) {
            this.nargs(0);
        }
        type_ = type;
        return this;
    }

    @Override
    public Argument setDefault(Object value) {
        default_ = value;
        return this;
    }

    @Override
    public Argument setDest(String dest) {
        // setDest cannot be null, might have to change this part
        name_ = ArgumentTextHelper.nonNull(dest);
        return this;
    }

    @Override
    public <E> Argument setChoices(E... values) {
        choices_ = (String[]) values;
        return this;
    }

    @Override
    public Argument setHelp(String help) {
        help_ = ArgumentTextHelper.nonNull(help);
        return this;
    }
    
    @Override
    public Argument required() {
        required_ = true;
        return this;
    }
    
    @Override
    public Argument disables(String... dests) {
        if (dests.length == 0) {
            throw new IllegalArgumentException("dests is not specified");
        }

        disables_ = Set.of(dests);
        
        return this;
    }
    
    @Override
    public Argument disablesAll() {
        disablesAll_ = true;
        
        return this;
    }
    
    @Override
    public Argument action(String[] args, Map<String, Object> attr) throws ArgumentException {
        Action act;

        switch (type_) {
            case BOOLEAN:
                act = new ActionBoolean();
                break;
            case INTEGER:
                act = new ActionInteger();
                break;
            case STRING:
                act = new ActionString();
                break;
            default:
                // This will never be the case, since every argument is asigned
                // Type.String at creation and this argument cannot be removed
                throw new ArgumentException(String.format("Argument %s, doesn't have a Type!", name_));
            }

        act.run(args, this, attr, choices_, value_);
        consumed_ = true;
        
        return this;
    }

    // Getters  and setters ####################################################
    @Override
    public int getMaxNargs() {
        return maxNargs_;
    }

    @Override
    public int getMinNargs() {
        return minNargs_;
    }

    @Override
    public String getDest() {
        return name_;
    }

    @Override
    public Object getDefault() {
        return default_;
    }

    @Override
    public String getHelp() {
        return help_;
    }
    
    @Override
    public boolean getRequired() {
        return required_;
    }
    
    @Override
    public Set<String> getDisables() {
        return disables_;
    }

    @Override
    public boolean getDisablesAll() {
        return disablesAll_;
    }

    @Override
    public List<String> getFlags() {
        return flags_;
    }

    @Override
    public String getMainFlag() {
        return flags_.get(0);
    }

    @Override
    public String getShortFlag() {
        String min = null;
        int minLen = Integer.MAX_VALUE;
        for (String flg : flags_) {
            if (flg.length() < minLen) {
                minLen = flg.length();
                min = flg;
            }
        }
        
        return min;
    }
    
    @Override
    public boolean getConsumed() {
        return consumed_;
    }

    @Override
    public void consumed() {
        consumed_ = true;
    }
}
