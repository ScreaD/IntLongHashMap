package probing;

public class QuadraticProbing implements ProbingStrategy {

    public int probe(int i) {
        return (i + i * i);
    }
}
