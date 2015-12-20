package com.smrental.activities;

import com.smrental.entities.Customer;
import com.smrental.entities.Customer.CustomerStatus;
import com.smrental.entities.Van.VanStatus;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

import static smrental.Constants.*;

public class LoadVan extends ConditionalActivity {
    private SMRental model;
    private int vanId;
    private Customer icCustomer;
    private Location loadingLocId;

    public LoadVan(SMRental model) {
        this.model = model;
    }

    public static boolean precondition(SMRental model) {
        return model.udp.getLoadingLocation() != null;
    }

    @Override
    protected double duration() {
        return this.model.rvp.uBoardingTime(this.icCustomer.numberOfAdditionalPassenager);
    }

    @Override
    public void startingEvent() {
        this.loadingLocId = this.model.udp.getLoadingLocation();
        this.icCustomer = this.model.udp.getCanBoardCustomer(loadingLocId);
        this.icCustomer.customerStatus = CustomerStatus.BOARDING;
        if (this.loadingLocId == Location.COUNTER) {
            this.vanId = this.model.qVanLines[VANLINE_COUNTER_PICKUP].get(0);
        } else if (this.loadingLocId == Location.T1) {
            this.vanId = this.model.qVanLines[VANLINE_T1].get(0);
        } else if (this.loadingLocId == Location.T2) {
            this.vanId = this.model.qVanLines[VANLINE_T2].get(0);
        }
        this.model.rqVans[vanId].status = VanStatus.LOADING;
    }

    @Override
    protected void terminatingEvent() {
        if (this.loadingLocId == Location.COUNTER) {
            this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_PICKUP].remove(icCustomer);
        }
        if (this.loadingLocId == Location.T1) {
            this.model.qCustomerLines[CUSTOMERLINE_T1].remove(icCustomer);
        }
        if (this.loadingLocId == Location.T2) {
            this.model.qCustomerLines[CUSTOMERLINE_T2].remove(icCustomer);
        }
        this.model.rqVans[vanId].onBoardCustomers.add(icCustomer);
        this.model.rqVans[vanId].numOfSeatTaken = this.model.rqVans[vanId].numOfSeatTaken + icCustomer.numberOfAdditionalPassenager + 1;
    }

}
