import java.util.ArrayList;
import java.util.PriorityQueue;


public class Client {
	public static final double
			DRIVER_SHARE = 0.75,
			UBER_SHARE = 0.25,
			PAYMENT_RATE = 1.00;
	public ArrayList<Driver> drivers;
	public ArrayList<Passenger> passengers;
	public ArrayList<Trip> trips;
	
	public Client() {
		drivers = new ArrayList<Driver>();
		passengers = new ArrayList<Passenger>();
		trips = new ArrayList<Trip>();
	}

	public void addDriver(Driver d) {
		drivers.add(d);
	}
	
	public void addPassenger(Passenger p) {
		passengers.add(p);
	}
	
	public Driver checkDrivers(Trip trip) {
		PriorityQueue<Driver> orderedDrivers = new PriorityQueue<Driver>(drivers.size(), new DriverComp(trip.getPassenger()));
		for(Driver d : drivers) {
			if(d.available())
				orderedDrivers.add(d);
		}
		while(!orderedDrivers.peek().request(trip)) {
			orderedDrivers.poll();
		}
		if(orderedDrivers.size() == 0) {
			return null;
		}
		return orderedDrivers.poll();
	}
	
	public boolean addTrip(Trip trip) {
		Driver driver = checkDrivers(trip);
		if(driver == null) {
			return false;
		}
		if(!trip.addDriver(driver)) {
			return false;
		}
		trips.add(trip);
		return true;
	}
	
	public boolean transaction(Person p1, Person p2, double amount) {
		if(!p1.checkBalance(amount))
			return false;
		p1.pay(amount);
		p2.receive(amount * DRIVER_SHARE);
		return true;
	}
}
