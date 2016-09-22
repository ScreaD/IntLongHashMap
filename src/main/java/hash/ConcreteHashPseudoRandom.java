package hash;

public class ConcreteHashPseudoRandom implements HashStrategy {

    /**
     * Hash function proposed my "mikera" at https://goo.gl/i4NwGO
     */
    public int hash(int a) {
        a ^= (a << 13);
        a ^= (a >>> 17);
        a ^= (a << 5);
        return a;
    }
}
