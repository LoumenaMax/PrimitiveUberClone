import java.awt.Point;
import java.util.PriorityQueue;


public class Passenger extends Person{
	Client client;
	
	public Passenger(String name, double balance, Client client) {
		super(name, balance);
		this.client = client;
	}
	
	public void request(Point end) {
		Trip newTrip = new Trip(location, end, this);
		client.addTrip(newTrip);
	}
}