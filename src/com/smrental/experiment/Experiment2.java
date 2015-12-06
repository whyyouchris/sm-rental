package com.smrental.experiment;

import cern.jet.random.engine.RandomSeedGenerator;
import com.smrental.procedures.Seeds;
import smrental.Parameters;
import smrental.SMRental;

public class Experiment2 {
    private static final double START_TIME = 0.0;
    private static final double END_TIME   = 270;
    private static final int    NUMRUNS    = 10;

    private static final int    MAX_NUM_VAN     = 10;
    private static final int    MAX_NUM_AGENT   = 15;

    private static final double SERVICE_LEVEL_85 = 0.85;
    private static final double SERVICE_LEVEL_90 = 0.90;

    public static void main(String[] args){
        Seeds[] sds = new Seeds[NUMRUNS];

        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();
        for(int i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);

        // Without customer increase
        boolean customerIncrease = false;
        int numOfAgent = MAX_NUM_AGENT;
        int numOfVan = 0;
        int typeOfVan = 12;

        double serviceLevels[] = new double [NUMRUNS];
        double costs[] = new double[NUMRUNS];

        while (numOfAgent > 0) {
            numOfVan++;
            Parameters params = new Parameters.Builder()
                    .numberOfAgents(numOfAgent)
                    .numberOfVans(numOfVan)
                    .customerIncrease(customerIncrease)
                    .typeOfVan(typeOfVan)
                    .build();

            for (int i = 0; i < NUMRUNS; i++) {
                SMRental model = new SMRental(START_TIME, END_TIME, sds[i], params, false);
                model.runSimulation();
                serviceLevels[i] = (double) model.output.numOfSatisfiedCustomer / model.output.numOfServed;
                costs[i] = model.udp.calculateCosts();
            }
            numOfAgent--;
        }
    }
}
