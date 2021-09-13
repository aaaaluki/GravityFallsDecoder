package argparse.action;

import argparse.Argument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * Implementation of the {@link Action} interface for parsing as {@code String}
 *
 * @author luki
 */
public class ActionString implements Action {

    @Override
    public void run(String[] args, Argument arg, Map<String, Object> attr, Object choices, Object value) {
        List<String> argsList = new ArrayList<>(Arrays.asList(args));

        int max = arg.getMaxNargs();
        int min = arg.getMinNargs();

        if (min == max && min == 1) {
            value = argsList.get(0);
        } else {
            value = argsList;
        }

        attr.put(arg.getDest(), value);
    }
}
