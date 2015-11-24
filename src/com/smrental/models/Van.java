package com.smrental.models;

import java.util.ArrayList;
import java.util.List;

public class Van {
	public final int id;
	public final int capacity;
	public final List<Customer> onBoardCustomers;
	public int numOfSeatTaken;
	public VanStatus status;
	

	public Van(int id, int capacity) {
		this.id = id;
		this.capacity = capacity;
		this.onBoardCustomers = new ArrayList<>(capacity);
		this.numOfSeatTaken = 0;
		this.status = VanStatus.IDLE;
	}

	@Override public String toString() {
		return String.format("[id: %s numSeatTaken: %s]", id, numOfSeatTaken);
	}
}
