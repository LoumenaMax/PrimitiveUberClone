/**
 * A class detailing a user and passenger of Uber.
 * @author max
 */
public class Passenger extends Person{
	
	private boolean inTrip;
	
	/**
	 * @param name The Passenger's name
	 * @param balance The amount of money the Passenger has.
	 * @param client The interface between the Passenger and the Drivers.
	 */
	public Passenger(String name, double balance) {
		super(name, balance);
		inTrip = false;
	}

	/** Returns whether the passenger has already requested a trip.
	 * @return True if the passenger already requested a trip, false otherwise
	 */
	public boolean inTrip() {
		return inTrip;
	}
	
	/**
	 * Called when the passenger requests a trip, so he can't request multiple trips.
	 */
	public void startTrip() {
		inTrip = true;
	}
	
	/**
	 * Called when the passenger finishes a trip, so he can request trips again.
	 */
	public void endTrip() {
		inTrip = false;
	}
}