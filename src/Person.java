import java.awt.Point;
import java.util.Random;


/**
 * A class detailing all the parameters and functions that any person could make use of.
 * @author max
 */
public abstract class Person {
	protected String name;
	protected double balance;
	protected Point location;
	
	/**
	 * @param name The name of the person you wish to make a Person object of
	 * @param balance The amount of money this Person will start with
	 */
	public Person(String name, double balance) {
		this.name = name;
		this.balance = balance;
		Random ran = new Random();
		this.location = new Point(ran.nextInt(300), ran.nextInt(300));
	}
	
	/**
	 * @return The current balance of this person
	 */
	public double getBalance() {
		return balance;
	}

	/** Method to retrieve the Persons location
	 * @return The location of this object
	 */
	public Point getLocation() {
		return location;
	}
	
	/** A toString for the location of this object
	 * @return The location of this object in the form (x,y)
	 */
	public String getLocationString() {
		return "(" + location.x + "," + location.y + ")";
	}
	
	public void updateLocation(Point location) {
		this.location = location;
	}
	
	/** Removes the amount from your balance
	 * @param amount The amount removed from your balance
	 * @return The new balance of this object
	 */
	public double pay(double amount) {
		if(amount > balance)
			return -1;
		balance -= amount;
		return balance;
	}
	
	/** Adds the amount given to your balance
	 * @param amount The amount added to your balance
	 * @return The new balance of this object
	 */
	public double receive(double amount) {
		balance += amount;
		return balance;
	}
	
	/** Checks your balance against the amount given.
	 * @param amount The amount to check your balance against
	 * @return True if you have enough money to make such a purchase, false otherwise.
	 */
	public boolean checkBalance(double amount) {
		if(balance < amount) {
			return false;
		}
		return true;
	}

	/**
	 * Pays a set amount to person p2.
	 * @param p2 The person who will receive the amount specified, minus Uber's cut.
	 * @param amount The amount to transfer to p2, minus Uber's cut.
	 * @return True if the transaction was handled successfully, false otherwise.
	 */
	public boolean transaction(Person p2, double amount) {
		if(!checkBalance(amount))
			return false;
		pay(amount);
		p2.receive(amount * Uber.DRIVER_SHARE);
		return true;
	}
	
	/** Gets the distance between two Person objects
	 * @param p1 The person you will find the distance to
	 * @return A double that is the distance from the given Person object to this Person object.
	 */
	public double getDistance(Person p1) {
		return Math.sqrt(Math.pow((location.x - p1.getLocation().x), 2) + Math.pow((location.y - p1.getLocation().y), 2));
	}
	/** Gets the distance from the point specified to this Person object
	 * @param p1 The point you will find the distance to
	 * @return A double that is the distance from the Person object to the point.
	 */
	public double getDistance(Point p1) {
		return Math.sqrt(Math.pow((location.x - p1.x), 2) + Math.pow((location.y - p1.y), 2));
	}
	

	/**
	 * @return The name of the Person Object
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}
