public interface State {
    void addGumballs(int number, GumballMachine machine);
    void insertQuarter(GumballMachine machine);
    void removeQuarter(GumballMachine machine);
    void turnHandle(GumballMachine machine);
}
