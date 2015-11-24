package com.smrental.procedures;

import java.util.List;
import java.util.Optional;

import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.Location;
import com.smrental.models.Van;
import com.smrental.models.VanStatus;

import smrental.AirPortShematic;
import smrental.SMRental;

import static smrental.Constants.*;

public class UDPs 
{
	private SMRental model;

	// Constructor
	public UDPs(SMRental model) { this.model = model; }

	public Optional<Location> getLoadingLocation() {
		if (getCanBoardCustomer(Location.T1).isPresent()) {
			return Optional.of(Location.T1);
		}
		if (getCanBoardCustomer(Location.T2).isPresent()) {
			return Optional.of(Location.T2);
		}
		if (getCanBoardCustomer(Location.COUNTER).isPresent()) {
			return Optional.of(Location.COUNTER);
		}
		return Optional.empty();
	}

	public Optional<Location> getUnloadingLocation() {
		Optional<Van> vanCounter = getFirstVanInLine(Location.COUNTER);
		if (vanCounter.isPresent()) {
			Van van = vanCounter.get();
			if (van.getNumberOfCustomerOnBoard() > 0) {
				return Optional.of(Location.COUNTER);
			}
		}

		Optional<Van> vanDropOff = getFirstVanInLine(Location.DROP_OFF);
		if (vanDropOff.isPresent()) {
			Van van = vanDropOff.get();
			if (van.getNumberOfCustomerOnBoard() > 0) {
				return Optional.of(Location.DROP_OFF);
			}
		}
		return Optional.empty();
	}

	public Optional<Location> getDriveLocation() {
		Optional<Van> vanDropOff = getFirstVanInLine(Location.DROP_OFF);
		if (vanDropOff.isPresent()) {
			Van van = vanDropOff.get();
			if (van.getNumberOfCustomerOnBoard() == 0) {
				return Optional.of(Location.DROP_OFF);
			}
		}

		Optional<Customer> customerT1 = getCanBoardCustomer(Location.T1);
		Optional<Van> vanT1 = getFirstVanInLine(Location.T1);
		if (!customerT1.isPresent()
				&& vanT1.isPresent()
				&& vanT1.get().getNumberOfCustomerOnBoard() > 0
				&& vanT1.get().getStatus() == VanStatus.IDLE) {
			return Optional.of(Location.T1);
		}

		Optional<Customer> customerT2 = getCanBoardCustomer(Location.T2);
		Optional<Van> vanT2 = getFirstVanInLine(Location.T2);
		if (!customerT2.isPresent()
				&& vanT2.isPresent()
				&& vanT2.get().getNumberOfCustomerOnBoard() > 0
				&& vanT2.get().getStatus() == VanStatus.IDLE) {
			return Optional.of(Location.T2);
		}

		Optional<Customer> customerCounter = getCanBoardCustomer(Location.COUNTER);
		Optional<Van> vanCounter = getFirstVanInLine(Location.COUNTER);
		if (!customerCounter.isPresent()
				&& vanCounter.isPresent()
				&& vanCounter.get().getNumberOfCustomerOnBoard() > 0
				&& vanCounter.get().getStatus() == VanStatus.IDLE) {
			return Optional.of(Location.COUNTER);
		}
		return Optional.empty();
	}

	/**
	 * This function will find the first customer that both his accompany passengers
	 * and himself can board the van based on the position in customer line from given location
	 * !!!This method will not remove customer form the customer line
	 * @param location
	 * @return customer - Optional<Customer>
	 */
	public Optional<Customer> getCanBoardCustomer(Location location){
		Optional<Van> firstVan = getFirstVanInLine(location);
		List<Customer> customerLine = getCustomerPickUpLineByLocation(location);
		if (firstVan.isPresent()) {
			Van van = firstVan.get();
			int numSeatAvailable = van.capacity - van.getNumberOfCustomerOnBoard();
			for (Customer customer:customerLine) {
				int numSeatNeeded = customer.getNumberOfAdditionalPassenager() +1;
				if (numSeatNeeded < numSeatAvailable) {
					return Optional.of(customer);
				}
			}
		}
		return Optional.empty();
	}

	public Location getDestination(Location location, Van van) {
		Location next = null;
		if (location == Location.COUNTER) {
			if (van.getNumberOfCustomerOnBoard() >0 ) {
				next = Location.DROP_OFF;
			} else {
				next = Location.T1;
			}
		}
		if (location == Location.T1) {
			if (van.capacity == van.getNumberOfCustomerOnBoard()) {
				next = Location.COUNTER;
			} else {
				next = Location.T2;
			}
		}
		if (location == Location.DROP_OFF) {
			next = Location.T1;
		}
		if (location == Location.T2) {
			next = Location.COUNTER;
		}
		return next;
	}

	public double calculateCosts() {
		double personnelCost = this.model.params.getNumberOfAgents() * AGENT_RATE
				+ this.model.params.getNumberOfVans() * DRIVER_RATE;
		double vanCost = 0.0;
		double totalMilesTraveled = this.model.output.totalMilesTraveledByVans;
		switch (this.model.params.getTypeOfVan()) {
		case 12:
			vanCost = totalMilesTraveled * VAN_12S_COST_RATE;
			break;
		case 18:
			vanCost = totalMilesTraveled * VAN_18S_COST_RATE;
			break;
		case 30:
			vanCost = totalMilesTraveled * VAN_30S_COST_RATE;
			break;
		default:
			break;
		}
		return personnelCost + vanCost;
	}

	public double distance(Location origin, Location destination) {
		return AirPortShematic.getInstance().getDistance(origin, destination);
	}

	/**
	 * Get the first van in the vanLine of given location
	 * @param location
	 * @return van
	 */
	public Optional<Van> getFirstVanInLine(Location location) {
		List<Van> vanLine = this.model.qVanLines[location.ordinal()];
		if (vanLine.size() > 0) {
			return Optional.of(vanLine.get(0));
		} else {
			return Optional.empty();
		}
	}
	
	public List<Customer> getCustomerPickUpLineByLocation(Location location) {
		List<Customer> customerLine = null;
		if (location == Location.COUNTER) {
			customerLine = this.model.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_PICKUP.ordinal()];
		} else if (location == Location.T1){
			customerLine = this.model.qCustomerLines[CustomerLineID.T1.ordinal()];
		} else if (location == Location.T2) {
			customerLine = this.model.qCustomerLines[CustomerLineID.T2.ordinal()];
		}
		return customerLine;
	}
	
}
