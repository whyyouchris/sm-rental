package com.smrental.activities;

import com.smrental.entities.Customer;
import com.smrental.entities.Customer.CustomerStatus;
import com.smrental.entities.Customer.CustomerType;
import simulationModelling.ConditionalActivity;
import smrental.SMRental;

import static smrental.Constants.*;

public class Serving extends ConditionalActivity {

    private SMRental model;
    private Customer icCustomer;

    public Serving(SMRental model) {
        this.model = model;
    }

    public static boolean precondition(SMRental model) {
        boolean result = false;
        int numOfCustomersAtCounter = model.rgCounter.getN();
        int numberOfAgents = model.rgCounter.numberOfAgent;
        if (model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_SERVING].size() > 0
                && (numOfCustomersAtCounter < numberOfAgents)) {
            result = true;
        }
        return result;
    }

    @Override
    protected double duration() {
        return this.model.rvp.uServiceTime(this.icCustomer.uType);
    }

    @Override
    public void startingEvent() {
        this.icCustomer = this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_SERVING].remove(0);
        this.model.rgCounter.insertGrp(this.icCustomer);
        this.icCustomer.customerStatus = CustomerStatus.SERVING;
    }

    @Override
    protected void terminatingEvent() {
        this.model.rgCounter.removeGrp(this.icCustomer);
        double customerServiceTime = this.model.getClock() - this.icCustomer.timeEnterSystem;
        if (this.icCustomer.uType == CustomerType.CHECK_IN
                && customerServiceTime < ACCEPTABLE_CHECK_IN_TIME) {
            this.model.output.numOfSatisfiedCustomer++;
        }
        if (this.icCustomer.uType == CustomerType.CHECK_OUT) {
            this.model.qCustomerLines[CUSTOMERLINE_WAIT_FOR_PICKUP].add(this.icCustomer);
            this.icCustomer.customerStatus = CustomerStatus.WAITING_PICKUP;
        }
        if (this.icCustomer.uType == CustomerType.CHECK_IN) {
            this.icCustomer = null;
        }
        this.model.output.numOfServed++;
    }

}
