import java.util.Scanner;

public class GumballMachine {
    private int numberOfGumballs;
    private double profit;
    private State currentState = noGumballsNoQuarter;
    private static final NoGumballsNoQuarterInSlot noGumballsNoQuarter = new NoGumballsNoQuarterInSlot();
    private static final NoGumballsQuarterInSlot noGumballsYesQuarter = new NoGumballsQuarterInSlot();
    private static final GumballsNoQuarterInSlot gumballsNoQuarter = new GumballsNoQuarterInSlot();
    private static final GumballsQuarterInSlot gumballsYesQuarter = new GumballsQuarterInSlot();

    private static void printMenu() {
        System.out.println("Options: ");
        System.out.println("1. Add gumballs");
        System.out.println("2. Insert a quarter");
        System.out.println("3. Remove a quarter");
        System.out.println("4. Turn the handle");
        System.out.println("5. Quit");
    }

    public static void main(String[] args) {
        GumballMachine machine = new GumballMachine();
        Scanner scanner = new Scanner(System.in);
        printMenu();
        boolean active = true;
        while (active) {
            switch (scanner.next()){
                case "1":
                    System.out.println("How many?");
                    machine.addGumballs(scanner.nextInt());
                    break;
                case "2":
                    machine.insertQuarter();
                    break;
                case "3":
                    machine.removeQuarter();
                    break;
                case "4":
                    machine.turnHandle();
                    break;
                case "5":
                    active = false;
                    break;
            }
            printMenu();
        }
    }

    private void addGumballs(int number) {
        currentState.addGumballs(number, this);
    }

    private void insertQuarter() {
        currentState.insertQuarter(this);
    }

    private void removeQuarter() {
        currentState.removeQuarter(this);
    }

    private void turnHandle() {
        currentState.turnHandle(this);
    }

    public int getNumberOfGumballs() {
        return numberOfGumballs;
    }

    public void setNumberOfGumballs(int numberOfGumballs) {
        this.numberOfGumballs = numberOfGumballs;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public static NoGumballsNoQuarterInSlot getNoGumballsNoQuarter() {
        return noGumballsNoQuarter;
    }

    public static NoGumballsQuarterInSlot getNoGumballsYesQuarter() {
        return noGumballsYesQuarter;
    }

    public static GumballsNoQuarterInSlot getGumballsNoQuarter() {
        return gumballsNoQuarter;
    }

    public static GumballsQuarterInSlot getGumballsYesQuarter() {
        return gumballsYesQuarter;
    }
}
