package com.smrental.activities;

import java.util.List;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.CustomerType;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;

public class Serving extends ConditionalActivity {

	private static final int COUNTER_WAIT_FOR_SERVING = CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal();
	private static final int COUNTER_WAIT_FOR_PICKUP = CustomerLineID.COUNTER_WAIT_FOR_PICKUP.ordinal();
	private SMRental model;
	private Customer icCustomer;

	public Serving(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		boolean result = false;
		List<Customer> customerLine = model.qCustomerLines[COUNTER_WAIT_FOR_SERVING];
		int numOfCustomersAtCounter = model.rgCounter.getN();
		int numberOfAgents = model.rgCounter.getNumberOfAgent();
		if (customerLine.size() > 0 && (numOfCustomersAtCounter < numberOfAgents)) {
			result = true;
		}
		return result;
	}

	@Override protected double duration() {
		return this.model.rvp.uServiceTime(this.icCustomer.getType());
	}

	@Override public void startingEvent() {
		this.icCustomer = this.model.qCustomerLines[COUNTER_WAIT_FOR_SERVING].remove(0);
		this.model.rgCounter.insertGrp(this.icCustomer);
	}

	@Override protected void terminatingEvent() {
		this.model.rgCounter.removeGrp(this.icCustomer);
		double customerServiceTime = this.model.getClock() - this.icCustomer.getTimeEnterSystem();
		if (this.icCustomer.getType() == CustomerType.CHECK_IN
				&& customerServiceTime < 20) {
			this.model.output.numOfSatistifiedCustomer++;
		}
		if (this.icCustomer.getType() == CustomerType.CHECK_OUT) {
			this.model.qCustomerLines[COUNTER_WAIT_FOR_PICKUP].add(this.icCustomer);
		}
		if (this.icCustomer.getType() == CustomerType.CHECK_IN) {
			this.icCustomer = null;
		}
		this.model.output.numOfServed++;
		this.model.output.satisfactionLevel = this.model.output.numOfSatistifiedCustomer / this.model.output.numOfServed;
	}

}