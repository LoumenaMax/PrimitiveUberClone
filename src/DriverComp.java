import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


public class DriverComp implements Comparator<Driver> {

	Person passenger;
	
	public DriverComp(Passenger p) {
		this.passenger = p;
	}
	
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
