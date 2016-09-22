import hash.ConcreteHashHPPC;
import hash.HashContext;
import hash.HashStrategy;
import probing.LinearProbing;
import probing.ProbingContext;
import probing.ProbingStrategy;

/**
 * A main.hash map of <code>int</code> to <code>long</code>, implemented using open
 * addressing for collision resolution.
 * <p>
 * The internal buffers of this implementation ({@link #keys}, {@link #values},
 * {@link #allocated}) are always allocated to the nearest size that is a power of two. When
 * the capacity exceeds the given load factor, the buffer size is doubled.
 * </p>
 */
public class IntLongHashMap implements IntLongMap {
    /**
     * Default capacity.
     */
    public final static int DEFAULT_CAPACITY = 16;

    /**
     * Minimum capacity for the map.
     */
    public final static int MIN_CAPACITY = 4;

    /**
     * Default load factor.
     */
    public final static float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * Default load main.hash strategy.
     */
    public final static HashStrategy DEFAULT_HASH_STRATEGY = new ConcreteHashHPPC();
    /**
     * Default load main.probing strategy.
     */
    public final static ProbingStrategy DEFAULT_PROBING_STRATEGY = new LinearProbing();
    /**
     * The load factor for this map (fraction of allocated slots
     * before the buffers must be rehashed or reallocated).
     */
    private final float loadFactor;
    /**
     * Hash-indexed array holding all keys.
     *
     * @see #values
     */
    private int[] keys;
    /**
     * Hash-indexed array holding all values associated to the keys
     * stored in {@link #keys}.
     *
     * @see #keys
     */
    private long[] values;
    /**
     * Information if an entry (slot) in the {@link #values} table is allocated
     * or empty.
     *
     * @see #assigned
     */
    private boolean[] allocated;
    /**
     * Cached number of assigned slots in {@link #allocated}.
     */
    private int assigned;
    /**
     * Cached capacity threshold at which we must resize the buffers.
     */
    private int resizeThreshold;
    /**
     * Choosing hashing strategy, by default {@link ConcreteHashHPPC}
     */
    private HashContext hashContext;
    /**
     * Choosing main.probing strategy, by default {@link LinearProbing}
     */
    private ProbingContext probingContext;

    /**
     * Creates a main.hash map with the default capacity of {@value #DEFAULT_CAPACITY},
     * load factor of {@value #DEFAULT_LOAD_FACTOR}.
     * <p>
     * <p>See class notes about main.hash distribution importance.</p>
     */
    public IntLongHashMap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates a main.hash map with the given initial capacity, default load factor of
     * {@value #DEFAULT_LOAD_FACTOR}.
     * <p>
     * <p>See class notes about main.hash distribution importance.</p>
     *
     * @param initialCapacity Initial capacity (greater than zero and automatically
     *                        rounded to the next power of two).
     *
     * @throws IllegalArgumentException when initial capacity less 0 and greater Integer.MAX_VALUE
     */
    public IntLongHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a main.hash map with the given initial capacity,
     * load factor, default main.hash strategy of {@link #DEFAULT_HASH_STRATEGY},
     * default main.probing strategy of {@link #DEFAULT_PROBING_STRATEGY}
     *
     * @param initialCapacity Initial capacity (greater than zero and automatically
     *                        rounded to the next power of two).
     * @param loadFactor      The load factor (greater than zero and smaller than 1).
     *
     * @throws IllegalArgumentException when initial capacity less 0 and greater Integer.MAX_VALUE,
     *                                  or load factor NOT in range (0, 1]
     */
    public IntLongHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_HASH_STRATEGY, DEFAULT_PROBING_STRATEGY);
    }

    /**
     * Creates a main.hash map with the given initial capacity, load factor and main.hash function,
     * default main.probing strategy of {@link #DEFAULT_PROBING_STRATEGY}
     *
     * @param initialCapacity Initial capacity (greater than zero and automatically
     *                        rounded to the next power of two).
     * @param loadFactor      The load factor (greater than zero and smaller than 1).
     * @param hashStrategy    Strategy of main.hash value computation
     *
     * @throws IllegalArgumentException when initial capacity less 0 and greater Integer.MAX_VALUE,
     *                                  or load factor NOT in range (0, 1]
     */
    public IntLongHashMap(int initialCapacity, float loadFactor, HashStrategy hashStrategy) {
        this(initialCapacity, loadFactor, hashStrategy, DEFAULT_PROBING_STRATEGY);
    }

    /**
     * Creates a main.hash map with the given initial capacity, load factor and main.probing strategy
     * default hashing strategy of {@link #DEFAULT_HASH_STRATEGY}
     *
     * @param initialCapacity Initial capacity (greater than zero and automatically
     *                        rounded to the next power of two).
     * @param loadFactor      The load factor (greater than zero and smaller than 1).
     * @param probingStrategy Strategy of next slot main.probing
     *
     * @throws IllegalArgumentException when initial capacity less 0 and greater Integer.MAX_VALUE,
     *                                  or load factor NOT in range (0, 1]
     */
    public IntLongHashMap(int initialCapacity, float loadFactor, ProbingStrategy probingStrategy) {
        this(initialCapacity, loadFactor, DEFAULT_HASH_STRATEGY, probingStrategy);
    }

    /**
     * Creates a main.hash map with the given initial capacity,
     * load factor, main.hash function and main.probing strategy
     *
     * @param initialCapacity Initial capacity (greater than zero and automatically
     *                        rounded to the next power of two).
     * @param loadFactor      The load factor (greater than zero and smaller than 1).
     * @param hashStrategy    Strategy of main.hash value computation
     * @param probingStrategy Strategy of next slot main.probing
     *
     * @throws IllegalArgumentException when initial capacity less 0 and greater Integer.MAX_VALUE,
     *                                  or load factor NOT in range (0, 1]
     */
    public IntLongHashMap(int initialCapacity, float loadFactor, HashStrategy hashStrategy,
                          ProbingStrategy probingStrategy) {

        if(initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be between (0, " + Integer.MAX_VALUE + "].");
        }

        if (!(loadFactor > 0 && loadFactor <= 1)) {
            throw new IllegalArgumentException("Load factor must be between (0, 1].");
        }

        this.loadFactor = loadFactor;
        allocateBuffers(roundCapacity(initialCapacity));
        hashContext = new HashContext(hashStrategy);
        probingContext = new ProbingContext(probingStrategy);
    }

    /**
     * {@inheritDoc}
     */
    public long put(int key, long value) {
        if (assigned >= resizeThreshold)
            expandAndRehash();

        final int mask = allocated.length - 1;
        int slot = hash(key) & mask;
        while (allocated[slot]) {
            if (((key) == (keys[slot]))) {
                final long oldValue = values[slot];
                values[slot] = value;
                return oldValue;
            }

            slot = probing(slot) & mask;
        }

        assigned++;
        allocated[slot] = true;
        keys[slot] = key;
        values[slot] = value;
        return ((long) 0);
    }

    /**
     * {@inheritDoc}
     */
    public long get(int key) {
        final int mask = allocated.length - 1;
        int slot = hash(key) & mask;
        while (allocated[slot]) {
            if (((key) == (keys[slot]))) {
                return values[slot];
            }

            slot = probing(slot) & mask;
        }
        return ((long) 0);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return assigned;
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(int key) {
        final int mask = allocated.length - 1;
        int slot = hash(key) & mask;
        while (allocated[slot]) {
            if (((key) == (keys[slot]))) {
                return true;
            }
            slot = probing(slot) & mask;
        }
        return false;
    }

    /**
     * Expand the internal storage buffers (capacity).
     */
    private void expandAndRehash() {
        final int[] oldKeys = this.keys;
        final long[] oldValues = this.values;
        final boolean[] oldStates = this.allocated;

        if (assigned >= resizeThreshold) {
            allocateBuffers(nextCapacity(keys.length));
        }

        final int mask = allocated.length - 1;
        for (int i = 0; i < oldStates.length; i++) {
            if (oldStates[i]) {
                final int key = oldKeys[i];
                final long value = oldValues[i];

                int slot = hash(key) & mask;
                while (allocated[slot]) {
                    if (((key) == (keys[slot]))) {
                        break;
                    }
                    slot = probing(slot) & mask;
                }

                allocated[slot] = true;
                keys[slot] = key;
                values[slot] = value;
            }
        }
    }

    /**
     * Allocate internal buffers for a given capacity.
     *
     * @param capacity New capacity (must be a power of two).
     */
    private void allocateBuffers(int capacity) {
        this.keys = new int[capacity];
        this.values = new long[capacity];
        this.allocated = new boolean[capacity];

        this.resizeThreshold = (int) (capacity * loadFactor);
    }

    /**
     * Hashes a Java primitive int, using strategy.
     */
    private int hash(int k) {
        return hashContext.computeHash(k);
    }

    /**
     * Probing value, according to probe strategy
     */
    private int probing(int i) {
        return probingContext.probe(i);
    }

    /**
     * Round the capacity to the next allowed value.
     */
    private int roundCapacity(int requestedCapacity) {
        // Maximum positive integer that is a power of two.
        if (requestedCapacity > (0x80000000 >>> 1)) // (0x80000000 >>> 1) == Integer.MAX_VALUE
            return Integer.MAX_VALUE;

        return Math.max(MIN_CAPACITY, nextHighestPowerOfTwo(requestedCapacity));
    }

    /**
     * Returns the next highest power of two, or the current value if it's already
     * a power of two or zero
     */
    private int nextHighestPowerOfTwo(int v) {
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++;
        return v;
    }

    /**
     * Return the next possible capacity, counting from the current buffers'
     * size.
     *
     * @throws UnsupportedOperationException when capacity must be a power of two
     * @throws IndexOutOfBoundsException     when maximum capacity exceeded
     */
    private int nextCapacity(int current) {
        if (!(current > 0 && Long.bitCount(current) == 1)) {
            throw new UnsupportedOperationException("Capacity must be a power of two.");
        }

        if (!((current << 1) > 0)) {
            throw new IndexOutOfBoundsException("Maximum capacity exceeded (" + Integer.MAX_VALUE + ").");
        }

        if (current < MIN_CAPACITY / 2) current = MIN_CAPACITY / 2;
        return current << 1;
    }

}
