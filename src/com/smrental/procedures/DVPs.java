package com.smrental.procedures;

import com.smrental.models.VanLineID;

import smrental.SMRental;

public class DVPs 
{
	private SMRental model;

	public DVPs(SMRental model) { this.model = model; }

	public double calculateTime(VanLineID origin, VanLineID destination) {
		double distance = this.model.udp.distance(origin, destination);
		double vanSpeed = 20;
		return distance/vanSpeed;
	}
}
