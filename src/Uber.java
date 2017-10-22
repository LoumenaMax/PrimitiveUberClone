import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Uber {
	public final static String TRIP_LOG_FILENAME = "trip_log.dat";
	public final static String FINAL_LOG_FILENAME = "final_log.dat";
	private static final int RAND_TRIP_NUMBER = 5;
	
	private static boolean parse(String filename, Client client) {
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
				client.addDriver(new Driver(line[0], Double.parseDouble(line[1]), client, line[2]));
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
				client.addPassenger(new Passenger(line[0], Double.parseDouble(line[1]), client));
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
	
	private static void randTrip(Client client) {
		Random rand = new Random();
		Passenger randPass = client.getPassenger();
		randPass.request(new Point(rand.nextInt(300), rand.nextInt(300)));
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		if(args.length == 1) {
			if(!parse(args[0], client)) {
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
			randTrip(client);
		}
		client.logTrips(TRIP_LOG_FILENAME);
		client.logFinal(FINAL_LOG_FILENAME);
	}

}
