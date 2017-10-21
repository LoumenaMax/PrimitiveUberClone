import java.awt.Point;
import java.util.PriorityQueue;


public class Passenger extends Person{
	Client client;
	
	public Passenger(String name, double balance, Client client, Point location) {
		super(name, balance, location);
		this.client = client;
	}
	
	public void request(Point end) {
		Trip newTrip = new Trip(location, end, this);
		client.addTrip(newTrip);
	}
	
	public void rate(Driver d1) {
		
	}
}
