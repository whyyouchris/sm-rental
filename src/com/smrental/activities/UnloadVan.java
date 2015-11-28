package com.smrental.activities;

import com.smrental.models.*;
import com.smrental.utils.Operation;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

import static smrental.Constants.ACCEPTABLE_CHECK_OUT_TIME;

public class UnloadVan extends ConditionalActivity{
	private static final int COUNTER_WAIT_FOR_SERVIVING = CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal();

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
		Van van = this.model.vans[this.vanId];
		van.status = VanStatus.UNLOADING;
		this.icCustomer = van.onBoardCustomers.get(0);
		this.icCustomer.customerStatus = CustomerStatus.UNBOARDING;
	}

	@Override protected void terminatingEvent() {
		Van van = this.model.vans[this.vanId];
		van.onBoardCustomers.remove(this.icCustomer);
		van.numOfSeatTaken = van.numOfSeatTaken - this.icCustomer.numberOfAdditionalPassenager - 1;

		if (this.icCustomer.type == CustomerType.CHECK_IN) {
            this.model.udp.getCustomerLine(Location.COUNTER, Operation.DROP_OFF).add(this.icCustomer);
			this.icCustomer.customerStatus = CustomerStatus.WAITING_SERVICING;
		}

		if (this.icCustomer.type == CustomerType.CHECK_OUT) {
			double serviceTime = this.model.getClock() - this.icCustomer.timeEnterSystem;
			if (serviceTime < ACCEPTABLE_CHECK_OUT_TIME) {
				this.model.output.numOfSatistifiedCustomer++;
				this.model.output.satisfactionLevel = this.model.output.numOfSatistifiedCustomer / this.model.output.numOfServed;
			}
			van.onBoardCustomers.remove(this.icCustomer);
			this.icCustomer = null;
		}

		if (this.unloadingLocation == Location.COUNTER
				&& van.onBoardCustomers.isEmpty()) {
			this.model.udp.getVanLine(Location.COUNTER, Operation.DROP_OFF).remove(this.vanId);
			this.model.udp.getVanLine(Location.COUNTER, Operation.PICK_UP).add(this.vanId);
		}
	}

}
