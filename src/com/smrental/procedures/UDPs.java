package com.smrental.procedures;

import java.util.List;
import java.util.Optional;

import com.smrental.models.*;
import com.smrental.models.VanLineID;

import com.smrental.utils.Operation;
import smrental.AirPortShematic;
import smrental.Constants;
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
            return  Optional.of(Location.COUNTER);
        }
		return Optional.empty();
	}

	/**	
	 * This method will check both unloading customer locations:COUNTER and DROP_OFF
	 * and return the location that is available to unload customer
	 * @return Optional<VanLineID> location
	 */
	public Optional<Location> getUnloadingLocation() {
        if (getUnloadingVan(Location.COUNTER).isPresent()) {
            return  Optional.of(Location.COUNTER);
        }
        if (getUnloadingVan(Location.DROP_OFF).isPresent()) {
            return  Optional.of(Location.DROP_OFF);
        }
		return Optional.empty();
	}

    /**
     * Return the van reference that available to move from given location
     * @param location - airport location id
     * @return Optional<Van> van
     */
    public Optional<Van> getUnloadingVan(Location location) {
        if (location == Location.COUNTER) {
            List<Van> counterVanLine = getVanLine(Location.COUNTER, Operation.DROP_UP);
            for (Van eachVan : counterVanLine) {
                if (eachVan.status != VanStatus.IDLE) {
                    continue;
                }
                for (Customer customer : eachVan.onBoardCustomers) {
                    if (customer.type == CustomerType.CHECK_IN) {
                        return Optional.of(eachVan);
                    }
                }
            }
        }
        if (location == Location.DROP_OFF) {
            List<Van> dropOffVans = getVanLine(Location.DROP_OFF, Operation.DROP_UP);
            for (Van eachVan : dropOffVans) {
                if (eachVan.onBoardCustomers.size() > 0 && eachVan.status == VanStatus.IDLE) {
                    return Optional.of(eachVan);
                }
            }

        }

        return Optional.empty();
    }

    /**
     *  If customer line is empty, move to the next location.
     *  If there is no enough space for customer to baord the first of the van, move to the next location
     *  If an van has customers on board and the van in front of it is empty, move to next location
     *
     * @return Optional<Location> - location id that has van available to move
     */
    public Optional<Location> getDriveLocation() {
        if (getVanCanDrive(Location.COUNTER).isPresent()) {
            return  Optional.of(Location.COUNTER);
        }
        if (getVanCanDrive(Location.T1).isPresent()) {
            return Optional.of(Location.T1);
        }
        if (getVanCanDrive(Location.T2).isPresent()) {
            return  Optional.of(Location.T2);
        }
        if (getVanCanDrive(Location.DROP_OFF).isPresent()) {
            return  Optional.of(Location.DROP_OFF);
        }
        return Optional.empty();
    }

    /**
     * This check all the van pick up line see if any van can drive
     * return the reference of van that can drive from given location
     * @param location - airport location id
     * @return van - reference
     */
    public Optional<Van> getVanCanDrive(Location location) {
        double currentTime = this.model.getClock();
        List<Van> vanLine;
        //Move van from van counter drop off to the van counter pick up should be
        // in the unload van activity
        if (location == Location.DROP_OFF) {
            vanLine = getVanLine(location, Operation.DROP_UP);
            for (Van van : vanLine) {
                if (van.status == VanStatus.IDLE && van.numOfSeatTaken == 0) {
                    return  Optional.of(van);
                }
            }
        } else {
            vanLine = getVanLine(location, Operation.PICK_UP);
            for (Van van : vanLine) {
                if (((currentTime - van.startWaitingTime > Constants.VAN_WAIT_DURATION)
                        || !getCanBoardCustomer(location).isPresent())
                        && van.status == VanStatus.IDLE) {
                    return Optional.of(van);
                }
            }
        }
        return Optional.empty();
    }

	/**
	 * This function will find the first customer that both his accompany passengers
	 * and himself can board the van based on the position in customer line from given location
	 * !!!This method will not remove customer form the customer line
	 * @param location - airport location id
	 * @return customer - Optional<Customer>
	 */
	public Optional<Customer> getCanBoardCustomer(Location location){
		Optional<Van> firstVan = getFirstVanInLine(location, Operation.PICK_UP);
		List<Customer> customerLine = getCustomerLine(location, Operation.PICK_UP);
		if (firstVan.isPresent() && firstVan.get().status == VanStatus.IDLE) {
			Van van = firstVan.get();
			int numSeatAvailable = van.capacity - van.numOfSeatTaken;
			for (Customer customer:customerLine) {
				int numSeatNeeded = customer.numberOfAdditionalPassenager +1;
				if (numSeatNeeded < numSeatAvailable) {
					return Optional.of(customer);
				}
			}
		}
		return Optional.empty();
	}

    /**
     * This method will calculate the destination based on current van status and current van location
     * @param origin - current van location
     * @param van - current van
     * @return Location - destination location id
     */
	public Location getDestination(Location origin, Van van) {
		Location next = null;
		if (origin == Location.COUNTER) {
			if (van.onBoardCustomers.size() >0 ) {
				next = Location.DROP_OFF;
			} else {
				next = Location.T1;
			}
		}
		if (origin == Location.T1) {
			if (van.capacity == van.onBoardCustomers.size()) {
				next = Location.COUNTER;
			} else {
				next = Location.T2;
			}
		}
		if (origin == Location.DROP_OFF) {
			next = Location.T1;
		}
		if (origin == Location.T2) {
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

	public double distance(VanLineID origin, VanLineID destination) {
		return AirPortShematic.getInstance().getDistance(origin, destination);
	}

	/**
	 * Get the first van in the vanLine of given vanLineID
	 * @param location - airport location
     * @param operation - operation performed
	 * @return van
	 */
	public Optional<Van> getFirstVanInLine(Location location, Operation operation) {
		List<Van> vanLine = getVanLine(location, operation);
		if (vanLine.size() > 0) {
			return Optional.of(vanLine.get(0));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * This just a java helper method to help me retrieve the CustomerLine by vanLineID
	 * So I don't have to type the super long 'this.mode.qCustomerLine[CustomerLineId.LocationId]' to get the
	 * customer line reference
	 * @param location - airport location
     * @param operation - operation performed
	 * @return List<Customer> - customerLine
	 */
	public List<Customer> getCustomerLine(Location location, Operation operation) {
		List<Customer> customerLine = null;
		if (operation == Operation.DROP_UP) {
            if (location == Location.COUNTER) {
                customerLine = this.model.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal()];
            }
        }

        if (operation == Operation.PICK_UP) {
            if (location == Location.COUNTER) {
                customerLine = this.model.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_PICKUP.ordinal()];
            }
            if (location == Location.T1) {
                customerLine = this.model.qCustomerLines[CustomerLineID.T1.ordinal()];
            }
            if (location == Location.T2) {
                customerLine = this.model.qCustomerLines[CustomerLineID.T2.ordinal()];
            }
        }
		return customerLine;
	}

    /**
     * This method will retrieve vanLine at a particular location basic on the operation
     * @param location - Airport location id
     * @param operation - Operation perfomed on the customer: pick up or drop off
     * @return List<van> - vanLine
     */
    public List<Van> getVanLine(Location location, Operation operation) {
        List<Van> vanList = null;
        if (operation == Operation.PICK_UP) {
            if (location == Location.COUNTER) {
                vanList = this.model.qVanLines[VanLineID.COUNTER_PICK_UP.ordinal()];
            }
            if (location == Location.T1) {
                vanList = this.model.qVanLines[VanLineID.T1.ordinal()];
            }
            if (location == Location.T2) {
                vanList = this.model.qVanLines[VanLineID.T2.ordinal()];
            }
        }

        if (operation == Operation.DROP_UP) {
            if (location == Location.COUNTER) {
                vanList = this.model.qVanLines[VanLineID.COUNTER_DROP_OFF.ordinal()];
            }
            if (location == Location.DROP_OFF) {
                vanList = this.model.qVanLines[VanLineID.DROP_OFF.ordinal()];
            }
        }

        return vanList;
    }
}
