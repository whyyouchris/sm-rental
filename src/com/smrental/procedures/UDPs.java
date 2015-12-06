package com.smrental.procedures;

import com.smrental.entities.Customer;
import com.smrental.entities.Customer.CustomerStatus;
import com.smrental.entities.Van;
import smrental.AirPortShematic;
import smrental.Constants.*;
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
            List<Integer> rqDropOff = null;
            if (location == Location.COUNTER) {
                rqDropOff = this.model.qVanLines[VANLINE_COUNTER_DROPOFF];
            }
            if (location == Location.DROP_OFF) {
                rqDropOff = this.model.qVanLines[VANLINE_DROPOFF];
            }
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
            List<Integer> rgDropoff = this.model.qVanLines[VANLINE_DROPOFF];
            for (int vanId : rgDropoff) {
                Van rqVan = this.model.rqVans[vanId];
                if (rqVan.onBoardCustomers.isEmpty()) {
                    result = true;
                    break;
                }
            }
        } else {
            Integer vanId = null;
            if (location == Location.COUNTER
                    && this.model.qVanLines[VANLINE_COUNTER_PICKUP].size()>0) {
                vanId = this.model.qVanLines[VANLINE_COUNTER_PICKUP].get(0);
            } else if (location == Location.T1
                    && this.model.qVanLines[VANLINE_T1].size()>0) {
                vanId = this.model.qVanLines[VANLINE_T1].get(0);
            } else if (location == Location.T2
                    && this.model.qVanLines[VANLINE_T2].size()>0) {
                vanId = this.model.qVanLines[VANLINE_T2].get(0);
            }
            List<Customer> customerLine = null;
            if (location == Location.COUNTER) {
                customerLine = this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_PICKUP];
            }
            if (location == Location.T1) {
                customerLine = this.model.qCustomerLines[CUSTOMERLINE_T1];
            }
            if (location == Location.T2) {
                customerLine = this.model.qCustomerLines[CUSTOMERLINE_T2];
            }
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
        Integer vanId = null;
        if (location == Location.COUNTER
                && this.model.qVanLines[VANLINE_COUNTER_PICKUP].size() > 0) {
            vanId = this.model.qVanLines[VANLINE_COUNTER_PICKUP].get(0);
        }
        if (location == Location.T1
                && this.model.qVanLines[VANLINE_T1].size() > 0) {
            vanId = this.model.qVanLines[VANLINE_T1].get(0);
        }
        if (location == Location.T2
                && this.model.qVanLines[VANLINE_T2].size() > 0) {
            vanId = this.model.qVanLines[VANLINE_T2].get(0);
        }

        // If there is already a customer boarding,
        // then no need to check again until that customer finishes.
        if (isCustomerBoarding(location)) {
            return null;
        }
        if (vanId != null) {
            Van firstVan = this.model.rqVans[vanId];
            List<Customer> customerLine = null;
            if (location == Location.COUNTER) {
                customerLine = this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_PICKUP];
            }
            if (location == Location.T1) {
                customerLine = this.model.qCustomerLines[CUSTOMERLINE_T1];
            }
            if (location == Location.T2) {
                customerLine = this.model.qCustomerLines[CUSTOMERLINE_T2];
            }
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
        List<Customer> customerLine = null;
        if (location == Location.COUNTER) {
            customerLine = this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_PICKUP];
        }
        if (location == Location.T1) {
            customerLine = this.model.qCustomerLines[CUSTOMERLINE_T1];
        }
        if (location == Location.T2) {
            customerLine = this.model.qCustomerLines[CUSTOMERLINE_T2];
        }
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
}
