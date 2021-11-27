package StrategyPt2;

public class EmailClient {
    Prompter validationPrompter;

    public EmailClient(Prompter validationPrompter) {
        this.validationPrompter = validationPrompter;
    }

    public void sendMessage() {
        validationPrompter.validationPrompt();
    }
}
