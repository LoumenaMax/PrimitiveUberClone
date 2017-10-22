import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


/**
 * An object containing all the information in a single trip.
 * @author max
 */
public class Trip {
	public final static int WAIT_RATE = 1000;
	
	private Passenger passenger;
	private Driver driver;
	private Point start, end;
	private double fare;
	private int rating;
	private boolean running;
	
	/**
	 * Constructs a Trip 
	 * @param start The starting point of the passenger
	 * @param end Where the passenger wants to go to
	 * @param passenger The passenger who is requesting a trip
	 */
	public Trip(Point start, Point end, Passenger passenger) {
		this.start = start;
		this.end = end;
		this.passenger = passenger;
		running = true;
		rating = -1;
		fare = -1;
	}

	/**
	 * Ends the indicator that the trip is ongoing, due to unforeseen circumstances.
	 */
	public void stopRunning() {
		running = false;
	}

	/**
	 * Checks to see if this trip has finished.
	 * @return true if the trip is complete, false otherwise
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Checks the end of this trip to see if it is within the 300x300 grid.
	 * @return true if the end location is within the 300x300 grid, false otherwise.
	 */
	public boolean checkBoundaries() {
		if(end.x <= 0 || end.x > 300 ||
			end.y <= 0 || end.y > 300)
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
	
	/**
	 * @return The passenger of this trip
	 */
	public Passenger getPassenger() {
		return passenger;
	}

	/**
	 * @return The ending location of this trip in the form (x,y)
	 */
	public String getEnd() {
		return "(" + end.x + "," + end.y + ")";
	}
	
	/**
	 * @return The starting location of this trip in the form (x,y)
	 */
	public String getStart() {
		return "(" + start.x + "," + start.y + ")";
	}
	
	/**
	 * @return The amount that this trip will cost the passenger.
	 */
	public double getFare() {
		return fare;
	}

	/**
	 * Executes the trip. Sets a time based on the distance and WAIT_TIME for the program to wait for.
	 * Changes the values of the driver and passenger based on the end location of the trip.
	 */
	public void execute() {
		System.out.println(passenger + ", " + driver + " is on his way.");
		long ETAToPickUp = (long)(WAIT_RATE * passenger.getDistance(driver));
		long ETAToDropOff = (long)(WAIT_RATE * passenger.getDistance(end));
		System.out.println("ETA to pick up: " + (ETAToPickUp / 1000) + " seconds.");
		TimerTask pickUp = new TimerTask() {
			public void run() {
				System.out.println(passenger + ", " + driver + " has arrived at your location.");
				driver.updateLocation(start);
			}
		};
		TimerTask dropOff = new TimerTask() {
			public void run() {
				System.out.println(driver + " has arrived at the destination.");
				askPassenger();
				driver.endTrip();
				passenger.updateLocation(end);
				driver.updateLocation(end);
				running = false;
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(pickUp, ETAToPickUp);
		timer.schedule(dropOff, ETAToPickUp + ETAToDropOff);
	}
	
	/**
	 * Asks the passenger of this trip to rate the driver of this trip. Updates the trip object and the drivers rating list.
	 */
	public void askPassenger() {
		Scanner input = new Scanner(System.in);
		while(rating == -1) {
			System.out.println(passenger + ", how was your experience with " + driver + "?(1-5)");
			switch(input.next()) {
				case "1":
					driver.addRating(1);
					rating = 1;
					break;
				case "2":
					driver.addRating(2);
					rating = 2;
					break;
				case "3":
					driver.addRating(3);
					rating = 3;
					break;
				case "4":
					driver.addRating(4);
					rating = 4;
					break;
				case "5":
					driver.addRating(5);
					rating = 5;
					break;
				default:
					System.out.println("We need a number between 1 and 5.");
			}
		}
	}
	
	/**
	 * Adds the driver to the trip object, calculates the fare based on the drivers location and the given end point, and conducts a transaction.
	 * @param driver The driver that will take the passenger to the end location.
	 * @return	true if the passenger can pay the fare, false otherwise.
	 */
	public boolean addDriver(Driver driver) {
		this.driver = driver;
		this.fare = Client.PAYMENT_RATE * (driver.getDistance(passenger) + driver.getDistance(end));
		return passenger.transaction(driver, fare);
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
			b.write("   Start: " + "(" + 
					start.x + "," + start.y + ")");
			b.newLine();
			b.write("   End: " + "(" + 
					end.x + "," + end.y + ")");
			b.newLine();
			if(rating != -1) {
				b.write("   Rating: " + rating);
				b.newLine();
			}
			if(driver != null) {
				b.write("   Driver Average Rating: " + driver.getRating());
				b.newLine();
				b.write("   Driver Balance: " + driver.getBalance());
				b.newLine();
			}
			b.write("   Passenger Balance: " + passenger.getBalance());
			b.newLine();
			if(fare != -1) {
				b.write("   Fare: " + fare);
				b.newLine();
			}
			b.flush();
			b.close();
		}
		catch(IOException e) {
			System.out.println("There was a problem with logging this trip.");
			e.printStackTrace();
		}
	}
}
