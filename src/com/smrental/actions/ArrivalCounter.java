package com.smrental.actions;

import com.smrental.models.Customer;
import com.smrental.models.CustomerType;

import simulationModelling.ScheduledAction;
import smrental.Location;
import smrental.SMRental;

public class ArrivalCounter extends ScheduledAction{
	private static final CustomerType CUSTOMER_TYPE = CustomerType.CHECK_OUT;
	private static final int COUNTER = Location.COUNTER.ordinal();
	private SMRental model;
	
	public ArrivalCounter(SMRental model) {
		this.model = model;
	}

	@Override
	protected double timeSequence() {
		return this.model.rvp.DuCCounter(this.model.params.isCustomerIncrease());
	}

	@Override
	protected void actionEvent() {
		Customer icCustomer =
				new Customer(CUSTOMER_TYPE, this.model.getClock(), this.model.rvp.additionalPassengers());
		this.model.qCustomerLines[COUNTER].add(icCustomer);
	}
}