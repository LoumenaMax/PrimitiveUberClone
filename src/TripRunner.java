/**
 * Makes a Trip object runnable.
 * @author max
 *
 */
public class TripRunner implements Runnable {

	private Trip trip;
	
	/** Creates a TripRuner object
	 * @param trip The trip that contains information for the TripRunner
	 */
	public TripRunner(Trip trip) {
		this.trip = trip;
	}
	
	/**
	 * @param passenger The passenger that will embark upon the trip.
	 * @param driver
	 * @throws InterruptedException
	 */
	public void runTimer(final Passenger passenger, final Driver driver) throws InterruptedException {
		System.out.println(passenger + ", " + driver + " is on his way.");
		long ETAToPickUp = (long)(Uber.WAIT_RATE * passenger.getDistance(driver));
		long ETAToDropOff = (long)(Uber.WAIT_RATE * passenger.getDistance(trip.getEndPoint()));
		System.out.println("ETA to pick up: " + (ETAToPickUp / 1000) + " seconds.");
		Thread.sleep(ETAToPickUp);
		System.out.println(passenger + ", " + driver + " has arrived at your location.");
		driver.updateLocation(trip.getStartPoint());
		Thread.sleep(ETAToDropOff);
		System.out.println(driver + " has arrived at the destination.");
		trip.askPassenger();
		driver.endTrip();
		passenger.updateLocation(trip.getEndPoint());
		driver.updateLocation(trip.getEndPoint());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if(trip.getTransaction().makePayment()) {
			System.out.println(trip.getPassenger() + ", your transaction of " + trip.getTransaction() + " to " + trip.getDriver() + " has been successfully completed.");
		}
		else {
			System.out.println(trip.getPassenger() + " does not have enough to complete the transaction." +
					"\n   " + trip.getPassenger() + " has: " + trip.getPassenger().getWallet() +
					"\n   Transaction Amount: " + trip.getTransaction());
			return;
		}
		try {
			runTimer(trip.getPassenger(), trip.getDriver());
		} catch (InterruptedException e) {
		}
		trip.getPassenger().endTrip();
	}
}
