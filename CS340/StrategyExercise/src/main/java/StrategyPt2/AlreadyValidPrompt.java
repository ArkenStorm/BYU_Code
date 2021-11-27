package StrategyPt2;

public class AlreadyValidPrompt implements Prompter {
    @Override
    public boolean validationPrompt() {
        return true;
    }
}
