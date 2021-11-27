public class Part2Main {
    public static void main(String[] args) {
        Part2ArrayProxy proxy = new Part2ArrayProxy("out.dat");
        if (proxy.instance == null) {
            System.out.println("Instance is correctly null, since no methods have been called.");
        }
        System.out.println("The value at 1,5 is " + proxy.getValue(1, 5));
        if (proxy.instance == null) {
            System.out.println("Instance should no longer be null, this is in error.");
        }
    }
}
