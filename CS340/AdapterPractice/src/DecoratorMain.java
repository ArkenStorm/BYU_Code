public class DecoratorMain {
    public static void main(String[] args) {
        Decorator d1 = new Reverser(new BeeMovie());
        System.out.println(d1.next());

        Decorator d2 = new Capitalizer(new TotallyNotARickRoll());
        System.out.println(d2.next());

        Decorator d3 = new CharacterInserter(new TotallyNotARickRoll(), '-');
        Decorator d4 = new Capitalizer(d3);
        System.out.println(d4.next());
    }
}
