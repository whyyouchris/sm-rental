package com.smrental.activities;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.CustomerStatus;
import com.smrental.models.CustomerType;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

import java.util.List;

import static smrental.Constants.ACCEPTABLE_CHECK_IN_TIME;

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
		int numberOfAgents = model.rgCounter.numberOfAgent;
		if (customerLine.size() > 0 && (numOfCustomersAtCounter < numberOfAgents)) {
			result = true;
		}
		return result;
	}

	@Override protected double duration() {
		return this.model.rvp.uServiceTime(this.icCustomer.uType);
	}

	@Override public void startingEvent() {
		this.icCustomer = this.model.qCustomerLines[COUNTER_WAIT_FOR_SERVING].remove(0);
		this.model.rgCounter.insertGrp(this.icCustomer);
		this.icCustomer.customerStatus = CustomerStatus.SERVING;
	}

	@Override protected void terminatingEvent() {
		this.model.rgCounter.removeGrp(this.icCustomer);
		double customerServiceTime = this.model.getClock() - this.icCustomer.timeEnterSystem;
		if (this.icCustomer.uType == CustomerType.CHECK_IN
				&& customerServiceTime < ACCEPTABLE_CHECK_IN_TIME) {
			this.model.output.numOfSatistifiedCustomer++;
		}
		if (this.icCustomer.uType == CustomerType.CHECK_OUT) {
			this.model.qCustomerLines[COUNTER_WAIT_FOR_PICKUP].add(this.icCustomer);
			this.icCustomer.customerStatus = CustomerStatus.WAITING_PICKUP;
		}
		if (this.icCustomer.uType == CustomerType.CHECK_IN) {
			this.icCustomer = null;
		}
		this.model.output.numOfServed++;
	}

}
