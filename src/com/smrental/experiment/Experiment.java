package com.smrental.experiment;

import com.smrental.models.VanType;
import com.smrental.procedures.Seeds;
import com.smrental.utils.Parameters;

import cern.jet.random.engine.*;
import smrental.*;

public class Experiment
{

	private static final double START_TIME = 0.0;
	private static final double END_TIME = 270;
	private static final double SATISFACTION_85 = 0.85;
	private static final double SATISFACTION_90 = 0.90;
	private static final int 	MAX_NUM_AGENTS = 10;
	private static final int	MAX_NUM_VANS = 10;

   public static void main(String[] args) {
       int i, NUMRUNS = 10;

       Seeds[] sds = new Seeds[NUMRUNS];

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);

       int typeOfVan = VanType.SEAT12.getSeats();
       int numberOfAgents = 10;
       int numberOfVans = 10;
       boolean customerIncrease = false;
       Parameters params = new Parameters.Builder()
  			 .typeOfVan(typeOfVan)
  			 .numberOfAgents(numberOfAgents)
  			 .numberOfVans(numberOfVans)
  			 .customerIncrease(customerIncrease)
  			 .build();

       for(i=0 ; i < NUMRUNS ; i++) {
    	   SMRental model = new SMRental(START_TIME, END_TIME, sds[i], params, true);
    	   model.runSimulation();
    	   System.out.println("result: "+ model.output.satisfactionLevel);
       }
   }

   private static void experimentProcedure(Parameters params, Seeds sds, boolean log) {
	   final int maxNumAgents = 10;
	   final int maxNumVan = 10;

	   while (true) {
		   SMRental model = new SMRental(START_TIME, END_TIME, sds, params, log);
		   model.runSimulation();
		   if(model.output.satisfactionLevel >= SATISFACTION_85){
			   break;
		   } else {
			   
		   }
	   }
   }
}
