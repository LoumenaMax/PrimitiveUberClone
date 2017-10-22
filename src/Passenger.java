import java.awt.Point;


/**
 * A class detailing a user and passenger of Uber.
 * @author max
 */
public class Passenger extends Person{
	Client client;
	
	/**
	 * @param name The Passenger's name
	 * @param balance The amount of money the Passenger has.
	 * @param client The interface between the Passenger and the Drivers.
	 */
	public Passenger(String name, double balance, Client client) {
		super(name, balance);
		this.client = client;
	}
	
	/** Sends a request to the Passenger's client for a pickup and transport.
	 * @param end The location this Passenger wishes to go to.
	 */
	public void request(Point end) {
		Trip newTrip = new Trip(location, end, this);
		client.addTrip(newTrip);
	}
}