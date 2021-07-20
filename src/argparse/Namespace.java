package argparse;

import java.util.List;
import java.util.Map;

/**
 * This class is a wrapper for a map.
 *
 * @author luki
 */
public class Namespace {

    private final Map<String, Object> attrs_;

    /**
     * Constructor for {@link Namespace}
     * 
     * @param attrs map of attributes already populated,
     *              K: name of the argument
     *              V: value of the argument
     */
    public Namespace(Map<String, Object> attrs) {
        this.attrs_ = attrs;
    }

    /**
     * Returns the value of the argument and it casts it to T
     * 
     * @param <T> Type to cast to
     * @param dest name of the argument
     * @return T or null
     */
    public <T> T get(String dest) {
        return (T) attrs_.get(dest);
    }

    /**
     * Returns the value of the argument as a String or null if the object is null
     * 
     * @param dest name of the argument
     * @return String or null
     */
    public String getString(String dest) {
        Object obj = get(dest);

        if (obj == null) {
            return null;
        }

        return obj.toString();
    }

    /**
     * Returns the value of the argument as an {@code Integer}
     * 
     * @param dest name of the argument
     * @return {@code Integer}
     */
    public Integer getInt(String dest) {
        return get(dest);
    }

    /**
     * Returns the value of the argument as a {@code boolean}
     * 
     * @param dest name of the argument
     * @return {@code boolean}
     */
    public boolean getBoolean(String dest) {
        return get(dest);
    }

    /**
     * Returns the value of the argument as a {@code List<E>}
     * 
     * @param <E> Type of the list
     * @param dest name of the argument
     * @return {@code List<E>}
     */
    public <E> List<E> getList(String dest) {
        return get(dest);
    }
}
