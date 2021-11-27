public class DisplayStatus implements Observer {
    private FlightFeed flightFeed;

    public DisplayStatus(FlightFeed f) {
        flightFeed = f;
    }

    @Override
    public void update() {
        Flight flight = flightFeed.getFlight();
        System.out.println(flight.toString());
    }
}