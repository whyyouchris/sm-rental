package com.smrental.procedures;

import com.smrental.entities.*;
import com.smrental.utils.LineType;
import smrental.AirPortShematic;
import smrental.SMRental;

import java.util.List;

import static smrental.Constants.*;

public class UDPs {
    private SMRental model;

    public UDPs(SMRental model) {
        this.model = model;
    }

    public Location getLoadingLocation() {
        if (getCanBoardCustomer(Location.T1) != null) {
            return Location.T1;
        }
        if (getCanBoardCustomer(Location.T2) != null) {
            return Location.T2;
        }

        if (getCanBoardCustomer(Location.COUNTER) != null) {
            return Location.COUNTER;
        }
        return null;
    }

    /**
     * This method will check both unloading customer locations:COUNTER and DROP_OFF
     * and return the location that is available to unload customer
     *
     * @return Optional<VanLineID> location
     */
    public Location getUnloadingLocation() {
        if (getUnloadingVan(Location.COUNTER) != null) {
            return Location.COUNTER;
        }
        if (getUnloadingVan(Location.DROP_OFF) != null) {
            return Location.DROP_OFF;
        }
        return null;
    }

    /**
     * Return the van reference that available to move from given location
     *
     * @param location - airport location id
     * @return Optional<Integer> vanId
     */
    public Integer getUnloadingVan(Location location) {
        if (location == Location.COUNTER || location == Location.DROP_OFF) {
            List<Integer> rqDropOff = getVanLine(location, LineType.DROP_OFF);
            for (int eachVanId : rqDropOff) {
                Van eachVan = this.model.rqVans[eachVanId];
                if (!eachVan.onBoardCustomers.isEmpty()
                        && !isCustomerUnloading(eachVanId)) {
                    return eachVanId;
                }
            }
        }
        return null;
    }

    /**
     * @param vanId
     * @return boolean value indicate whether customer unloading
     */
    public boolean isCustomerUnloading(int vanId) {
        boolean result = false;
        Van rqVan = this.model.rqVans[vanId];
        for (Customer customer : rqVan.onBoardCustomers) {
            if (customer.customerStatus == CustomerStatus.UNBOARDING) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * If customer line is empty, move to the next location.
     * If there is no enough space for customer to baord the first of the van, move to the next location
     * If an van has customers on board and the van in front of it is empty, move to next location
     *
     * @return Optional<Location> - location id that has van available to move
     */
    public Location getDriveLocation() {
        if (isTheLocationCanDrive(Location.COUNTER)) {
            return Location.COUNTER;
        }
        if (isTheLocationCanDrive(Location.T1)) {
            return Location.T1;
        }
        if (isTheLocationCanDrive(Location.T2)) {
            return Location.T2;
        }
        if (isTheLocationCanDrive(Location.DROP_OFF)) {
            return Location.DROP_OFF;
        }

        return null;
    }

    /**
     * @param location
     * @return boolean value which check whether can drive to location
     */
    private boolean isTheLocationCanDrive(Location location) {
        boolean result = false;
        if (location == Location.DROP_OFF) {
            List<Integer> rgDropoff = getVanLine(Location.DROP_OFF, LineType.DROP_OFF);
            for (int vanId : rgDropoff) {
                Van rqVan = this.model.rqVans[vanId];
                if (rqVan.onBoardCustomers.isEmpty()) {
                    result = true;
                    break;
                }
            }
        } else {
            Integer vanId = getFirstVanInLine(location, LineType.PICK_UP);
            List<Customer> customerLine = getCustomerLine(location, LineType.PICK_UP);
            if (vanId != null) {
                if ((getCanBoardCustomer(location) == null
                        && !this.model.udp.isCustomerBoarding(location))
                        || customerLine.isEmpty()) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * This function will find the first customer that both his accompany passengers
     * and himself can board the van based on the position in customer line from given location
     * !!!This method will not remove customer form the customer line
     *
     * @param location - airport location id
     * @return customer - Optional<Customer>
     */
    public Customer getCanBoardCustomer(Location location) {
        Integer vanId = getFirstVanInLine(location, LineType.PICK_UP);

        // If there is already a customer boarding,
        // then no need to check again until that customer finishes.
        if (isCustomerBoarding(location)) {
            return null;
        }
        if (vanId != null) {
            Van firstVan = this.model.rqVans[vanId];
            List<Customer> customerLine = getCustomerLine(location, LineType.PICK_UP);
            int numSeatAvailable = firstVan.capacity - firstVan.numOfSeatTaken;
            for (Customer customer : customerLine) {
                int numSeatNeeded = customer.numberOfAdditionalPassenager + 1;
                if (numSeatNeeded < numSeatAvailable) {
                    return customer;
                }
            }
        }
        return null;
    }

    /**
     * @param location
     * @return a boolean value indicate whether customers can boarding
     */
    public boolean isCustomerBoarding(Location location) {
        boolean result = false;
        List<Customer> customerLine = getCustomerLine(location, LineType.PICK_UP);
        for (Customer customer : customerLine) {
            if (customer.customerStatus == CustomerStatus.BOARDING) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This method will calculate the destination based on current van status and current van location
     *
     * @param origin - current van location
     * @param vanId  - current van
     * @return Location - destination location id
     */
    public Location getDestination(Location origin, int vanId) {
        Location next = null;
        Van van = this.model.rqVans[vanId];
        if (origin == Location.COUNTER) {
            if (van.onBoardCustomers.size() > 0) {
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

    /**
     * @return total cost
     */
    public double calculateCosts() {
        double personnelCost = this.model.params.getNumberOfAgents() * AGENT_RATE * TOTAL_TIME
                + this.model.params.getNumberOfVans() * DRIVER_RATE * TOTAL_TIME;
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
     * Get the first van in the vanLine of given vanLineID
     *
     * @param location - airport location
     * @param lineType - lineType performed
     * @return van
     */
    public Integer getFirstVanInLine(Location location, LineType lineType) {
        List<Integer> vanLine = getVanLine(location, lineType);
        if (vanLine.size() > 0) {
            return vanLine.get(0);
        } else {
            return null;
        }
    }

    /**
     * This just a java helper method to help me retrieve the CustomerLine by vanLineID
     * So I don't have to type the super long 'this.mode.qCustomerLine[CustomerLineId.LocationId]' to get the
     * customer line reference
     *
     * @param location - airport location
     * @param lineType - lineType performed
     * @return List<Customer> - customerLine
     */
    public List<Customer> getCustomerLine(Location location, LineType lineType) {
        List<Customer> customerLine = null;
        if (lineType == LineType.DROP_OFF) {
            if (location == Location.COUNTER) {
                customerLine = this.model.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal()];
            }
        }

        if (lineType == LineType.PICK_UP) {
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
     * This method will retrieve vanLine at a particular location basic on the lineType
     *
     * @param location - Airport location id
     * @param lineType - LineType perfomed on the customer: pick up or drop off
     * @return List<van> - vanLine
     */
    public List<Integer> getVanLine(Location location, LineType lineType) {
        List<Integer> vanList = null;
        if (lineType == LineType.PICK_UP) {
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

        if (lineType == LineType.DROP_OFF) {
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
