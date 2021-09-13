package argparse.action;

import argparse.Argument;
import argparse.ArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import utils.TextHelper;

/**
 * Implementation of the {@link Action} interface for parsing as {@code Integer}
 *
 * @author luki
 */
public class ActionInteger implements Action {

    @Override
    public void run(String[] args, Argument arg, Map<String, Object> attr, Object choices, Object value) throws ArgumentException {
        List<Integer> argsList = new ArrayList<>();

        for (String str : args) {
            if (!TextHelper.checkInteger(str)) {
                // If str cannot be converted to String throw
                throw new ArgumentException(String.format("Argument \"%s\" needs it's values to be Integers, and got: \"%s\"", arg.getMainFlag(), str));
            }

            try {
                argsList.add(Integer.valueOf(str));
            } catch (NumberFormatException e) {
                throw new ArgumentException("Number introduced to large, max value is: " + Integer.MAX_VALUE);
            }
        }

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
