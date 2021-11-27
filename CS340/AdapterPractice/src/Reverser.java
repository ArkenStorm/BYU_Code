public class Reverser extends Decorator {

    public Reverser(StringSource source) {
        super(source);
    }

    @Override
    public String alterString(String s) {
        StringBuilder builder = new StringBuilder(s);
        return builder.reverse().toString();
    }
}