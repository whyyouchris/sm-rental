package com.smrental.experiment;

import cern.jet.random.engine.RandomSeedGenerator;
import com.smrental.models.VanType;
import com.smrental.procedures.Seeds;
import com.smrental.utils.Parameters;
import smrental.SMRental;

public class Experiment
{

	private static final double START_TIME = 0.0;
	private static final double END_TIME = 270;
	private static final double SATISFACTION_85 = 0.85;
	private static final double SATISFACTION_90 = 0.90;
	private static final int 	MAX_NUM_AGENTS = 10;
	private static final int	MAX_NUM_VANS = 10;

   public static void main(String[] args) {
       int i, NUMRUNS = 1;

       Seeds[] sds = new Seeds[NUMRUNS];

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);

       int typeOfVan = VanType.SEAT12.getSeats();
       int numberOfAgents = 13;
       int numberOfVans = 5;
       
       boolean customerIncrease = false;
       Parameters params = new Parameters.Builder()
  			 .typeOfVan(typeOfVan)
  			 .numberOfAgents(numberOfAgents)
  			 .numberOfVans(numberOfVans)
  			 .customerIncrease(customerIncrease)
  			 .build();

       for(i=0 ; i < NUMRUNS ; i++) {
		   System.out.println("configuration" + params);
    	   SMRental model = new SMRental(START_TIME, END_TIME, sds[i], params, true);
    	   model.runSimulation();
		   double serviceLevel = (double) model.output.numOfSatistifiedCustomer / model.output.numOfServed;
    	   System.out.println("Service Level: " + serviceLevel);
    	   System.out.println("Total cost for the configuration: " + model.udp.calculateCosts());
       }
   }
}
