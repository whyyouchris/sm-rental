package com.smrental.activities;

import com.smrental.models.Location;
import com.smrental.models.VanLineID;
import com.smrental.models.Van;

import com.smrental.models.VanStatus;
import com.smrental.utils.Operation;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

public class Drive extends ConditionalActivity{

	private SMRental model;
	private Location origin;
	private Location destination;
	private int vanId;

	public Drive(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getDriveLocation().isPresent();
	}

	@Override public void startingEvent() {
		this.origin = this.model.udp.getDriveLocation().get();
        if (this.origin == Location.DROP_OFF) {
            this.vanId = this.model.udp.getVanLine(Location.DROP_OFF, Operation.DROP_OFF).remove(0);
        } else {
            this.vanId = this.model.udp.getVanLine(this.origin, Operation.PICK_UP).remove(0);
        }
		this.destination = this.model.udp.getDestination(this.origin, vanId);
        Van van = this.model.vans[this.vanId];
        if (this.origin == Location.COUNTER
                && this.destination == Location.T1) {
            van.status = VanStatus.DRIVING_COUNTER_T1;
        } else if (this.origin == Location.COUNTER
                && this.destination == Location.DROP_OFF) {
            van.status = VanStatus.DRIVING_COUNTER_DROP_OFF;
        } else if (this.origin == Location.DROP_OFF
                && this.destination == Location.T1) {
            van.status = VanStatus.DRIVING_DROP_OFF_T1;
        } else if (this.origin == Location.T1
                && this.destination == Location.T2) {
            van.status = VanStatus.DRIVING_T1_T2;
        } else if (this.origin == Location.T2
                && this.destination == Location.COUNTER) {
            van.status = VanStatus.DRIVING_T2_COUNTER;
        }
	}

	@Override protected double duration() {
		return this.model.dvp.calculateTime(this.origin, this.destination);
	}

    @Override protected void terminatingEvent() {
        if (this.destination == Location.COUNTER) {
            this.model.udp.getVanLine(Location.COUNTER, Operation.DROP_OFF).add(this.vanId);
        } else if (this.destination == Location.DROP_OFF){
            this.model.udp.getVanLine(Location.DROP_OFF, Operation.DROP_OFF).add(this.vanId);
        } else {
            this.model.udp.getVanLine(destination, Operation.PICK_UP).add(this.vanId);
        }
        this.model.output.totalMilesTraveledByVans += this.model.udp.distance(origin, destination);
        Van van = this.model.vans[this.vanId];
        van.status = VanStatus.IDLE;
    }
}
