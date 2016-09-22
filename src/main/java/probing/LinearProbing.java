package probing;

public class LinearProbing implements ProbingStrategy {

    public int probe(int i) {
        return i + 1;
    }
}
