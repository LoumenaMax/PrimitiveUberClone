import java.awt.Point;
import java.util.Random;


public abstract class Person {
	protected String name;
	protected double balance;
	protected Point location;
	
	public Person(String name, double balance, Point location) {
		this.name = name;
		this.balance = balance;
		this.location = location;
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
		if(balance < amount)
			return false;
		return true;
	}
	
	public double getDistance(Person p1) {
		return Math.sqrt(Math.pow((location.x - p1.getX()), 2) + Math.pow((location.y - p1.getY()), 2));
	}
	public double getDistance(Point p1) {
		return Math.sqrt(Math.pow((location.x - p1.x), 2) + Math.pow((location.y - p1.y), 2));
	}
}
