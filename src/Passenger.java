/**
 * A class detailing a user and passenger of Uber.
 * @author max
 */
public class Passenger extends Person{
	
	private boolean inTrip;
	
	/**
	 * @param name The Passenger's name
	 * @param wallet The amount of money the Passenger has.
	 */
	public Passenger(String name, Wallet wallet) {
		super(name, wallet);
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