package smrental;

import com.smrental.models.Location;
import com.smrental.models.VanLineID;

/**
 * Singleton class represents the airport schematic
 */
public class AirPortShematic {
	private double[][] schematic;
	private static final int T1 = Location.T1.ordinal();
	private static final int T2 = Location.T2.ordinal();
	private static final int COUNTER = Location.COUNTER.ordinal();
	private static final int DROP_OFF = Location.DROP_OFF.ordinal();
	
	// Forbid other classes to create an instance of this class
	private AirPortShematic() {
		schematic = new double[4][4];
		initialize();
		addDistance();
	}
	
	private void initialize() {
		for (int row = 0; row< schematic.length; row++) {
			for (int col=0; col< schematic[row].length; col++) {
				schematic[row][col] = 0;
			}
		}
	}
	
	private void addDistance() {
		schematic[COUNTER][T1] = 1.5;
		schematic[COUNTER][DROP_OFF] = 1.7;
		schematic[DROP_OFF][T1] = 0.5;
		schematic[T1][T2] = 0.3;
		schematic[T1][COUNTER] = 2.3;
		schematic[T2][COUNTER] = 2.0;
	}
	
	public double getDistance(Location origin, Location destination) {
		return this.schematic[origin.ordinal()][destination.ordinal()];
	}
	
	public static AirPortShematic getInstance() {
		return Singleton.INSTANCE;
	}

	private static class Singleton {
		private static final AirPortShematic INSTANCE = new AirPortShematic();
	}
}
