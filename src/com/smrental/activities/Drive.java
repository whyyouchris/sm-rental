package com.smrental.activities;

import com.smrental.models.Location;
import com.smrental.models.Van;

import simulationModelling.ConditionalActivity;
import smrental.SMRental;

import java.util.*;

public class Drive extends ConditionalActivity{

	private SMRental model;
	private List<Location> locationList;
	private List<Van> readyToMove;
    private Map<Location, Location> originToDestination;

	public Drive(SMRental model) {
		this.model = model;
        this.readyToMove = new LinkedList<>();
        this.originToDestination = new HashMap<>();
	}

	public static boolean precondition(SMRental model) {
		return model.udp.getDriveLocations().size() >0;
	}

	@Override public void startingEvent() {
		this.locationList = this.model.udp.getDriveLocations();
        for (Location origin : this.locationList) {
            Van van = this.model.qVanLines[origin.ordinal()].remove(0);
	    	this.readyToMove.add(van);
		    Location destination = this.model.udp.getDestination(origin, van);
            this.originToDestination.put(origin, destination);
        }
	}

	@Override protected double duration() {
		double totalTime = 0.0;
        for (Map.Entry<Location, Location> entry : this.originToDestination.entrySet()) {
            totalTime += this.model.dvp.calculateTime(entry.getKey(), entry.getValue());
        }
        return totalTime;
	}

	@Override protected void terminatingEvent() {
        for ()
		this.model.qVanLines[this.destination.ordinal()].add(0, this.readyToMove);
		this.model.output.totalMilesTraveledByVans += this.model.udp.distance(origin, destination);
	}

}
