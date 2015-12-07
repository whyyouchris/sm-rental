package com.smrental.experiment;

import cern.jet.random.engine.RandomSeedGenerator;
import com.smrental.entities.VanType;
import com.smrental.procedures.Seeds;
import smrental.Parameters;
import smrental.SMRental;


public class Experiment1 {

    // Please make sure you have JDK 8 installed in order to run the project
    private static final double START_TIME = 0.0;
    private static final double END_TIME = 270;
    private static final int NUMRUNS = 40;

    public static void main(String[] args) {

        Seeds[] sds = new Seeds[NUMRUNS];

        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();

        for (int i = 0; i < NUMRUNS; i++) sds[i] = new Seeds(rsg);

        // Experiment1 Params
        int typeOfVan = VanType.SEAT12.getSeats();
        int numberOfAgents = 11;
        int numberOfVans = 5;
        boolean customerIncrease = false;

        Parameters params = new Parameters();
        params.typeOfVan = typeOfVan;
        params.numberOfAgents = numberOfAgents;
        params.numberOfVans = numberOfVans;
        params.customerIncrease = customerIncrease;

        for (int i = 0; i < NUMRUNS; i++) {
            System.out.println("configuration" + params);
            SMRental model = new SMRental(START_TIME, END_TIME, sds[i], params, true);
            model.runSimulation();
            double serviceLevel = (double) model.output.numOfSatisfiedCustomer / model.output.numOfServed;
            System.out.println("Service Level: " + serviceLevel);
            System.out.println("Total cost for the configuration: " + model.udp.calculateCosts());
        }
    }
}
