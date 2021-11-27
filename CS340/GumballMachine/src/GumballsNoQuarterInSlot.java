public class GumballsNoQuarterInSlot implements State {
    @Override
    public void addGumballs(int number, GumballMachine machine) {
        if (number < 0) {
            System.out.println("You cannot add a negative amount of gumballs.");
            return;
        }
        machine.setNumberOfGumballs(machine.getNumberOfGumballs() + number);
        machine.setCurrentState(GumballMachine.getGumballsNoQuarter());
    }

    @Override
    public void insertQuarter(GumballMachine machine) {
        System.out.println("Quarter inserted.");
        machine.setCurrentState(GumballMachine.getGumballsYesQuarter());
    }

    @Override
    public void removeQuarter(GumballMachine machine) {
        System.out.println("There is not currently a quarter in the slot.");
    }

    @Override
    public void turnHandle(GumballMachine machine) {
        System.out.println("There is currently no quarter in the slot.");
    }
}
