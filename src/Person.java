import java.awt.Point;
import java.util.Random;


public abstract class Person {
	protected String name;
	protected double balance;
	protected Point location;
	
	public Person(String name, double balance) {
		this.name = name;
		this.balance = balance;
		Random ran = new Random();
		this.location = new Point(ran.nextInt(300), ran.nextInt(300));
	}
	
	public void changeLocation(int x, int y) {
		location = new Point(x, y);
	}
	
	public String getName() {
		return name;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public int getX() {
		return location.x;
	}
	
	public int getY() {
		return location.y;
	}
	
	public String getLocation() {
		return "(" + location.x + "," + location.y + ")";
	}
	
	public double pay(double amount) {
		if(amount > balance)
			return -1;
		balance -= amount;
		return balance;
	}
	
	public double receive(double amount) {
		balance += amount;
		return balance;
	}
	
	public boolean checkBalance(double amount) {
		if(balance < amount) {
			System.out.format(name + " does not have enough to complete the transaction.\n" +
					"   " + name + " has: $%.2f\n" +
					"   Transaction Amount: $%.2f\n", balance, amount);
			return false;
		}
		return true;
	}

	/**
	 * Pays a set amount to person p2.
	 * @param p2 The person who will receive the amount specified, minus Uber's cut.
	 * @param amount The amount to transfer to p2, minus Uber's cut.
	 * @return boolean True if the transaction was handled successfully, false otherwise.
	 */
	public boolean transaction(Person p2, double amount) {
		if(!checkBalance(amount))
			return false;
		pay(amount);
		p2.receive(amount * Client.DRIVER_SHARE);
		System.out.println(name + ", your transaction of " + amount + " to " + p2 + " has been successfully completed.");
		return true;
	}
	
	public double getDistance(Person p1) {
		return Math.sqrt(Math.pow((location.x - p1.getX()), 2) + Math.pow((location.y - p1.getY()), 2));
	}
	public double getDistance(Point p1) {
		return Math.sqrt(Math.pow((location.x - p1.x), 2) + Math.pow((location.y - p1.y), 2));
	}
	
	public String toString() {
		return name;
	}
}
