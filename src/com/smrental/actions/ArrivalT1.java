package com.smrental.actions;

import com.smrental.models.Customer;
import com.smrental.models.CustomerType;

import simulationModelling.ScheduledAction;
import smrental.Location;
import smrental.SMRental;

public class ArrivalT1 extends ScheduledAction{
	private static final CustomerType CUSTOMER_TYPE = CustomerType.CHECK_IN;
	private static final int T1 = Location.T1.ordinal();
	private SMRental model;
	
	public ArrivalT1(SMRental model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		return this.model.rvp.DuCT1(this.model.params.isCustomerIncrease());
	}

	@Override
	protected void actionEvent() {
 		Customer icCustomer = 
 				new Customer(CUSTOMER_TYPE, this.model.getClock(), this.model.rvp.additionalPassengers());
 		this.model.qCustomerLines[T1].add(icCustomer);
	}

}
