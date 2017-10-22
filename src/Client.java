import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;


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
			PAYMENT_RATE = 0.10;
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

	public boolean finishedTrips() {
		for(Trip t : trips) {
			if(t.isRunning())
				return false;
		}
		return true;
	}
	
	/**
	 * Gets a driver from the client at a specific index
	 * @param index The index of the driver
	 * @return The driver at the given index in the ArrayList
	 */
	public Driver getDriver(int index) {
		return drivers.get(index);
	}

	/**
	 * Gets a random passenger from the client
	 * @return A random passenger from the ArrayList of passengers
	 */
	public Passenger getPassenger() {
		Random rand = new Random();
		return passengers.get(rand.nextInt(passengers.size()));
	}

	/**
	 * Gets a passenger from the client at a specific index
	 * @param index The index of the passenger
	 * @return The passenger at the given index in the ArrayList
	 */
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
	
	/** Asks a specific driver if they want to take a trip
	 * @param driver The driver you wish to ask to accept a trip
	 * @param trip The trip you are asking the driver to accept
	 * @return True if the driver accepted the trip, false otherwise.
	 */
	public boolean askDriver(Driver driver, Trip trip) {
		Scanner input = new Scanner(System.in);
		while(true) {
			System.out.println(driver + ", would you like to accept " + trip.getPassenger() + "\'s ride from " + 
					trip.getStart() + " to " + 
					trip.getEnd() + "?(y\\n)");
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
		while(!askDriver(orderedDrivers.peek(), trip)) {
			orderedDrivers.poll();
		}
		if(orderedDrivers.size() == 0) {
			System.out.println("No drivers are available to take " + trip.getPassenger() + " to " + trip.getEnd());
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
		if(!trip.checkBoundaries()) {
			System.out.println("The location you have requested is not within acceptable boundaries. \n" +
					"Please choose a location between (0,0) and (300,300)");
			return;
		}
		trips.add(trip);
		Driver driver = checkDrivers(trip);
		if(driver != null) {
			if(trip.addDriver(driver)) {
				trip.execute();
			}
		}
		trip.stopRunning();
	}

	/**
	 * Logs every trip object into the given filename
	 * @param filename The log you will put all the trips into
	 */
	public void logTrips(String filename) {
		for(Trip t : trips) {
			t.logTrip(filename);
		}
	}
	
	/**
	 * Logs the final information into the given filename
	 * @param filename The log you wish to put the final information into
	 */
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
