public class Capitalizer extends Decorator {
    public Capitalizer(StringSource source) {
        super(source);
    }

    @Override
    public String alterString(String s) {
        return s.toUpperCase();
    }
}
