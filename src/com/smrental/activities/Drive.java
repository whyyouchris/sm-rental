package com.smrental.activities;

import com.smrental.entities.Van;
import com.smrental.entities.Van.VanStatus;
import smrental.Constants.LineType;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;
import smrental.Constants.Location;

public class Drive extends ConditionalActivity{

	private SMRental model;
	private Location origin;
	private Location destination;
	private int vanId;

	public Drive(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getDriveLocation() != null;
	}

	@Override public void startingEvent() {
		this.origin = this.model.udp.getDriveLocation();
        if (this.origin == Location.DROP_OFF) {
            this.vanId = this.model.udp.getVanLine(Location.DROP_OFF, LineType.DROP_OFF).remove(0);
        } else {
            this.vanId = this.model.udp.getVanLine(this.origin, LineType.PICK_UP).remove(0);
        }
		this.destination = this.model.udp.getDestination(this.origin, vanId);
        Van rqVan = this.model.rqVans[this.vanId];
        if (this.origin == Location.COUNTER
                && this.destination == Location.T1) {
        	rqVan.status = VanStatus.DRIVING_COUNTER_T1;
        } else if (this.origin == Location.COUNTER
                && this.destination == Location.DROP_OFF) {
        	rqVan.status = VanStatus.DRIVING_COUNTER_DROP_OFF;
        } else if (this.origin == Location.DROP_OFF
                && this.destination == Location.T1) {
        	rqVan.status = VanStatus.DRIVING_DROP_OFF_T1;
        } else if (this.origin == Location.T1
                && this.destination == Location.T2) {
        	rqVan.status = VanStatus.DRIVING_T1_T2;
        } else if (this.origin == Location.T2
                && this.destination == Location.COUNTER) {
        	rqVan.status = VanStatus.DRIVING_T2_COUNTER;
        }
	}

	@Override protected double duration() {
		return this.model.dvp.calculateTime(this.origin, this.destination);
	}

    @Override protected void terminatingEvent() {
        Van rqVan = this.model.rqVans[this.vanId];
        if (this.destination == Location.COUNTER && !rqVan.onBoardCustomers.isEmpty()) {
            this.model.udp.getVanLine(Location.COUNTER, LineType.DROP_OFF).add(this.vanId);
        } else if (this.destination == Location.DROP_OFF){
            this.model.udp.getVanLine(Location.DROP_OFF, LineType.DROP_OFF).add(this.vanId);
        } else {
            this.model.udp.getVanLine(destination, LineType.PICK_UP).add(this.vanId);
        }
        this.model.output.totalMilesTraveledByVans += this.model.udp.distance(origin, destination);
        rqVan.status = VanStatus.IDLE;
    }
}
