package com.smrental.activities;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.CustomerType;
import com.smrental.models.Location;
import com.smrental.models.Van;
import com.smrental.models.VanStatus;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;
import static smrental.Constants.ACCEPTABLE_CHECK_OUT_TIME;

public class UnloadVan extends ConditionalActivity{
	private static final int COUNTER = Location.COUNTER.ordinal();
	private static final int DROP_OFF = Location.DROP_OFF.ordinal();
	private static final int COUNTER_WAIT_FOR_SERVIVING = CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal();
	private SMRental model;
	private Van van;
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
		if (location == Location.COUNTER) {
			this.van = this.model.qVanLines[COUNTER].get(0);
			this.icCustomer = this.van.unBoardCustomer();
			
			this.model.qCustomerLines[COUNTER_WAIT_FOR_SERVIVING].add(this.icCustomer);
		}

		if (location == Location.DROP_OFF) {
			van = this.model.qVanLines[DROP_OFF].get(0);
			this.icCustomer = van.unBoardCustomer();
		}
		int numSeatTaken = this.van.getNumberOfCustomerOnBoard() - this.icCustomer.getNumberOfAdditionalPassenager() -1;
		this.van.setNumberOfCustomerOnBoard(numSeatTaken);
		this.van.setStatus(VanStatus.UNLOADING);
	}

	@Override protected void terminatingEvent() {
		this.van.setStatus(VanStatus.IDLE);
		if (this.icCustomer.getType() == CustomerType.CHECK_OUT) {
			double serviceTime = this.model.getClock() - this.icCustomer.getTimeEnterSystem();
			if (serviceTime < ACCEPTABLE_CHECK_OUT_TIME) {
				this.model.output.numOfSatistifiedCustomer++;
			}
			this.model.output.satisfactionLevel = this.model.output.numOfSatistifiedCustomer / this.model.output.numOfServed;
			this.icCustomer = null;
		}
	}

}
