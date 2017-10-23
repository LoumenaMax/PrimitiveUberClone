import java.util.ArrayList;
import java.util.Random;

/**
 * A class detailing an Uber driver
 * @author max
 */
public class Driver extends Person{

	@SuppressWarnings("unused")
	private String carTitle;
	private boolean available;
	private ArrayList<Integer> ratings;
	
	/**
	 * @param name The name of the driver.
	 * @param balance The amount of money this driver will start with.
	 * @param carTitle The title of the car.
	 */
	public Driver(String name, Wallet wallet, String carTitle) {
		super(name, wallet);
		this.carTitle = carTitle;
		this.available = true;
		ratings = new ArrayList<Integer>();
	}
	
	/** Adds a rating to this drivers list of ratings.
	 * @param rating The value of the rating you will add to this drivers list of ratings.
	 */
	public void addRating(Integer rating) {
		ratings.add(rating);
	}

	/**
	 * To be called when the driver has taken a Passenger, and cannot take other Passengers.
	 */
	public void acceptRequest() {
		available = false;
	}
	
	/**
	 * To be called when the driver has finished with a Passenger, and wishes to become available again.
	 */
	public void endTrip() {
		available = true;
	}
	
	/** Returns the availability of this driver.
	 * @return True if the driver is available to take Passengers, false otherwise.
	 */
	public boolean available() {
		return available;
	}
	
	/** Takes the average from the list of ratings of this driver.
	 * @return The average rating of this driver
	 */
	public double getRating() {
		double average = 0;
		if(ratings.size() == 0) {
			return 5;
		}
		for(Integer rating : ratings) {
			average += rating;
		}
		return (average / ratings.size());
	}
	
	
	/** Asks a driver specified by driver to accept a trip specified by trip.
	 * @param driver The driver you will ask to accept a ride.
	 * @param trip The trip you will retrieve information about the ride from
	 * @return True if the driver accepts, False otherwise.
	 */
	public boolean askDriver(Trip trip) {
		Random rand = new Random();
		System.out.println(name + ", would you like to accept " + trip.getPassenger() + "\'s ride from " + 
			trip.getStart() + " to " + 
			trip.getEnd() + "?(y\\n)");
		if(rand.nextInt()%2 == 0 || rand.nextInt()%2==0) {
			acceptRequest();
			return true;
		}
		return false;
	}
}
