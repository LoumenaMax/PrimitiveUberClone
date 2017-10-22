import java.util.ArrayList;

public class Driver extends Person{

	private String carTitle;
	private boolean available;
	private Client client; 
	private ArrayList<Integer> ratings;
	
	public Driver(String name, double balance, String carTitle) {
		super(name, balance);
		this.carTitle = carTitle;
		this.available = true;
		ratings = new ArrayList<Integer>();
	}
	
	public void addRating(Integer rating) {
		ratings.add(rating);
	}

	public void acceptRequest() {
		available = false;
	}
	
	public void endTrip() {
		available = true;
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
