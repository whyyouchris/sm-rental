package com.smrental.models;

import java.util.ArrayList;
import java.util.List;

public class Van {
	public final int id;
	public final int capacity;
	private int numOfSeatTaken;
	private List<Customer> onBoardCustomers;
	private VanStatus status;
	

	public Van(int id, int capacity) {
		this.id = id;
		this.capacity = capacity;
		this.onBoardCustomers = new ArrayList<>(capacity);
		this.numOfSeatTaken = 0;
	}

	public void boardCustomer(Customer icCustomer) {
		this.onBoardCustomers.add(icCustomer);
	}

	public Customer unBoardCustomer() {
		return this.onBoardCustomers.remove(0);
	}

	public VanStatus getStatus() {
		return status;
	}

	public void setStatus(VanStatus status) {
		this.status = status;
	}
	
	public int getNumberOfCustomerOnBoard() {
		return numOfSeatTaken;
	}

	public void setNumberOfCustomerOnBoard(int num) {
		this.numOfSeatTaken = num;
	}

	@Override public String toString() {
		return String.format("[id: %s numSeatTaken: %s]", id, numOfSeatTaken);
	}
}
