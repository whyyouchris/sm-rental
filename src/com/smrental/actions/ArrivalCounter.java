package com.smrental.actions;

import com.smrental.entities.Customer;
import com.smrental.entities.CustomerStatus;
import com.smrental.entities.CustomerType;
import simulationModelling.ScheduledAction;
import smrental.SMRental;

import static smrental.Constants.CUSTOMERLINE_WAIT_FOR_SERVING;

public class ArrivalCounter extends ScheduledAction{
	private static final CustomerType CUSTOMER_TYPE = CustomerType.CHECK_OUT;
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
 		icCustomer.numberOfAdditionalPassenager = this.model.rvp.uAdditionalPassengers();
 		icCustomer.customerStatus = CustomerStatus.WAITING_SERVICING;
		this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_SERVING].add(icCustomer);
	}
}