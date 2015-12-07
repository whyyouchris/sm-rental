package com.smrental.experiment;

import outputAnalysis.ConfidenceInterval;

public class Experiment3 {
    private static final double CONF_LEVEL = 0.975;

    private static final double SERVICE_LEVEL_85 = 0.85;
    private static final double SERVICE_LEVEL_90 = 0.90;

    public static void main(String[] args) {
        ExperimentResult result1 = Experiment2.doExperiment(12, false, SERVICE_LEVEL_85);
        ExperimentResult result5 = Experiment2.doExperiment(18, false, SERVICE_LEVEL_85);
        ExperimentResult result9 = Experiment2.doExperiment(30, false, SERVICE_LEVEL_85);

        ExperimentResult result3 = Experiment2.doExperiment(12, false, SERVICE_LEVEL_90);
        ExperimentResult result7 = Experiment2.doExperiment(18, false, SERVICE_LEVEL_90);
        ExperimentResult result11 = Experiment2.doExperiment(30, false, SERVICE_LEVEL_90);

        ExperimentResult result2 = Experiment2.doExperiment(12, true, SERVICE_LEVEL_85);
        ExperimentResult result6 = Experiment2.doExperiment(18, true, SERVICE_LEVEL_85);
        ExperimentResult result10 = Experiment2.doExperiment(30, true, SERVICE_LEVEL_85);

        ExperimentResult result4 = Experiment2.doExperiment(12, true, SERVICE_LEVEL_90);
        ExperimentResult result8 = Experiment2.doExperiment(18, true, SERVICE_LEVEL_90);
        ExperimentResult result12 = Experiment2.doExperiment(30, true, SERVICE_LEVEL_90);

        printSatisfactionTableHeading();
        printCompareServiceLevel("2A-1A", result5, result1);
        printCompareServiceLevel("3A-1A", result9, result1);
        printCompareServiceLevel("3A-2A", result9, result5);

        printCostTableHeading();
        printCompareCost("2A-1A", result5, result1);
        printCompareCost("3A-1A", result9, result1);
        printCompareCost("3A-2A", result9, result5);

        printSatisfactionTableHeading();
        printCompareServiceLevel("2B-1B", result7, result3);
        printCompareServiceLevel("3B-1B", result11, result3);
        printCompareServiceLevel("3B-2B", result11, result7);

        printCostTableHeading();
        printCompareCost("2B-1B", result7, result3);
        printCompareCost("3B-1B", result11, result3);
        printCompareCost("3B-2B", result11, result7);

        printSatisfactionTableHeading();
        printCompareServiceLevel("2AI-1AI", result6, result2);
        printCompareServiceLevel("3AI-1AI", result10, result2);
        printCompareServiceLevel("3AI-2AI", result10, result6);

        printCostTableHeading();
        printCompareCost("2AI-1AI", result6, result2);
        printCompareCost("3AI-1AI", result10, result2);
        printCompareCost("3AI-2AI", result10, result6);

        printSatisfactionTableHeading();
        printCompareServiceLevel("2BI-1BI", result8, result4);
        printCompareServiceLevel("3BI-1BI", result12, result4);
        printCompareServiceLevel("3BI-2BI", result12, result8);

        printCostTableHeading();
        printCompareCost("2BI-1BI", result8, result4);
        printCompareCost("3BI-1BI", result12, result4);
        printCompareCost("3BI-2BI", result12, result8);

    }


    private static void printCostTableHeading() {
        System.out.println("+------------------------------------------------------------------------------------------+");
        System.out.println("|                                       Cost                                               |");
        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.println("|Comparison |  Point estimate (ybar(n))    s(n)    zeta   |zeta/yb(n)|    CI Min    CI Max |");
        printLines(1);//   --------------------------------------------------------------------------------------------------------
    }

    private static void printSatisfactionTableHeading() {
        System.out.println("+------------------------------------------------------------------------------------------+");
        System.out.println("|                                     Satisfaction Level                                   |");
        printLines(1);//   --------------------------------------------------------------------------------------------------------
        System.out.println("|Comparison |  Point estimate (ybar(n))    s(n)    zeta   |zeta/yb(n)|    CI Min    CI Max |");
        printLines(1);//   --------------------------------------------------------------------------------------------------------
    }

    private static void printCompareServiceLevel(String caseNumber, ExperimentResult result1, ExperimentResult result2) {

        ConfidenceInterval sl1 = new ConfidenceInterval(result1.serviceLevels,CONF_LEVEL);
        ConfidenceInterval sl2 = new ConfidenceInterval(result2.serviceLevels, CONF_LEVEL);

        System.out.printf("|%9s  |%12.3f %22.3f %8.3f %8.4f %13.4f %9.4f |\n",
                caseNumber,
                sl2.getPointEstimate() - sl1.getPointEstimate(),
                sl2.getStdDev() - sl1.getStdDev(),
                sl2.getZeta() - sl1.getZeta(),
                Math.abs(sl2.getZeta() / sl2.getPointEstimate()  - (sl1.getZeta() / sl1.getPointEstimate())),
                sl2.getPointEstimate() - sl1.getPointEstimate() - Math.abs((sl2.getZeta() / sl2.getPointEstimate())  - (sl1.getZeta() / sl1.getPointEstimate())),
                sl2.getPointEstimate() - sl1.getPointEstimate() + Math.abs((sl2.getZeta() / sl2.getPointEstimate())  - (sl1.getZeta() / sl1.getPointEstimate()))
        );
        printLines(1);//   --------------------------------------------------------------------------------------------------------
    }

    private static void printCompareCost(String caseNumber, ExperimentResult result1, ExperimentResult result2) {

        ConfidenceInterval c1 = new ConfidenceInterval(result1.costs, CONF_LEVEL);
        ConfidenceInterval c2 = new ConfidenceInterval(result2.costs, CONF_LEVEL);

        System.out.printf("|%9s  |%12.3f %22.3f %8.3f %8.4f %13.4f %9.4f |\n",
                caseNumber,
                c2.getPointEstimate() - c1.getPointEstimate(),
                c2.getStdDev() - c1.getStdDev(),
                c2.getZeta() - c1.getZeta(),
                Math.abs(c2.getZeta() / c2.getPointEstimate()  - (c1.getZeta() / c1.getPointEstimate())),
                c2.getPointEstimate() - c1.getPointEstimate() - Math.abs((c2.getZeta() / c2.getPointEstimate())  - (c1.getZeta() / c1.getPointEstimate())),
                c2.getPointEstimate() - c1.getPointEstimate() + Math.abs((c2.getZeta() / c2.getPointEstimate())  - (c1.getZeta() / c1.getPointEstimate()))
        );
        printLines(1);//   --------------------------------------------------------------------------------------------------------
    }

    private static void printLines(int numLines)
    {
        for (int i = 0; i < numLines; i++)
            System.out.println("+-----------+------------------------------------------------------------------------------+");
    }
}
