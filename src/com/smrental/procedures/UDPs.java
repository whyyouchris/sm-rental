package com.smrental.procedures;

import com.smrental.models.Location;

import smrental.AirPortShematic;
import smrental.SMRental;

public class UDPs 
{
	private SMRental model;
	
	// Constructor
	public UDPs(SMRental model) { this.model = model; }

	public Location getLoadingLocation() {
		return Location.COUNTER;
	}
	public double distance(Location origin, Location destination) {
		return AirPortShematic.getInstance().getDistance(origin, destination);
	}
}
