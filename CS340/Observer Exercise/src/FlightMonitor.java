
public class FlightMonitor {
	
	public static void main(String[] args) {
	
		FlightFeed feed = new FlightFeed();
		feed.attach(new DisplayStatus(feed));
		feed.attach(new DisplayDelta(feed));
		feed.start();
	}
	
}