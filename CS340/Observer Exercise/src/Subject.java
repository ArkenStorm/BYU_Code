import java.util.ArrayList;

abstract public class Subject {
    ArrayList<Observer> observers = new ArrayList<>();

    public void broadcast() {
        for (Observer o : observers) {
            o.update();
        }
    }

    public void attach(Observer o) {
        observers.add(o);
    }
}
