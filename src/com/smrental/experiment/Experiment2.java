package com.smrental.experiment;

import cern.jet.random.engine.RandomSeedGenerator;
import com.smrental.procedures.Seeds;
import com.sun.xml.internal.rngom.digested.DDataPattern;
import outputAnalysis.ConfidenceInterval;
import smrental.Parameters;
import smrental.SMRental;

public class Experiment2 {
    public static class ExperimentResult {
        Parameters params;
        double[] serviceLevels;
        double[] costs;
    }


    private static final double START_TIME = 0.0;
    private static final double END_TIME = 270;
    private static final int NUMRUNS = 40;

    private static final double CONF_LEVEL = 0.9;

    private static final int MAX_NUM_VAN = 10;
    private static final int MAX_NUM_AGENT = 15;

    private static final double SERVICE_LEVEL_85 = 0.85;
    private static final double SERVICE_LEVEL_90 = 0.90;

    public static void main(String[] args) {
        ExperimentResult result1 = doExperiment(12, false, SERVICE_LEVEL_85);
        printExperimentResult(12, false, result1, "1A");
        ExperimentResult result3 = doExperiment(12, false, SERVICE_LEVEL_90);
        printExperimentResult(12, false, result3, "1B");
        ExperimentResult result2 = doExperiment(12, true, SERVICE_LEVEL_85);
        printExperimentResult(12, true, result2, "1AI");
        ExperimentResult result4 = doExperiment(12, true, SERVICE_LEVEL_90);
        printExperimentResult(12, true, result4, "1BI");


        ExperimentResult result5 = doExperiment(18, false, SERVICE_LEVEL_85);
        printExperimentResult(18, false, result5, "2A");
        ExperimentResult result7 = doExperiment(18, false, SERVICE_LEVEL_90);
        printExperimentResult(18, false, result7, "2B");
        ExperimentResult result6 = doExperiment(18, true, SERVICE_LEVEL_85);
        printExperimentResult(18, true, result6, "2AI");
        ExperimentResult result8 = doExperiment(18, true, SERVICE_LEVEL_90);
        printExperimentResult(18, true, result8, "2BI");


        ExperimentResult result9 = doExperiment(30, false, SERVICE_LEVEL_85);
        printExperimentResult(30, false, result9, "3A");
        ExperimentResult result11 = doExperiment(30, false, SERVICE_LEVEL_90);
        printExperimentResult(30, false, result11, "3B");
        ExperimentResult result10 = doExperiment(30, true, SERVICE_LEVEL_85);
        printExperimentResult(30, true, result10, "3AI");
        ExperimentResult result12 = doExperiment(30, true, SERVICE_LEVEL_90);
        printExperimentResult(30, true, result12, "3BI");
    }

    public static ExperimentResult doExperiment(final int typeOfVan, final boolean isCustomerIncrease, final double serviceLevel) {
        Seeds[] sds = new Seeds[NUMRUNS];

        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();
        for (int i = 0; i < NUMRUNS; i++) sds[i] = new Seeds(rsg);


        ExperimentResult result = new ExperimentResult();
        double serviceLevels[] = new double[NUMRUNS];
        double costs[] = new double[NUMRUNS];

        int numOfAgent = 0;

        // Set number of van to the max and find the optimal number of agent
        while (numOfAgent <= MAX_NUM_AGENT) {
            numOfAgent++;
            Parameters params = new Parameters();
            params.numberOfAgents = numOfAgent;
            params.numberOfVans = MAX_NUM_VAN;
            params.customerIncrease = isCustomerIncrease;
            params.typeOfVan = typeOfVan;

            for (int i = 0; i < NUMRUNS; i++) {
                SMRental model = new SMRental(START_TIME, END_TIME, sds[i], params, false);
                model.runSimulation();
                serviceLevels[i] = (double) model.output.numOfSatisfiedCustomer / model.output.numOfServed;
            }
            ConfidenceInterval serviceLevelCI = new ConfidenceInterval(serviceLevels, CONF_LEVEL);
            if (serviceLevelCI.getPointEstimate() >= serviceLevel) {
                break;
            }
        }

        // use the number of agent found in the previous step and find the optimal number of van
        int numOfVan = 0;
        while (numOfVan <= MAX_NUM_VAN) {
            numOfVan++;

            Parameters params = new Parameters();
            params.numberOfAgents = numOfAgent;
            params.numberOfVans = numOfVan;
            params.customerIncrease = isCustomerIncrease;
            params.typeOfVan = typeOfVan;

            for (int i = 0; i < NUMRUNS; i++) {
                SMRental model = new SMRental(START_TIME, END_TIME, sds[i], params, false);
                model.runSimulation();
                serviceLevels[i] = (double) model.output.numOfSatisfiedCustomer / model.output.numOfServed;
                costs[i] = model.udp.calculateCosts();
            }
            ConfidenceInterval serviceLevelCI = new ConfidenceInterval(serviceLevels, CONF_LEVEL);
            if (serviceLevelCI.getPointEstimate() >= serviceLevel) {
                result.params = params;
                result.serviceLevels = serviceLevels;
                result.costs = costs;
                break;
            }
        }

        return result;
    }

    private static void printExperimentResult(int typeOfVan, boolean isIncrease, ExperimentResult result, String caseNumer) {

        ConfidenceInterval sl20 = new ConfidenceInterval(slice(result.serviceLevels, 20), CONF_LEVEL);
        ConfidenceInterval sl30 = new ConfidenceInterval(slice(result.serviceLevels, 30), CONF_LEVEL);
        ConfidenceInterval sl40 = new ConfidenceInterval(slice(result.serviceLevels, 40), CONF_LEVEL);

        ConfidenceInterval c20 = new ConfidenceInterval(slice(result.costs, 20), CONF_LEVEL);
        ConfidenceInterval c30 = new ConfidenceInterval(slice(result.costs, 30), CONF_LEVEL);
        ConfidenceInterval c40 = new ConfidenceInterval(slice(result.costs, 40), CONF_LEVEL);

        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.printf("|  Case#: %3s     %d-seat van      numberOfAgents: %d       numberOfVans: %d     customerIncrease: %5s    |\n",
                caseNumer, typeOfVan, result.params.numberOfAgents, result.params.numberOfVans, isIncrease);
        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.println("|        |              Satisfaction Level                |                    Cost                        |");
        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.printf("|   n    |  yb(n)        s(n)       z(n)      z(n)/yb(n)  |   yb(n)      s(n)       zeta       zeta/yb(n)  |\n");
        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.printf("|%7d |%7.3f %11.3f %11.3f %12.4f    | %9.3f %8.3f %9.3f %12.4f      |\n",
                20, sl20.getPointEstimate(), sl20.getStdDev(), sl20.getZeta(), sl20.getZeta() / sl20.getPointEstimate(),
                c20.getPointEstimate(), c20.getStdDev(), c20.getZeta(), c20.getZeta() / c20.getPointEstimate());
        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.printf("|%7d |%7.3f %11.3f %11.3f %12.4f    | %9.3f %8.3f %9.3f %12.4f      |\n",
                30, sl30.getPointEstimate(), sl30.getStdDev(), sl30.getZeta(), sl30.getZeta() / sl30.getPointEstimate(),
                c30.getPointEstimate(), c30.getStdDev(), c30.getZeta(), c30.getZeta() / c30.getPointEstimate());
        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.printf("|%7d |%7.3f %11.3f %11.3f %12.4f    | %9.3f %8.3f %9.3f %12.4f      |\n",
                40, sl40.getPointEstimate(), sl40.getStdDev(), sl40.getZeta(), sl40.getZeta() / sl40.getPointEstimate(),
                c40.getPointEstimate(), c40.getStdDev(), c40.getZeta(), c40.getZeta() / c40.getPointEstimate());
        printLines(1);//   --------------------------------------------------------------------------------------------------------
    }

    private static void printLines(int numLines) {
        for (int i = 0; i < numLines; i++)
            System.out.println("+--------+------------------------------------------------+------------------------------------------------+");
    }

    private static double[] slice(double[] array, int size) {
        double[] values = new double[size];
        System.arraycopy(array, 0, values, 0, size);
        return values;
    }
}
