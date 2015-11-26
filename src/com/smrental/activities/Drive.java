package com.smrental.activities;

import com.smrental.models.Location;
import com.smrental.models.Van;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;

public class Drive extends ConditionalActivity{

	private SMRental model;
	private Location origin;
	private Location destination;
	private Van van;

	public Drive(SMRental model) {
		this.model = model;
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getDriveLocation().isPresent();
	}

	@Override public void startingEvent() {
		this.origin = this.model.udp.getDriveLocation().get();
		this.van = this.model.qVanLines[this.origin.ordinal()].remove(0);
		this.destination = this.model.udp.getDestination(this.origin, van);
	}

	@Override protected double duration() {
		return this.model.dvp.calculateTime(this.origin, this.destination);
	}

	@Override protected void terminatingEvent() {
		this.model.qVanLines[this.destination.ordinal()].add(this.van);
		this.model.output.totalMilesTraveledByVans += this.model.udp.distance(origin, destination);
	}

}
