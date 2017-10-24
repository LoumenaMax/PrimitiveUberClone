import java.awt.Point;
import java.util.Random;


/**
 * A class detailing all the parameters and functions that any person could make use of.
 * @author max
 */
public abstract class Person {
	protected String name;
	protected Wallet wallet;
	protected Point location;
	
	/**
	 * @param name The name of the person you wish to make a Person object of
	 * @param wallet The amount of money this Person will start with
	 */
	public Person(String name, Wallet wallet) {
		this.name = name;
		this.wallet = wallet;
		Random ran = new Random();
		this.location = new Point(ran.nextInt(300), ran.nextInt(300));
	}
	
	/** Method to retrieve the wallet of the person
	 * @return The current balance of this person
	 */
	public Wallet getWallet() {
		return wallet;
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
