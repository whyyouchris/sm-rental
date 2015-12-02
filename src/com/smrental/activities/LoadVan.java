package com.smrental.activities;

import com.smrental.models.*;
import com.smrental.utils.LineType;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

import java.util.List;

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
		this.vanId = this.model.udp.getFirstVanInLine(this.loadingLocation, LineType.PICK_UP).get();
		this.model.rqVans[vanId].status = VanStatus.LOADING;
	}

	@Override protected void terminatingEvent() {
		List<Customer> customerLine = this.model.udp.getCustomerLine(this.loadingLocation, LineType.PICK_UP);
		customerLine.remove(this.icCustomer);
		Van rqVan = this.model.rqVans[this.vanId];
		rqVan.onBoardCustomers.add(this.icCustomer);
		rqVan.numOfSeatTaken = rqVan.numOfSeatTaken + this.icCustomer.numberOfAdditionalPassenager +1;
	}

}
