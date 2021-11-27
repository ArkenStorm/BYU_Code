public class CharacterInserter extends Decorator {
    private char insertion;

    public CharacterInserter(StringSource source) {
        super(source);
    }

    public CharacterInserter(StringSource source, char c) {
        super(source);
        insertion = c;
    }

    @Override
    public String alterString(String s) {
        String[] split = s.split("");
        return String.join(Character.toString(insertion), split);
    }
}
