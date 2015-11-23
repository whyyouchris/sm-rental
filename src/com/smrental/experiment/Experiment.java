package com.smrental.experiment;

import com.smrental.models.VanType;
import com.smrental.procedures.Seeds;
import com.smrental.utils.Parameters;

import cern.jet.random.engine.*;
import smrental.*;

public class Experiment
{

   
   public static void main(String[] args)
   {
       int i, NUMRUNS = 30; 
       double startTime=0.0, endTime=660.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMRental smRental;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
       // Loop for NUMRUN simulation runs for each case
       // Case 1
       System.out.println(" Case 1");
       int typeOfVan = VanType.SEAT12.getSeats();
       int numberOfAgents = 10;
       int numberOfVans = 10;
       boolean customerIncrease = false;
       for(i=0 ; i < NUMRUNS ; i++)
       {
    	  Parameters params = new Parameters.Builder()
    			 .typeOfVan(typeOfVan)
    			 .numberOfAgents(numberOfAgents)
    			 .numberOfVans(numberOfVans)
    			 .customerIncrease(customerIncrease)
    			 .build();
    			 
          smRental = new SMRental(startTime,endTime,sds[i], params, true);
          smRental.runSimulation();
          // See examples for hints on collecting output
          // and developping code for analysis
       }
   }
}
