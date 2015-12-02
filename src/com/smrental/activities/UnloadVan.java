package com.smrental.activities;

import com.smrental.models.*;
import com.smrental.utils.LineType;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

import static smrental.Constants.ACCEPTABLE_CHECK_OUT_TIME;

public class UnloadVan extends ConditionalActivity{

	private SMRental model;
	private int vanId;
	private Location unloadingLocation;
	private Customer icCustomer;

	public UnloadVan(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getUnloadingLocation().isPresent();
	}

	@Override protected double duration() {
		return this.model.rvp.exitingTime(this.icCustomer.numberOfAdditionalPassenager);
	}

	@Override public void startingEvent() {
		this.unloadingLocation = this.model.udp.getUnloadingLocation().get();
		this.vanId = this.model.udp.getUnloadingVan(this.unloadingLocation).get();
		Van rqVan = this.model.rqVans[this.vanId];
		rqVan.status = VanStatus.UNLOADING;
		this.icCustomer = rqVan.onBoardCustomers.get(0);
		this.icCustomer.customerStatus = CustomerStatus.UNBOARDING;
	}

	@Override protected void terminatingEvent() {
		Van rqVan = this.model.rqVans[this.vanId];
		rqVan.onBoardCustomers.remove(this.icCustomer);
		rqVan.numOfSeatTaken = rqVan.numOfSeatTaken - this.icCustomer.numberOfAdditionalPassenager - 1;

		if (this.icCustomer.uType == CustomerType.CHECK_IN) {
            this.model.udp.getCustomerLine(Location.COUNTER, LineType.DROP_OFF).add(this.icCustomer);
			this.icCustomer.customerStatus = CustomerStatus.WAITING_SERVICING;
		}

		if (this.icCustomer.uType == CustomerType.CHECK_OUT) {
			double serviceTime = this.model.getClock() - this.icCustomer.timeEnterSystem;
			if (serviceTime < ACCEPTABLE_CHECK_OUT_TIME) {
				this.model.output.numOfSatistifiedCustomer++;
			}
			rqVan.onBoardCustomers.remove(this.icCustomer);
			this.icCustomer = null;
		}

		if (this.unloadingLocation == Location.COUNTER
				&& rqVan.onBoardCustomers.isEmpty()) {
			this.model.udp.getVanLine(Location.COUNTER, LineType.DROP_OFF).remove(new Integer(this.vanId));
			this.model.udp.getVanLine(Location.COUNTER, LineType.PICK_UP).add(this.vanId);
		}
	}

}
