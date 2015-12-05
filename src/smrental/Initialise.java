package smrental;

import com.smrental.entities.VanLineID;
import simulationModelling.ScheduledAction;

class Initialise extends ScheduledAction {
	SMRental model;

	// Constructor
	protected Initialise(SMRental model) {
		this.model = model;
	}

	double[] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0; // set index to first entry.

	@Override
	protected double timeSequence() {
		return ts[tsix++]; // only invoked at t=0
	}

	@Override
	protected void actionEvent() {
		int vanId = 0;
		int numOfVan = this.model.params.getNumberOfVans();
		// Van VanLineID ids
		int T1 = VanLineID.T1.ordinal();
		int T2 = VanLineID.T2.ordinal();
		int COUNTER = VanLineID.COUNTER_PICK_UP.ordinal();

		while(vanId < numOfVan) {
			this.model.qVanLines[COUNTER].add(vanId);
			vanId++;
			if (vanId < numOfVan) {
				this.model.qVanLines[T1].add(vanId);
				vanId++;
			}
			if (vanId < numOfVan) {
				this.model.qVanLines[T2].add(vanId);
				vanId++;
			}
		}
	}
}