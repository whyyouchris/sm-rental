package com.smrental.models;

public class Customer {
	public final CustomerType uType;
	public final double timeEnterSystem;
	public final int numberOfAdditionalPassenager;
	public CustomerStatus customerStatus;

    public Customer(CustomerType type, double timeEnterSystem, int numberOfAdditionalPassenager) {
    	this.uType = type;
    	this.timeEnterSystem = timeEnterSystem;
    	this.numberOfAdditionalPassenager = numberOfAdditionalPassenager;
		if (type == CustomerType.CHECK_IN) {
			this.customerStatus = CustomerStatus.WAITING_PICKUP;
		} else {
			this.customerStatus = CustomerStatus.WAITING_SERVICING;
		}
    }

	@Override public String toString() {
		return String.format("[type: %s, add_pass:%s]", uType.name(), numberOfAdditionalPassenager);
	}
}
