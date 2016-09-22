/**
 * An associative container with unique binding from keys to a single value.
 */
public interface IntLongMap {
    /**
     * Place a given key and value in the container.
     *
     * @return The value previously stored under the given key in the map is returned.
     */
    long put(int key, long value);

    /**
     * @return Returns the value associated with the given key or the default value
     * for the key type, if the key is not associated with any value.
     *
     * Note, use {@link #containsKey(int)} if you want to know weather key in map
     */
    long get(int key);

    /**
     * Check whether map contains given key
     */
    boolean containsKey(int key);

    /**
     * @return Returns the number key-value pairs in map
     */
    int size();
}
