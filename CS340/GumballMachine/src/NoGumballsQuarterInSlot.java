public class NoGumballsQuarterInSlot implements State {
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
        machine.setCurrentState(GumballMachine.getNoGumballsNoQuarter());
    }

    @Override
    public void turnHandle(GumballMachine machine) {
        System.out.println("There are no gumballs in the machine, you cannot get any at this time.");
    }
}
