package com.smrental.activities;

import com.smrental.entities.Customer;
import com.smrental.entities.Customer.CustomerStatus;
import com.smrental.entities.Van;
import com.smrental.entities.Van.VanStatus;
import com.smrental.utils.LineType;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;
import smrental.Constants.Location;

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
		return model.udp.getLoadingLocation() != null;
	}
	@Override protected double duration() {
		return this.model.rvp.uBoardingTime(this.icCustomer.numberOfAdditionalPassenager);
	}

	@Override public void startingEvent() {
		this.loadingLocation = this.model.udp.getLoadingLocation();
		this.icCustomer = this.model.udp.getCanBoardCustomer(this.loadingLocation);
		this.icCustomer.customerStatus = CustomerStatus.BOARDING;
		this.vanId = this.model.udp.getFirstVanInLine(this.loadingLocation, LineType.PICK_UP);
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
