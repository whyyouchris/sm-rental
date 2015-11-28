package com.smrental.models;

public class Customer {
	public final CustomerType type;
	public final double timeEnterSystem;
	public final int numberOfAdditionalPassenager;
	public CustomerStatus customerStatus;

    public Customer(CustomerType type, double timeEnterSystem, int numberOfAdditionalPassenager) {
    	this.type = type;
    	this.timeEnterSystem = timeEnterSystem;
    	this.numberOfAdditionalPassenager = numberOfAdditionalPassenager;
		if (type == CustomerType.CHECK_IN) {
			this.customerStatus = CustomerStatus.WAITING_PICKUP;
		} else {
			this.customerStatus = CustomerStatus.WAITING_SERVICING;
		}
    }

	@Override public String toString() {
		return String.format("[type: %s]", type.name());
	}
}
