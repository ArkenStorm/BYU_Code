package StrategyPt1;

public class TextBox {
    ValidatorInterface validator;

    public TextBox(ValidatorInterface validator) {
        this.validator = validator;
    }

    public boolean checkText(String input) {
        return validator.validate(input);
    }
}
