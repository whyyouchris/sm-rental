package com.smrental.actions;

import com.smrental.entities.Customer;
import com.smrental.entities.Customer.CustomerStatus;
import com.smrental.entities.Customer.CustomerType;
import simulationModelling.ScheduledAction;
import smrental.SMRental;

import static smrental.Constants.CUSTOMERLINE_T2;

public class ArrivalT2 extends ScheduledAction{
	private static final CustomerType CUSTOMER_TYPE = CustomerType.CHECK_IN;
	private SMRental model;
	
	public ArrivalT2(SMRental model) {
		this.model = model;
	}

	@Override
	protected double timeSequence() {
		return this.model.rvp.DuCT2(this.model.params.isCustomerIncrease());
	}

	@Override
	protected void actionEvent() {
		Customer icCustomer = new Customer(); 
 		icCustomer.uType = CUSTOMER_TYPE;
 		icCustomer.timeEnterSystem = this.model.getClock();
 		icCustomer.numberOfAdditionalPassenager = this.model.rvp.uAdditionalPassengers();
 		icCustomer.customerStatus = CustomerStatus.WAITING_PICKUP;
		this.model.qCustomerLines[CUSTOMERLINE_T2].add(icCustomer);
	}

}
