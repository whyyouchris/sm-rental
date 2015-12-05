package com.smrental.procedures;

import com.smrental.entities.Location;
import smrental.SMRental;

public class DVPs 
{
	private SMRental model;

	public DVPs(SMRental model) { this.model = model; }

	/**
	 * 
	 * @param origin
	 * @param destination
	 * @return time which cost from origin to destination
	 */
	public double calculateTime(Location origin, Location destination) {
		double distance = this.model.udp.distance(origin, destination);
		double vanSpeed = 20; //given van average speed
		double time = (distance/vanSpeed) * 60; //the time unit is minute in this experiment.
										 //i.e. (miles/miles per hour) * minutes
		return time;
	}
}
