package smrental;

import simulationModelling.ScheduledAction;

import static smrental.Constants.*;

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

		while(vanId < numOfVan) {
			this.model.qVanLines[VANLINE_COUNTER_PICKUP].add(vanId);
			vanId++;
			if (vanId < numOfVan) {
				this.model.qVanLines[VANLINE_T1].add(vanId);
				vanId++;
			}
			if (vanId < numOfVan) {
				this.model.qVanLines[VANLINE_T2].add(vanId);
				vanId++;
			}
		}
	}
}