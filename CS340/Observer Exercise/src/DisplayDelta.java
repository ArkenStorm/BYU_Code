public class DisplayDelta implements Observer {
    FlightFeed flightFeed;
    Flight oldFlight;

    public DisplayDelta(FlightFeed f) {
        flightFeed = f;
    }

    @Override
    public void update() {
        if (oldFlight == null) {
            oldFlight = flightFeed.getFlight();
        }
        else {
            System.out.println(deltas());
        }
    }

    public String deltas() {
        Flight newFlight = flightFeed.getFlight();
        float longitude = java.lang.Math.abs(oldFlight.longitude - newFlight.longitude);
        float latitude = java.lang.Math.abs(oldFlight.latitude - newFlight.latitude);
        float velocity = java.lang.Math.abs(oldFlight.velocity - newFlight.velocity);
        float altitude = java.lang.Math.abs(oldFlight.geo_altitude - newFlight.geo_altitude);
        oldFlight = newFlight;

        return String.format("Deltas - lon: %f, lat: %f, vel: %f, alt: %f",
                longitude, latitude, velocity, altitude);
    }
}
