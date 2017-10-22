import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;


/**
 * The Client portion of Uber is meant to hold and handle the instances of
 * Drivers, Passengers, and Trips in the Uber project.
 * 
 * @author max
 * @version 0.1
 */
public class Client {
	public static final double
			DRIVER_SHARE = 0.75,
			UBER_SHARE = 0.25,
			PAYMENT_RATE = 1.00;
	private ArrayList<Driver> drivers;
	private ArrayList<Passenger> passengers;
	private ArrayList<Trip> trips;

	
	/**
	 * Constructs a client without any given drivers or passengers
	 */
	public Client() {
		drivers = new ArrayList<Driver>();
		passengers = new ArrayList<Passenger>();
		trips = new ArrayList<Trip>();
	}

	public Driver getDriver(int index) {
		return drivers.get(index);
	}

	public Passenger getPassenger() {
		Random rand = new Random();
		return passengers.get(rand.nextInt(passengers.size()));
	}
	
	public Passenger getPassenger(int index) {
		return passengers.get(index);
	}
	
	/**
	 * Adds a driver to the pool of drivers
	 * @param d The driver to be added
	 */
	public void addDriver(Driver d) {
		drivers.add(d);
	}
	
	/**
	 * Adds a passenger to the pool of passengers
	 * @param p The passenger to be added
	 */
	public void addPassenger(Passenger p) {
		passengers.add(p);
	}
	
	/**
	 * Checks the current list of drivers in Client for the ones with the highest priority.
	 * @param trip The trip that contains the information needed to properly sort the drivers
	 * @return Driver This is the driver with the highest available priority, based on DriverComp. If no drivers are suitable, returns null.
	 */
	public Driver checkDrivers(Trip trip) {
		PriorityQueue<Driver> orderedDrivers = new PriorityQueue<Driver>(drivers.size(), new DriverComp(trip.getPassenger()));
		for(Driver d : drivers) {
			if(d.available())
				orderedDrivers.add(d);
		}
		while(!trip.askDriver(orderedDrivers.peek())) {
			orderedDrivers.poll();
		}
		if(orderedDrivers.size() == 0) {
			System.out.println("No drivers are available to take " + trip.getPassenger().getName() + " to (" + trip.getEnd().x + "," + trip.getEnd().y + ")");
			return null;
		}
		System.out.println(trip.getPassenger() + ", " + orderedDrivers.peek() + " has accepted your ride!");
		return orderedDrivers.poll();
	}
	
	/**
	 * Adds a trip to the current pool of running trips, and executes said trip.
	 * @param	trip	The trip you wish to add.
	 */
	public void addTrip(Trip trip) {
		trips.add(trip);
		Driver driver = checkDrivers(trip);
		if(driver != null) {
			if(trip.addDriver(driver)) {
				trip.execute();
			}
		}
	}

	public void logTrips(String filename) {
		for(Trip t : trips) {
			t.logTrip(filename);
		}
	}
	
	public void logFinal(String filename) {
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(filename, true));
			int numTrips = 0, numTripsSuccessful = 0, numTripsCanceled = 0;
			for(Trip t : trips) {
				numTrips++;
				if(t.isSuccessful())
					numTripsSuccessful++;
				else
					numTripsCanceled++;
			}
			b.write("Total Amount of Trips: " + numTrips);
			b.newLine();
			b.write("	Successful Trips: " + numTripsSuccessful);
			b.newLine();
			b.write("	Cancelled Trips: " + numTripsCanceled);
			b.newLine();
			b.newLine();
			
			b.write("New Balance of Drivers:");
			b.newLine();
			for(Driver d : drivers) {
				b.write("   " + d + ": " + d.getBalance());
				b.newLine();
			}
			b.write("New Balance of Passengers:");
			b.newLine();
			for(Passenger p : passengers) {
				b.write("   " + p + ": " + p.getBalance());
				b.newLine();
			}
			b.newLine();
			
			b.write("Total Amount of Transactions: " + numTripsSuccessful);
			b.newLine();
			b.newLine();
			
			b.write("Rating Scores of Drivers:");
			b.newLine();
			for(Driver d : drivers) {
				b.write("   " + d + ": " + d.getRating());
				b.newLine();
			}
			b.newLine();
			
			b.write("Final Locations:");
			b.newLine();
			for(Driver d : drivers) {
				b.write("   " + d + ": " + d.getLocation());
				b.newLine();
			}
			b.newLine();
			for(Passenger p : passengers) {
				b.write("   " + p + ": " + p.getLocation());
				b.newLine();
			}
			
			b.flush();
			b.close();
		}
		catch(IOException e) {
			System.out.println("There was a problem with logging the final state of Uber.");
			e.printStackTrace();
		}
	}
}
