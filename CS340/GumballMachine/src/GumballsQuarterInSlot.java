public class GumballsQuarterInSlot implements State {
    @Override
    public void addGumballs(int number, GumballMachine machine) {
        if (number < 0) {
            System.out.println("You cannot add a negative amount of gumballs.");
            return;
        }
        machine.setNumberOfGumballs(machine.getNumberOfGumballs() + number);
        machine.setCurrentState(GumballMachine.getGumballsYesQuarter());
    }

    @Override
    public void insertQuarter(GumballMachine machine) {
        System.out.println("There is already a quarter in the slot.");
    }

    @Override
    public void removeQuarter(GumballMachine machine) {
        System.out.println("Quarter removed.");
        machine.setCurrentState(GumballMachine.getGumballsNoQuarter());
    }

    @Override
    public void turnHandle(GumballMachine machine) {
        System.out.println("Gumball dispensed!");
        int numBalls = machine.getNumberOfGumballs();
        machine.setNumberOfGumballs(numBalls - 1);
        if (numBalls - 1 == 0) {
            machine.setCurrentState(GumballMachine.getNoGumballsNoQuarter());
            System.out.println("There are no more gumballs left.");
        }
        else {
            machine.setCurrentState(GumballMachine.getGumballsNoQuarter());
        }
        machine.setProfit(machine.getProfit() + .25);
    }
}
