package com.smrental.activities;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.CustomerType;
import com.smrental.models.Location;
import com.smrental.models.Van;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;

public class UnloadVan extends ConditionalActivity{
	private static final int COUNTER = Location.COUNTER.ordinal();
	private static final int DROP_OFF = Location.DROP_OFF.ordinal();
	private static final int COUNTER_WAIT_FOR_SERVIVING = CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal();
	private SMRental model;
	private Customer icCustomer;

	public UnloadVan(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getUnloadingLocation().isPresent();
	}

	@Override protected double duration() {
		return this.model.rvp.exitingTime(this.icCustomer.getNumberOfAdditionalPassenager());
	}

	@Override public void startingEvent() {
		Location location = this.model.udp.getUnloadingLocation().get();
		Van van;
		if (location == Location.COUNTER) {
			van = this.model.qVanLines[COUNTER].get(0);
			this.icCustomer = van.unBoardCustomer();
			int numSeatTaken = van.getNumberOfCustomerOnBoard() - this.icCustomer.getNumberOfAdditionalPassenager() -1;
			van.setNumberOfCustomerOnBoard(numSeatTaken);
			this.model.qCustomerLines[COUNTER_WAIT_FOR_SERVIVING].add(this.icCustomer);
		}

		if (location == Location.DROP_OFF) {
			van = this.model.qVanLines[DROP_OFF].get(0);
			this.icCustomer = van.unBoardCustomer();
			int numSeatTaken = van.getNumberOfCustomerOnBoard() - this.icCustomer.getNumberOfAdditionalPassenager() -1;
			van.setNumberOfCustomerOnBoard(numSeatTaken);
		}
	}

	@Override protected void terminatingEvent() {
		if (this.icCustomer.getType() == CustomerType.CHECK_OUT) {
			double serviceTime = this.model.getClock() - this.icCustomer.getTimeEnterSystem();
			if (serviceTime < 18) {
				this.model.output.numOfSatistifiedCustomer++;
			}
			this.model.output.satisfactionLevel = this.model.output.numOfSatistifiedCustomer / this.model.output.numOfServed;
			this.icCustomer = null;
		}
	}

}
