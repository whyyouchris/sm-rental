package com.smrental.models;

import java.util.ArrayList;
import java.util.List;

public class Van {
	public final int id;
	public final int capacity;
	public final List<Customer> onBoardCustomers;
	public int numOfSeatTaken;
	public VanStatus status;
	public double startWaitingTime;

	public Van(int id, int capacity) {
		this.id = id;
		this.capacity = capacity;
		this.onBoardCustomers = new ArrayList<>(capacity);
		this.numOfSeatTaken = 0;
		this.status = VanStatus.IDLE;
		this.startWaitingTime = 0.0;
	}

	@Override public String toString() {
		return String.format("[id: %s numSeatTaken: %s]", id, numOfSeatTaken);
	}
}
