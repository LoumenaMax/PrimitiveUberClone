import java.awt.Point;
import java.util.ArrayList;


public class Driver extends Person{

	private String carTitle;
	private boolean available;
	private Client client; 
	private ArrayList<Integer> ratings;
	
	public Driver(String name, double balance, Point location, Client client, String carTitle) {
		super(name, balance, location);
		this.carTitle = carTitle;
		this.available = true;
		ratings = new ArrayList<Integer>();
	}
	
	public void addRating(Integer rating) {
		ratings.add(rating);
	}

	public boolean request(Trip t) {
		//TODO
		available = false;
		return true;
	}
	
	public boolean available() {
		return available;
	}
	
	public double getRating() {
		double average = 0;
		if(ratings.size() == 0) {
			return -1;
		}
		for(Integer rating : ratings) {
			average += rating;
		}
		return (average / ratings.size());
	}
}
