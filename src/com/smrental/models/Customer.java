package com.smrental.models;

public class Customer {
	public final CustomerType type;
	public final double timeEnterSystem;
	public final int numberOfAdditionalPassenager;

    public Customer(CustomerType type, double timeEnterSystem, int numberOfAdditionalPassenager) {
    	this.type = type;
    	this.timeEnterSystem = timeEnterSystem;
    	this.numberOfAdditionalPassenager = numberOfAdditionalPassenager;
    }

	@Override public String toString() {
		return String.format("[type: %s]", type.name());
	}
}
