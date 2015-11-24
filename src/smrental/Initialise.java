package smrental;

import com.smrental.models.CustomerLineID;
import com.smrental.models.Location;
import com.smrental.models.Van;

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
		int id = 0;
		int numOfVan = this.model.params.getNumberOfVans();
		// Van Location ids
		int T1 = Location.T1.ordinal();
		int T2 = Location.T2.ordinal();
		int COUNTER = Location.COUNTER.ordinal();

		while(id < numOfVan) {
			this.model.qVanLines[COUNTER].add(createVanHelper(id));
			id++;
			if (id < numOfVan) {
				this.model.qVanLines[T1].add(createVanHelper(id));
				id++;
			}
			if (id < numOfVan) {
				this.model.qVanLines[T2].add(createVanHelper(id));
				id++;
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
		this.model.qVanLines[Location.T1.ordinal()].clear();
		this.model.qVanLines[Location.T2.ordinal()].clear();
		this.model.qVanLines[Location.COUNTER.ordinal()].clear();
		this.model.qVanLines[Location.DROP_OFF.ordinal()].clear();
		this.model.output.numOfServed = 0;
		this.model.output.numOfSatistifiedCustomer = 0;
		this.model.output.satisfactionLevel = 0.0;
		this.model.output.totalMilesTraveledByVans = 0.0;
	}

	private Van createVanHelper(int id) {
		return new Van(id, this.model.params.getTypeOfVan());
	}
}