package ciphers;

/**
 * This class is a wrapper for an Object, this will be used as the encryption key
 *
 * @author luki
 */
public class Key {

    private final Object value_;

    /**
     * Constructor for {@code Key}, assigns {@code null} to value_
     *
     */
    public Key() {
        value_ = null;
    }

    /**
     * Constructor for {@code Key}, assigns {@code value} to value_
     *
     * @param value value of the key
     */
    public Key(Object value) {
        value_ = value;
    }

    /**
     * Returns the value and it casts it to T
     *
     * @param <T> Type to cast to
     * @return T or null
     */
    public <T> T get() {
        return (T) value_;
    }

    /**
     * Returns the value as an {@code Integer}
     *
     * @return {@code Integer}
     */
    public Integer getInteger() {
        return get();
    }

    /**
     * Returns the value of the argument as a String or {@code "None"} if the
     * object is null
     *
     * @return String
     */
    public String getString() {
        Object obj = value_;

        if (obj == null) {
            return "None";
        }

        return obj.toString();
    }
}
