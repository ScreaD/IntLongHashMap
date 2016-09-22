package probing;

public class ProbingContext {

    private ProbingStrategy probingStrategy;

    public ProbingContext(ProbingStrategy probingStrategy) {
        this.probingStrategy = probingStrategy;
    }

    // Set new strategy
    public void setProbingStrategy(ProbingStrategy probingStrategy) {
        this.probingStrategy = probingStrategy;
    }

    public int probe(int i) {
        return probingStrategy.probe(i);
    }
}