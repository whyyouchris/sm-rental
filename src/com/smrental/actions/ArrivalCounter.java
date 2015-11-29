package com.smrental.actions;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.CustomerStatus;
import com.smrental.models.CustomerType;
import simulationModelling.ScheduledAction;
import smrental.SMRental;

public class ArrivalCounter extends ScheduledAction{
	private static final CustomerType CUSTOMER_TYPE = CustomerType.CHECK_OUT;
	private static final int COUNTER = CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal();
	private SMRental model;
	
	public ArrivalCounter(SMRental model) {
		this.model = model;
	}

	@Override protected double timeSequence() {
		return this.model.rvp.DuCCounter(this.model.params.isCustomerIncrease());
	}

	@Override protected void actionEvent() {
		Customer icCustomer = new Customer(); 
 		icCustomer.uType = CUSTOMER_TYPE;
 		icCustomer.timeEnterSystem = this.model.getClock();
 		icCustomer.numberOfAdditionalPassenager = this.model.rvp.additionalPassengers();
 		icCustomer.customerStatus = CustomerStatus.WAITING_SERVICING;
		this.model.qCustomerLines[COUNTER].add(icCustomer);
	}
}