package smrental;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds 
{
	int cistm;  		// Check in service time
	int costm;  		// Check out service time

	int t1Arrival;  	// T1 customer arrivals
	int t2Arrival;  	// T2 customer arrivals
	int counterArrival; // counter customer arrivals

	int ac; 			// Additional customers
	int boardingTime;   // Boarding time
	int exitingTime;	// exiting time

	public Seeds(RandomSeedGenerator rsg)
	{
		cistm = rsg.nextSeed();
		costm = rsg.nextSeed();
		t1Arrival = rsg.nextSeed();
		t2Arrival = rsg.nextSeed();
		counterArrival = rsg.nextSeed();
		ac = rsg.nextSeed();
		boardingTime = rsg.nextSeed();
		exitingTime = rsg.nextSeed();
	}
}
