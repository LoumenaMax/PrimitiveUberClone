import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;


/**
 * The class which actually runs the Uber program.
 * Also contains all needed variables for the program, like the Grid size and Payment rate.
 * @author max
 *
 */
public class Uber {
	public final static String TRIP_LOG_FILENAME = "trip_log.dat";
	public final static String FINAL_LOG_FILENAME = "final_log.dat";
	public static final double
		DRIVER_SHARE = 0.75,
		UBER_SHARE = 0.25,
		PAYMENT_RATE = 0.10, //Controls the amount charged per "meter".
		GRID_HEIGHT = 300;
	public static final int
		RAND_TRIP_NUMBER = 5,
		WAIT_RATE = 200; //Controls how long it takes to travel a "meter". Higher WAIT_RATE = longer wait.
	
	/**
	 * Parses a given file into a given Client object
	 * @param filename The filename you will parse from
	 * @param client The client you will update with the parsed information
	 * @return true if no errors occur, false otherwise
	 */
	private static boolean parse(String filename, ArrayList<Driver> drivers, ArrayList<Passenger> passengers) {
		Scanner s;
		String[] line;
		String sline;
		int size = 0;
		try{
			s = new Scanner(new File(filename));
		}
		catch(FileNotFoundException e) {
			System.out.println("The file \"" + filename + "\" was not found.");
			return false;
		}
		if(s.nextLine().equals("Drivers:")) {
			while(!(sline = s.nextLine()).equals("")) {
				line = sline.split(",");
				drivers.add(new Driver(line[0], new Wallet(Double.parseDouble(line[1])), line[2]));
				size++;
			}
		}
		else {
			s.close();
			System.out.println("Improper formatting of input file.");
			return false;
		}
		if(size < 10) {
			s.close();
			System.out.println("Not enough Drivers for the Uber program.\n" +
					"   Needed: 10     Contains: " + size);
			return false;
		}
		size = 0;
		if(s.nextLine().equals("Passengers:")) {
			while(s.hasNextLine()) {
				line = s.nextLine().split(",");
				passengers.add(new Passenger(line[0], new Wallet(Double.parseDouble(line[1]))));
				size++;
			}
			s.close();
		}
		else {
			s.close();
			System.out.println("Improper formatting of input file.");
			return false;
		}
		if(size < 5) {
			System.out.println("Not enough Passengers for the Uber program.\n" +
					"   Needed: 5     Contains: " + size);
			return false;
		}
		return true;
	}
	
	/**
	 * Creates a trip from a random passenger in the given client to a random destination.
	 * @param client The client you will put the trip into
	 */
	private static Trip randTrip(ArrayList<Passenger> passengers) {
		Random rand = new Random();
		Passenger randPass;
		do{
			randPass = passengers.get(rand.nextInt(passengers.size()));
		}while(randPass.inTrip());
		return new Trip((new Point(rand.nextInt(300), rand.nextInt(300))), randPass);
	}
	
	/**
	 * Logs every trip object into the given filename
	 * @param filename The log you will put all the trips into
	 */
	public static void logTrips(String filename, ArrayList<Trip> trips) {
		try {
			PrintWriter pw = new PrintWriter(filename);
			pw.close();
		} catch (FileNotFoundException e) {
		}
		for(Trip t : trips) {
			t.logTrip(filename);
		}
	}
	
	/**
	 * Logs the final information into the given filename
	 * @param filename The log you wish to put the final information into
	 * @param drivers The drivers you wish to put into the log
	 * @param passengers The passengers you wish to put into the log
	 * @param trips The trips you wish to put into the log
	 */
	public static void logFinal(String filename, ArrayList<Driver> drivers, ArrayList<Passenger> passengers, ArrayList<Trip> trips) {
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(filename));
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
				b.write("   " + d + ": " + d.getWallet());
				b.newLine();
			}
			b.write("New Balance of Passengers:");
			b.newLine();
			for(Passenger p : passengers) {
				b.write("   " + p + ": " + p.getWallet());
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
				b.write("   " + d + ": " + d.getLocationString());
				b.newLine();
			}
			b.newLine();
			for(Passenger p : passengers) {
				b.write("   " + p + ": " + p.getLocationString());
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

	/** Checks the drivers for the best driver for a given trip
	 * @param trip The trip you will use to determine the best driver
	 * @param drivers The drivers you will check
	 * @return The best driver which has accepted the trip.
	 */
	public static Driver checkDrivers(Trip trip, List<Driver> drivers) {
		PriorityQueue<Driver> orderedDrivers = new PriorityQueue<Driver>(drivers.size(), new DriverComp(trip.getPassenger()));
		for(Driver d : drivers) {
			if(d.available())
				orderedDrivers.add(d);
		}
		while(!orderedDrivers.peek().askDriver(trip)) {
			orderedDrivers.poll();
		}
		if(orderedDrivers.size() == 0) {
			return null;
		}
		return orderedDrivers.poll();
	}
	
	public static void main(String[] args) {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		ArrayList<Passenger> passengers = new ArrayList<Passenger>();
		ArrayList<Trip> trips = new ArrayList<Trip>();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		Trip newTrip;
		Driver acceptedDriver;
		
		if(args.length == 1) {
			if(!parse(args[0], drivers, passengers)) {
				System.out.println("There was a problem with the input file. Uber program has been halted.");
				System.exit(1);
			}
		}
		else {
			System.out.println("Improper number of arguments.\n" +
					"   Usage: Uber.java <filename>");
			System.exit(1);
		}
		for(int i = 0; i < RAND_TRIP_NUMBER; i++) {
			newTrip = randTrip(passengers);
			if(!newTrip.checkBoundaries()) {
				System.out.println("The location you have requested is not within acceptable boundaries. \n" +
						"Please choose a location between (0,0) and (300,300)");
				continue;
			}
			if((acceptedDriver = checkDrivers(newTrip, drivers)) == null) {
				System.out.println("No drivers are available to take " + newTrip.getPassenger() + " to " + newTrip.getEnd());
				continue;
			}
			System.out.println(newTrip.getPassenger() + ", " + acceptedDriver + " has accepted your ride!");
			newTrip.addDriver(acceptedDriver);
			trips.add(newTrip);
			Thread currentTrip = new Thread(new TripRunner(newTrip));
			currentTrip.start();
			threads.add(currentTrip);
			//t.join()
		}
		for(Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}
		logTrips(TRIP_LOG_FILENAME, trips);
		logFinal(FINAL_LOG_FILENAME, drivers, passengers, trips);
	}

}
