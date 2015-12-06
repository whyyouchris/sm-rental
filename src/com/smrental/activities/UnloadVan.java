package com.smrental.activities;

import com.smrental.entities.Customer;
import com.smrental.entities.Customer.CustomerStatus;
import com.smrental.entities.Customer.CustomerType;
import com.smrental.entities.Van;
import com.smrental.entities.Van.VanStatus;
import simulationModelling.ConditionalActivity;
import smrental.Constants.*;
import smrental.SMRental;

import static smrental.Constants.*;

public class UnloadVan extends ConditionalActivity{

	private SMRental model;
	private int vanId;
	private Location unloadingLocation;
	private Customer icCustomer;

	public UnloadVan(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getUnloadingLocation() != null;
	}

	@Override protected double duration() {
		return this.model.rvp.uExitingTime(this.icCustomer.numberOfAdditionalPassenager);
	}

	@Override public void startingEvent() {
		this.unloadingLocation = this.model.udp.getUnloadingLocation();
		this.vanId = this.model.udp.getUnloadingVan(this.unloadingLocation);
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
			this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_SERVING].add(this.icCustomer);
			this.icCustomer.customerStatus = CustomerStatus.WAITING_SERVICING;
		}

		if (this.icCustomer.uType == CustomerType.CHECK_OUT) {
			double serviceTime = this.model.getClock() - this.icCustomer.timeEnterSystem;
			if (serviceTime < ACCEPTABLE_CHECK_OUT_TIME) {
				this.model.output.numOfSatisfiedCustomer++;
			}
			rqVan.onBoardCustomers.remove(this.icCustomer);
			this.icCustomer = null;
		}

		if (this.unloadingLocation == Location.COUNTER
				&& rqVan.onBoardCustomers.isEmpty()) {
			this.model.qVanLines[VANLINE_COUNTER_DROPOFF].remove(new Integer(this.vanId));
			this.model.qVanLines[VANLINE_COUNTER_PICKUP].add(this.vanId);
		}
	}

}
