package com.smrental.activities;

import java.util.List;

import com.smrental.models.Customer;
import com.smrental.models.Location;
import com.smrental.models.Van;
import com.smrental.models.VanStatus;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;

public class LoadVan extends ConditionalActivity{
	private SMRental model;
	private Van van;
	private Customer icCustomer;
	private Location loadingLocation;

	public LoadVan(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getLoadingLocation().isPresent();
	}
	@Override protected double duration() {
		return this.model.rvp.boardingTime(this.icCustomer.getNumberOfAdditionalPassenager());
	}

	@Override public void startingEvent() {
		this.loadingLocation = this.model.udp.getLoadingLocation().get();
		this.icCustomer = this.model.udp.getCanBoardCustomer(this.loadingLocation).get();
		List<Customer> customerLine = this.model.udp.getCustomerPickUpLineByLocation(loadingLocation);
		customerLine.remove(this.icCustomer);
		this.van = this.model.qVanLines[this.loadingLocation.ordinal()].get(0);
		this.van.setStatus(VanStatus.LOADING);
	}

	@Override protected void terminatingEvent() {
		this.van.boardCustomer(this.icCustomer);
		int newSeatTaken = this.van.getNumberOfCustomerOnBoard() + this.icCustomer.getNumberOfAdditionalPassenager() +1;
		this.van.setNumberOfCustomerOnBoard(newSeatTaken);
	}

}
