import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


/**
 * An object containing all the information in a single trip.
 * @author max
 */
public class Trip {
	
	private Passenger passenger;
	private Driver driver;
	private Point start, end;
	private Transaction transaction;
	private int rating;
	
	/**
	 * Constructs a Trip 
	 * @param end Where the passenger wants to go to
	 * @param passenger The passenger who is requesting a trip
	 */
	public Trip(Point end, Passenger passenger) {
		this.start = passenger.getLocation();
		this.end = end;
		this.passenger = passenger;
		passenger.startTrip();
		rating = -1;
		transaction = null;
	}
	
	/**
	 * Checks the end of this trip to see if it is within the 300x300 grid.
	 * @return true if the end location is within the 300x300 grid, false otherwise.
	 */
	public boolean checkBoundaries() {
		if(end.x <= 0 || end.x > Uber.GRID_HEIGHT ||
			end.y <= 0 || end.y > Uber.GRID_HEIGHT)
			return false;
		return true;
	}
	
	/**
	 * @return True if the trip fully completed, false otherwise
	 */
	public boolean isSuccessful() {
		if(rating == -1)
			return false;
		return true;
	}
	
	/** Method to retrieve the passenger of the trip
	 * @return The passenger of this trip
	 */
	public Passenger getPassenger() {
		return passenger;
	}
	
	/** Method to retrieve the driver of the trip
	 * @return The driver of this trip
	 */
	public Driver getDriver() {
		return driver;
	}

	/** MEthod to retrieve a string of the ending location of the trip.
	 * @return The ending location of this trip in the form (x,y)
	 */
	public String getEnd() {
		return "(" + end.x + "," + end.y + ")";
	}
	
	/** Method to retrieve the ending location of the trip.
	 * @return The ending location of this trip in the form (x,y)
	 */
	public Point getEndPoint() {
		return end;
	}
	
	/** Method to retrieve the starting location of the trip.
	 * @return The starting location of this trip in the form (x,y)
	 */
	public Point getStartPoint() {
		return start;
	}
	
	/** Method to retrieve a string of the starting location of the trip.
	 * @return The starting location of this trip in the form (x,y)
	 */
	public String getStart() {
		return "(" + start.x + "," + start.y + ")";
	}
	
	/**
	 * Method to obtain the transaction object
	 * @return The transaction object for this trip
	 */
	public Transaction getTransaction() {
		return transaction;
	}
	
	/**
	 * Asks the passenger of this trip to rate the driver of this trip. Updates the trip object and the drivers rating list.
	 */
	public void askPassenger() {
		System.out.println(passenger + ", how was your experience with " + driver + "?(1-5)");
		Random rand = new Random();
		rating = rand.nextInt(5) + 1;
		System.out.println(rating);
		driver.addRating(rating);
	}
	
	/**
	 * Adds the driver to the trip object and calculates the fare based on the drivers location and the given end point.
	 * @param driver The driver that will take the passenger to the end location.
	 */
	public void addDriver(Driver driver) {
		this.driver = driver;
		this.transaction = new Transaction(
				passenger.getWallet(),
				driver.getWallet(),
				Uber.PAYMENT_RATE * (driver.getDistance(passenger) + driver.getDistance(end)));
	}
	
	/** Logs this trips information into a file.
	 * @param filename The filename that we will write the information of this trip into.
	 */
	public void logTrip(String filename) {
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(filename, true));
			b.write(passenger + "\'s trip with " + driver);
			b.newLine();
			if(rating != -1) {
				b.write("Trip was successfully completed.");
				b.newLine();
			}
			else {
				b.write("Trip was not successfully completed.");
				b.newLine();
			}
			b.write("   Start: " + getStart());
			b.newLine();
			b.write("   End: " + getEnd());
			b.newLine();
			if(rating != -1) {
				b.write("   Rating: " + rating);
				b.newLine();
			}
			if(driver != null) {
				b.write("   Driver Average Rating: " + driver.getRating());
				b.newLine();
				b.write("   Driver Balance: " + driver.getWallet());
				b.newLine();
			}
			b.write("   Passenger Balance: " + passenger.getWallet());
			b.newLine();
			if(transaction != null) {
				b.write("   Fare: " + String.format("%.2f", transaction.getAmount()));
				b.newLine();
			}
			b.newLine();
			b.flush();
			b.close();
		}
		catch(IOException e) {
			System.out.println("There was a problem with logging this trip.");
			e.printStackTrace();
		}
	}
}
