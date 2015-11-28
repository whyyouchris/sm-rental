package com.smrental.activities;

import com.smrental.models.*;
import com.smrental.models.VanLineID;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;
import static smrental.Constants.ACCEPTABLE_CHECK_OUT_TIME;

import java.util.List;

public class UnloadVan extends ConditionalActivity{
	private static final int COUNTER = VanLineID.COUNTER.ordinal();
	private static final int DROP_OFF = VanLineID.DROP_OFF.ordinal();
	private static final int COUNTER_WAIT_FOR_SERVIVING = CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal();
	private SMRental model;
	private Van van;
	private Customer icCustomer;

	public UnloadVan(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getUnloadingLocation().isPresent();
	}

	@Override protected double duration() {
		return this.model.rvp.exitingTime(this.icCustomer.numberOfAdditionalPassenager);
	}

	@Override public void startingEvent() {
		VanLineID vanLineID = this.model.udp.getUnloadingLocation().get();
		if (vanLineID == VanLineID.COUNTER) {
			List<Van> vanList = this.model.qVanLines[VanLineID.COUNTER.ordinal()];
			for (Van van : vanList) {
				if (van.status != VanStatus.IDLE) {
					continue;
				}
				for (Customer customer : van.onBoardCustomers) {
					if (customer.type == CustomerType.CHECK_IN) {
						this.van = this.model.qVanLines[COUNTER].get(0);
						this.icCustomer = van.onBoardCustomers.get(0);
					}
				}
			}
		}

		if (vanLineID == VanLineID.DROP_OFF) {
			List<Van> vanList = this.model.qVanLines[VanLineID.DROP_OFF.ordinal()];
			for (Van van : vanList) {
				if (van.status != VanStatus.IDLE) {
					continue;
				}
				if (van.onBoardCustomers.size() > 0) {
					this.van = van;
					this.icCustomer = van.onBoardCustomers.get(0);
				}
			}
		}
		this.van.status = VanStatus.UNLOADING;
	}

	@Override protected void terminatingEvent() {
		this.van.status = VanStatus.IDLE;
		this.van.onBoardCustomers.remove(this.icCustomer);
		this.van.numOfSeatTaken = this.van.numOfSeatTaken - this.icCustomer.numberOfAdditionalPassenager -1;

		if (this.icCustomer.type == CustomerType.CHECK_IN) {
			this.model.qCustomerLines[COUNTER_WAIT_FOR_SERVIVING].add(this.icCustomer);
		}

		if (this.icCustomer.type == CustomerType.CHECK_OUT) {
			double serviceTime = this.model.getClock() - this.icCustomer.timeEnterSystem;
			if (serviceTime < ACCEPTABLE_CHECK_OUT_TIME) {
				this.model.output.numOfSatistifiedCustomer++;
			}
			this.model.output.satisfactionLevel = this.model.output.numOfSatistifiedCustomer / this.model.output.numOfServed;
			this.icCustomer = null;
		}
	}

}
