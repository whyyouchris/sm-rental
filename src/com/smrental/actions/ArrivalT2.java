package com.smrental.actions;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.CustomerStatus;
import com.smrental.models.CustomerType;
import simulationModelling.ScheduledAction;
import smrental.SMRental;

public class ArrivalT2 extends ScheduledAction{
	private static final CustomerType CUSTOMER_TYPE = CustomerType.CHECK_IN;
	private static final int T2 = CustomerLineID.T2.ordinal();
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
 		icCustomer.numberOfAdditionalPassenager = this.model.rvp.additionalPassengers();
 		icCustomer.customerStatus = CustomerStatus.WAITING_PICKUP;
		this.model.qCustomerLines[T2].add(icCustomer);
	}

}
