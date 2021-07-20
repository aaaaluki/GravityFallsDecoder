package argparse.action;

import java.util.Map;
import argparse.Argument;

/**
 * Implementation of the {@link Action} interface for parsing as {@code boolean}
 *
 * @author luki
 */
public class ActionBoolean implements Action {

    @Override
    public void run(String[] args, Argument arg, Map<String, Object> attr, Object choices, Object value) {
        // Let's suppose that if it's a boolean argument and you set it, 
        // it's because you want to change the default value

        value = !((boolean) arg.getDefault());
        attr.put(arg.getDest(), value);
    }
}
