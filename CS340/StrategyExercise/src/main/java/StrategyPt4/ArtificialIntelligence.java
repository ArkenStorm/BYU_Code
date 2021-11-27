package StrategyPt4;

public class ArtificialIntelligence {
    Strategy strategy;

    public ArtificialIntelligence(Strategy strategy) {
        this.strategy = strategy;
    }

    public void playerBeatdown() {
        strategy.makeMove();
    }
}
