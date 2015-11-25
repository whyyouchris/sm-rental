package com.smrental.activities;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.CustomerType;
import com.smrental.models.Location;
import com.smrental.models.Van;
import com.smrental.models.VanStatus;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;
import static smrental.Constants.ACCEPTABLE_CHECK_OUT_TIME;

import java.util.List;

public class UnloadVan extends ConditionalActivity{
	private static final int COUNTER = Location.COUNTER.ordinal();
	private static final int DROP_OFF = Location.DROP_OFF.ordinal();
	private static final int COUNTER_WAIT_FOR_SERVIVING = CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal();
	private SMRental model;
	private Van van;

	public UnloadVan(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getUnloadingLocation().isPresent();
	}

	@Override protected double duration() {
		double totalUnboardTime = 0.0;
		for (Customer icCustomer : this.van.onBoardCustomers) {
			totalUnboardTime += this.model.rvp.exitingTime(icCustomer.numberOfAdditionalPassenager);
		}
		return totalUnboardTime;
	}

	@Override public void startingEvent() {
		Location location = this.model.udp.getUnloadingLocation().get();
		if (location == Location.COUNTER) {
			List<Van> vanLine = this.model.qVanLines[COUNTER];
			searchUnboard: for (Van eachVan : vanLine) {
				for (Customer customer : eachVan.onBoardCustomers) {
					if (customer.type == CustomerType.CHECK_IN) {
						this.van = eachVan;
						break searchUnboard;
					}
				}
			}
		}

		if (location == Location.DROP_OFF) {
			List<Van> vanLine = this.model.qVanLines[DROP_OFF];
			for (Van eachVan : vanLine) {
				if (eachVan.onBoardCustomers.size() > 0) {
					this.van = eachVan;
					break;
				}
			}
		}

		this.van.status = VanStatus.UNLOADING;
	}

	@Override protected void terminatingEvent() {
		this.van.status = VanStatus.IDLE;
		this.van.numOfSeatTaken = 0;

		for (Customer icCustomer : this.van.onBoardCustomers) {
			if (icCustomer.type == CustomerType.CHECK_IN) {
				this.model.qCustomerLines[COUNTER_WAIT_FOR_SERVIVING].add(icCustomer);
			}

			if (icCustomer.type == CustomerType.CHECK_OUT) {
				double serviceTime = this.model.getClock() - icCustomer.timeEnterSystem;
				if (serviceTime < ACCEPTABLE_CHECK_OUT_TIME) {
					this.model.output.numOfSatistifiedCustomer++;
					this.model.output.satisfactionLevel = this.model.output.numOfSatistifiedCustomer / this.model.output.numOfServed;
				}
				icCustomer = null;
			}
		}

		this.van.onBoardCustomers.clear();
		
	}

}
