package com.smrental.models;

import java.util.HashSet;

public class Counter {
	private int numberOfAgent;
	private HashSet<Customer> group = new HashSet<>();

	public Counter(int numberOfAgent){
		this.numberOfAgent = numberOfAgent;
	}
	/**
	 * @param Customer - icCustomer
	 * @return void
	 * Add an customer to the counter group
	 */
	public void insertGrp(Customer icCustomer) { 
		this.group.add(icCustomer);
	}

	public boolean removeGrp(Customer icCustomer) {
		return this.group.remove(icCustomer);
	}

	public int getN() {
		return this.group.size();
	}
	
	public int getNumberOfAgent() {
		return this.numberOfAgent;
	}

	public HashSet<Customer> getGroup() {
		return group;
	}

	public void setNumberOfAgent(int numberOfAgent) {
		this.numberOfAgent = numberOfAgent;
	}
	
}
