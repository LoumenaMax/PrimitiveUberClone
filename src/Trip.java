import java.awt.Point;


public class Trip {
	private Passenger passenger;
	private Driver driver;
	private Point start, end;
	private double fare;
	private int rating;
	
	public Trip(Point start, Point end, Passenger passenger) {
		this.start = start;
		this.end = end;
		this.passenger = passenger;
	}
	
	public Passenger getPassenger() {
		return passenger;
	}
	
	public boolean addDriver(Driver driver) {
		this.driver = driver;
		this.fare = Client.PAYMENT_RATE * (driver.getDistance(passenger) + driver.getDistance(end));
		return passenger.checkBalance(fare);
	}
}
