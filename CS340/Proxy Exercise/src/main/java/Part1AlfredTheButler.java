public class Part1AlfredTheButler {
    public static void main(String[] args) {
        // Tests the Breakfast Proxy
        Part1BreakfastProxy kitchen = new Part1BreakfastProxy();
        try {
            kitchen.makePancakes();
            kitchen.cookBacon();
            kitchen.toastBread();
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
