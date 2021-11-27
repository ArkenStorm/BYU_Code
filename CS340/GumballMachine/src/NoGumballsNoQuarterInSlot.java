public class NoGumballsNoQuarterInSlot implements State {
    @Override
    public void addGumballs(int number, GumballMachine machine) {
        if (number <= 0) {
            System.out.println("You must add a positive amount of gumballs.");
            return;
        }
        machine.setNumberOfGumballs(machine.getNumberOfGumballs() + number);
        machine.setCurrentState(GumballMachine.getGumballsNoQuarter());
    }

    @Override
    public void insertQuarter(GumballMachine machine) {
        System.out.println("Quarter inserted.");
        machine.setCurrentState(GumballMachine.getNoGumballsYesQuarter());
    }

    @Override
    public void removeQuarter(GumballMachine machine) {
        System.out.println("There is not currently a quarter in the slot.");
    }

    @Override
    public void turnHandle(GumballMachine machine) {
        System.out.println("There are currently no gumballs in the machine and no quarter in the slot.");
    }
}
