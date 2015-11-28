package com.smrental.activities;

import java.util.List;

import com.smrental.models.Customer;
import com.smrental.models.VanLineID;
import com.smrental.models.Van;
import com.smrental.models.VanStatus;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;

public class LoadVan extends ConditionalActivity{
	private SMRental model;
	private Van van;
	private Customer icCustomer;
	private VanLineID loadingVanLineID;

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
		this.loadingVanLineID = this.model.udp.getLoadingLocation().get();
		this.icCustomer = this.model.udp.getCanBoardCustomer(this.loadingVanLineID).get();
		this.van = this.model.qVanLines[this.loadingVanLineID.ordinal()].get(0);
		this.van.status = VanStatus.LOADING;
	}

	@Override protected void terminatingEvent() {
		List<Customer> customerLine = this.model.udp.getCustomerPickUpLineByLocation(loadingVanLineID);
		customerLine.remove(this.icCustomer);
		this.van.onBoardCustomers.add(this.icCustomer);
		this.van.numOfSeatTaken = this.van.numOfSeatTaken + this.icCustomer.numberOfAdditionalPassenager +1;
		this.van.status = VanStatus.IDLE;
	}

}
