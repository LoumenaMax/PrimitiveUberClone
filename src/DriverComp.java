import java.util.Comparator;


/**
 * A Comparator for the Driver class
 * @author max
 *
 */
public class DriverComp implements Comparator<Driver> {

	Person passenger;
	
	/**
	 * @param p The passenger that you will compare the drivers distance against
	 */
	public DriverComp(Passenger p) {
		this.passenger = p;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Driver d1, Driver d2) {
		if((d1.getDistance(passenger) - d2.getDistance(passenger)) < 0) {
			return -1;
		}
		if((d1.getDistance(passenger) - d2.getDistance(passenger)) > 0) {
			return 1;
		}
		if((d1.getRating() - d2.getRating()) > 0) {
			return -1;
		}
		if((d1.getRating() - d2.getRating()) < 0) {
			return 1;
		}
		return 0;
	}
}
