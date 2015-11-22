// File: Experiment.java
// Description:

import cern.jet.random.engine.*;
import smrental.*;

// Main Method: Experiments
// 
class Experiment
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
       for(i=0 ; i < NUMRUNS ; i++)
       {
          smRental = new SMRental(startTime,endTime,sds[i], true);
          smRental.runSimulation();
          // See examples for hints on collecting output
          // and developping code for analysis
       }
   }
}
