import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class Trip {
	public final static int WAIT_RATE = 1000;
	
	private Passenger passenger;
	private Driver driver;
	private Point start, end;
	private double fare;
	private int rating;
	
	public Trip(Point start, Point end, Passenger passenger) {
		this.start = start;
		this.end = end;
		this.passenger = passenger;
		rating = -1;
		fare = -1;
	}
	
	public boolean checkBoundaries(Point location) {
		if(end.x <= 0 || end.x > 300 ||
			end.y <= 0 || end.y > 300)
			return false;
		return true;
	}
	
	public boolean isSuccessful() {
		if(rating == -1)
			return false;
		return true;
	}
	
	public Passenger getPassenger() {
		return passenger;
	}
	
	public Point getEnd() {
		return end;
	}
	
	public double getFare() {
		return fare;
	}
	
	public boolean askDriver(Driver driver) {
		Scanner input = new Scanner(System.in);
		while(true) {
			System.out.println(driver + ", would you like to accept " + passenger + "\'s ride from (" + 
					start.x + "," + start.y + ") to (" +
					end.x + "," + end.y + ")?(y\\n)");
			switch(input.next()) {
				case "yes":
					driver.acceptRequest();
					return true;
				case "y":
					driver.acceptRequest();
					return true;
				case "no":
					return false;
				case "n":
					return false;
				default:
					System.out.println("We need a yes(yes|y) or no(no|n) answer.");
			}
		}
	}

	public void execute() {
		System.out.println(passenger + ", " + driver + " is on his way.");
		long ETAToPickUp = (long)(WAIT_RATE * passenger.getDistance(driver));
		long ETAToDropOff = (long)(WAIT_RATE * passenger.getDistance(end));
		System.out.println("ETA to pick up: " + (ETAToPickUp / 1000) + " seconds.");
		TimerTask pickUp = new TimerTask() {
			public void run() {
				System.out.println(driver + " has arrived at your location.");
			}
		};
		TimerTask dropOff = new TimerTask() {
			public void run() {
				System.out.println(driver + " has arrived at the destination.");
				askPassenger();
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(pickUp, ETAToPickUp);
		timer.schedule(dropOff, ETAToPickUp + ETAToDropOff);
	}
	
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
