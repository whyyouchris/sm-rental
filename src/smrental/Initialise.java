package smrental;

import com.smrental.models.CustomerLineID;
import com.smrental.models.VanLineID;
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
		restSystem();
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

	private void restSystem() {
		// System Initialisation
		this.model.rgCounter.getGroup().clear();

		// Customer lines
		this.model.qCustomerLines[CustomerLineID.T1.ordinal()].clear();
		this.model.qCustomerLines[CustomerLineID.T2.ordinal()].clear();
		this.model.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_PICKUP.ordinal()].clear();
		this.model.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal()].clear();

		// Van Lines
		this.model.qVanLines[VanLineID.T1.ordinal()].clear();
		this.model.qVanLines[VanLineID.T2.ordinal()].clear();
		this.model.qVanLines[VanLineID.COUNTER_PICK_UP.ordinal()].clear();
		this.model.qVanLines[VanLineID.COUNTER_DROP_OFF.ordinal()].clear();
		this.model.qVanLines[VanLineID.DROP_OFF.ordinal()].clear();

		// Outputs
		this.model.output.numOfServed = 0;
		this.model.output.numOfSatistifiedCustomer = 0;
		this.model.output.satisfactionLevel = 0.0;
		this.model.output.totalMilesTraveledByVans = 0.0;
	}
}