package com.smrental.entities;

public class Customer {
	public CustomerType uType;
	public double timeEnterSystem;
	public int numberOfAdditionalPassenager;
	public CustomerStatus customerStatus;

	@Override public String toString() {
		return String.format("[type: %s, add_pass:%s]", uType.name(), numberOfAdditionalPassenager);
	}
}
