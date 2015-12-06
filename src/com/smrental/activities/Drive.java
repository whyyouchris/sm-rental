package com.smrental.activities;

import com.smrental.entities.Van.VanStatus;
import simulationModelling.ConditionalActivity;
import smrental.Constants.*;
import smrental.SMRental;

import static smrental.Constants.*;

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
            this.vanId = this.model.qVanLines[VANLINE_DROPOFF].remove(0);
        } else if (this.origin == Location.COUNTER) {
            this.vanId = this.model.qVanLines[VANLINE_COUNTER_PICKUP].remove(0);
        } else if (this.origin == Location.T1) {
            this.vanId = this.model.qVanLines[VANLINE_T1].remove(0);
        } else if (this.origin == Location.T2) {
            this.vanId = this.model.qVanLines[VANLINE_T2].remove(0);
        }

		this.destination = this.model.udp.getDestination(this.origin, vanId);
        if (this.origin == Location.COUNTER
                && this.destination == Location.T1) {
        	this.model.rqVans[this.vanId].status = VanStatus.DRIVING_COUNTER_T1;
        } else if (this.origin == Location.COUNTER
                && this.destination == Location.DROP_OFF) {
        	this.model.rqVans[this.vanId].status = VanStatus.DRIVING_COUNTER_DROP_OFF;
        } else if (this.origin == Location.DROP_OFF
                && this.destination == Location.T1) {
        	this.model.rqVans[this.vanId].status = VanStatus.DRIVING_DROP_OFF_T1;
        } else if (this.origin == Location.T1
                && this.destination == Location.T2) {
        	this.model.rqVans[this.vanId].status = VanStatus.DRIVING_T1_T2;
        } else if (this.origin == Location.T2
                && this.destination == Location.COUNTER) {
        	this.model.rqVans[this.vanId].status = VanStatus.DRIVING_T2_COUNTER;
        }
	}

	@Override protected double duration() {
		return this.model.dvp.calculateTime(this.origin, this.destination);
	}

    @Override protected void terminatingEvent() {
        if (this.destination == Location.COUNTER && !this.model.rqVans[this.vanId].onBoardCustomers.isEmpty()) {
            this.model.qVanLines[VANLINE_COUNTER_DROPOFF].add(this.vanId);
        } else if (this.destination == Location.DROP_OFF){
            this.model.qVanLines[VANLINE_DROPOFF].add(this.vanId);
        } else if (this.destination == Location.COUNTER){
            this.model.qVanLines[VANLINE_COUNTER_PICKUP].add(this.vanId);
        } else if (this.destination == Location.T1) {
            this.model.qVanLines[VANLINE_T1].add(this.vanId);
        } else if (this.destination == Location.T2) {
            this.model.qVanLines[VANLINE_T2].add(this.vanId);
        }
        this.model.output.totalMilesTraveledByVans += this.model.udp.distance(origin, destination);
        this.model.rqVans[this.vanId].status = VanStatus.IDLE;
    }
}
