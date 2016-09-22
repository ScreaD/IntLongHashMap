package hash;

public class HashContext {

    private HashStrategy hashStrategy;

    public HashContext(HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    // Set new strategy
    public void setHashStrategy(HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    public int computeHash(int k) {
        return hashStrategy.hash(k);
    }
}
