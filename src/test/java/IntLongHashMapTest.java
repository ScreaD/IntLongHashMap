import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IntLongHashMapTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldIllegalArgumentException_whenPassIrrelevantSize() {
        // given
        int irrelevantSize = -1;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Initial capacity must be between (0, " + Integer.MAX_VALUE + "].");
        IntLongMap map = new IntLongHashMap(irrelevantSize);
    }

    @Test
    public void shouldIllegalArgumentException_whenIrrelevantLoadFactor() {
        float irrelevantLoadFactor = 50f;

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Load factor must be between (0, 1].");
        new IntLongHashMap(IntLongHashMap.DEFAULT_CAPACITY, irrelevantLoadFactor);
    }

    @Test
    public void shouldWorksProperly_whenAllotOperationsPerformed() {
        IntLongMap map = new IntLongHashMap();
        int maxIterations = 5000;

        for (int i = 0; i < maxIterations; i++) {
            map.put(i, i * i);
        }

        for (int i = 0; i < maxIterations; i++) {
            assertEquals(map.get(i), i * i);
        }
    }

    @Test
    public void shouldBeAddedValue_whenPutValue() {
        Random random = new Random();
        int key = random.nextInt();
        long value = random.nextLong();
        IntLongMap map = new IntLongHashMap(IntLongHashMap.MIN_CAPACITY);

        map.put(key, value);

        assertEquals(value, map.get(key));
    }

    @Test
    public void shouldProperlyResolved_whenBoundaryValuesAdded() {
        IntLongMap map = new IntLongHashMap(IntLongHashMap.MIN_CAPACITY);

        map.put(Integer.MIN_VALUE, Long.MIN_VALUE);
        map.put(Integer.MAX_VALUE, Long.MAX_VALUE);

        assertEquals(map.get(Integer.MIN_VALUE), Long.MIN_VALUE);
        assertEquals(map.get(Integer.MAX_VALUE), Long.MAX_VALUE);
    }

    @Test
    public void shouldZeroSize_whenEmptyMap() {
        IntLongMap map = new IntLongHashMap();

        assertEquals(map.size(), 0);
    }

    @Test
    public void shouldSameSize_whenValueOverrided() {
        IntLongMap map = new IntLongHashMap(IntLongHashMap.MIN_CAPACITY);
        Random random = new Random();
        int key = random.nextInt();
        long oldValue = random.nextLong();
        long newValue = random.nextLong();

        map.put(key, oldValue);
        map.put(key, newValue);

        assertEquals(map.size(), 1);
    }

    @Test
    public void shouldProperlySize_whenPuttedMap() {
        Random random = new Random();
        IntLongMap map = new IntLongHashMap(IntLongHashMap.MIN_CAPACITY);

        int expectedSize = 0;
        int key = random.nextInt();
        long value = random.nextLong();

        assertEquals(map.size(), expectedSize);

        map.put(key, value);
        expectedSize++;

        assertEquals(map.size(), expectedSize);
    }

    @Test
    public void shouldResize_whenMapIsFull() {
        Random random = new Random();
        IntLongMap map = new IntLongHashMap(IntLongHashMap.MIN_CAPACITY);
        int expectedSize = IntLongHashMap.MIN_CAPACITY + 1;

        for (int i = 0; i < expectedSize; i++) {
            map.put(random.nextInt(), random.nextLong());
        }

        assertEquals(map.size(), expectedSize);
    }

    @Test
    public void shouldOverrideValue_whenKeysAreEquals() {
        IntLongMap map = new IntLongHashMap(IntLongHashMap.MIN_CAPACITY);
        Random random = new Random();
        int key = random.nextInt();
        long oldValue = random.nextLong();
        long newValue = random.nextLong();

        map.put(key, oldValue);
        map.put(key, newValue);

        assertEquals(map.get(key), newValue);
    }

    @Test
    public void shouldLongDefaultValueReturned_whenGetNonExistentKey() {
        IntLongMap map = new IntLongHashMap();

        assertEquals(map.get(new Random().nextInt()), 0L);
    }

    @Test
    public void shouldTrue_whenContainsKey() {
        Random random = new Random();
        int key = random.nextInt();
        long value = random.nextLong();
        IntLongMap map = new IntLongHashMap();

        map.put(key, value);

        assertTrue(map.containsKey(key));
    }

    @Test
    public void shouldFalse_whenKeyDoesntExistInMap() {
        IntLongMap map = new IntLongHashMap();

        assertFalse(map.containsKey(new Random().nextInt()));
    }
}