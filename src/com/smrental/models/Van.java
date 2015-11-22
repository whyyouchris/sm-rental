package com.smrental.models;

import com.smrental.utils.FixedSizeList;

public class Van {
	public final int id;
	public final int capacity;
	private FixedSizeList<Customer> onBoardCustomers;
	private VanStatus status;
	

	public Van(int id, int capacity) {
		this.id = id;
		this.capacity = capacity;
		this.onBoardCustomers = new FixedSizeList<>(capacity);
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
		return this.onBoardCustomers.size();
	}
}
