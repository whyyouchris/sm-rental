package com.smrental.activities;

import java.util.List;

import com.smrental.models.*;

import com.smrental.utils.Operation;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

public class LoadVan extends ConditionalActivity{
	private SMRental model;
	private int vanId;
	private Customer icCustomer;
	private Location loadingLocation;

	public LoadVan(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getLoadingLocation().isPresent();
	}
	@Override protected double duration() {
		return this.model.rvp.boardingTime(this.icCustomer.numberOfAdditionalPassenager);
	}

	@Override public void startingEvent() {
		this.loadingLocation = this.model.udp.getLoadingLocation().get();
		this.icCustomer = this.model.udp.getCanBoardCustomer(this.loadingLocation).get();
		this.icCustomer.customerStatus = CustomerStatus.BOARDING;
		this.vanId = this.model.udp.getFirstVanInLine(this.loadingLocation, Operation.PICK_UP).get();
		this.model.vans[vanId].status = VanStatus.LOADING;
	}

	@Override protected void terminatingEvent() {
		List<Customer> customerLine = this.model.udp.getCustomerLine(this.loadingLocation, Operation.PICK_UP);
		customerLine.remove(this.icCustomer);
		Van van = this.model.vans[this.vanId];
		van.onBoardCustomers.add(this.icCustomer);
		van.numOfSeatTaken = van.numOfSeatTaken + this.icCustomer.numberOfAdditionalPassenager +1;
	}

}
