package hash;

public class ConcreteHashHPPC implements HashStrategy {

    /**
     * This hash function uses HPPC for hashing 4-bit Java int.
     */
    public int hash(int k) {
        k ^= k >>> 16;
        k *= 0x85ebca6b;
        k ^= k >>> 13;
        k *= 0xc2b2ae35;
        k ^= k >>> 16;

        return k;
    }
}
