package smrental;

import cern.jet.random.Exponential;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;

import static smrental.ArrivalConfig.*;
class RVPs 
{
	
	SMRental model;
	
	private Exponential t1ArrDist;  // Exponential distribution for T1 interarrival times
	private Exponential t2ArrDist; // Exponential distribution for T2 interarrival times
	private Exponential counterArrDist; // Exponential distribution for counter interarrival times


	private MersenneTwister additionalPassengerGen;// additional passenger
	private Exponential boardingTimeDist; // Boarding time distribution
	private Exponential exitingTimeDist; // Exiting time distribution

	private Uniform checkInServiceTime; // Check in customer service time distribution
	private Uniform checkOutServiceTime;// Check out customer service time distribution

	protected RVPs(SMRental model, Seeds sd) { 
		this.model = model; 
		// Set up distribution functions

		//Arrivals
		this.t1ArrDist = new Exponential(1.0/T1_MEAN_1, new MersenneTwister(sd.t1Arrival));
		this.t2ArrDist = new Exponential(1.0/T2_MEAN_1, new MersenneTwister(sd.t2Arrival));
		this.counterArrDist = new Exponential(1.0/COUNTER_MEAN_1, new MersenneTwister(sd.counterArrival));

		//boarding & unboarding
		this.additionalPassengerGen = new MersenneTwister(sd.ac);
		this.boardingTimeDist = new Exponential(1.0/AVERAGE_BOARDING_TIME, new MersenneTwister(sd.boardingTime));
		this.exitingTimeDist = new Exponential(1.0/AVERAGE_EXISTING_TIME, new MersenneTwister(sd.exitingTime));

		//service time
		this.checkInServiceTime = new Uniform(STCIMIN, STCIMAX, sd.cistm);
		this.checkOutServiceTime = new Uniform(STCOMIN, STCOMAX, sd.costm);
	}
	
	public double DuCT1(boolean customerIncrease) {
		double nextArrival;
		double mean;
		if (this.model.getClock() <= 30) mean = T1_MEAN_1;
		else if (this.model.getClock() <= 60) mean = T1_MEAN_2;
		else if (this.model.getClock() <= 90) mean = T1_MEAN_3;
		else if (this.model.getClock() <= 120) mean = T1_MEAN_4;
		else if (this.model.getClock() <= 150) mean = T1_MEAN_5;
		else if (this.model.getClock() <= 180) mean = T1_MEAN_6;
		else if (this.model.getClock() <= 210) mean = T1_MEAN_7;
		else if (this.model.getClock() <= 240) mean = T1_MEAN_8;
		else mean = T1_MEAN_9;
		nextArrival = this.model.getClock()+ this.t1ArrDist.nextDouble(1.0/mean);
		if (nextArrival > this.model.closingTime)
			nextArrival = -1.0;
		
		return nextArrival;
	}

	public double DuCT2(boolean customerIncrease) {
		double nextArrival;
		double mean;
		if (this.model.getClock() <= 30) mean = T2_MEAN_1;
		else if (this.model.getClock() <= 60) mean = T2_MEAN_2;
		else if (this.model.getClock() <= 90) mean = T2_MEAN_3;
		else if (this.model.getClock() <= 120) mean = T2_MEAN_4;
		else if (this.model.getClock() <= 150) mean = T2_MEAN_5;
		else if (this.model.getClock() <= 180) mean = T2_MEAN_6;
		else if (this.model.getClock() <= 210) mean = T2_MEAN_7;
		else if (this.model.getClock() <= 240) mean = T2_MEAN_8;
		else mean = T2_MEAN_9;
		nextArrival = this.model.getClock()+ this.t2ArrDist.nextDouble(1.0/mean);
		if (nextArrival > this.model.closingTime)
			nextArrival = -1.0;
		
		return nextArrival;
	}
	
	public double DuCCounter(boolean customerIncrease) {
		double nextArrival;
		double mean;
		if (this.model.getClock() <= 30) mean = COUNTER_MEAN_1;
		else if (this.model.getClock() <= 60) mean = COUNTER_MEAN_2;
		else if (this.model.getClock() <= 90) mean = COUNTER_MEAN_3;
		else if (this.model.getClock() <= 120) mean = COUNTER_MEAN_4;
		else if (this.model.getClock() <= 150) mean = COUNTER_MEAN_5;
		else if (this.model.getClock() <= 180) mean = COUNTER_MEAN_6;
		else if (this.model.getClock() <= 210) mean = COUNTER_MEAN_7;
		else if (this.model.getClock() <= 240) mean = COUNTER_MEAN_8;
		else mean = COUNTER_MEAN_9;
		nextArrival = this.model.getClock()+ this.counterArrDist.nextDouble(1.0/mean);
		if (nextArrival > this.model.closingTime)
			nextArrival = -1.0;

		return nextArrival;
	}

	public double uServiceTime(CustomerType type) {
		double serviceTime = -1;
		if (type == CustomerType.CHECK_IN) {
			serviceTime = this.checkInServiceTime.nextDouble();
		}
		if (type == CustomerType.CHECK_OUT) {
			serviceTime = this.checkOutServiceTime.nextDouble();
		}
		return serviceTime;
	}
	
	public int additionalPassengers() {
		double randNum = this.additionalPassengerGen.nextDouble();
		int numberOfAdditionalPassenger = -1;
		if (randNum < THREE_ADDITIONAL_PASSENGER) {
			numberOfAdditionalPassenger = 3;
		} else if (randNum < TWO_ADDITIONAL_PASSENGER) {
			numberOfAdditionalPassenger = 2;
		} else if (randNum < ONE_ADDITIONAL_PASSENGER) {
			numberOfAdditionalPassenger = 1;
		} else {
			numberOfAdditionalPassenger = 0;
		}
		return numberOfAdditionalPassenger;
	}
	
	/**
	 * This method return the total boarding time 
	 * of the customer and additional customers  
	 * 
	 * @param numberOfPassengers
	 * @return double - boardingTime
	 */
	public double boardingTime(int numberOfPassengers) {
		double boardingTime = 0;
		// Customer himself should also be considered as an passenger
		numberOfPassengers++;
		while (numberOfPassengers>0) {
			boardingTime += this.boardingTimeDist.nextDouble();
			numberOfPassengers--;
		}
		return boardingTime;
	}
	
	public double exitingTime(int numberOfPassengers) {
		double boardingTime = 0;
		// Customer himself should also be considered as an passenger
		numberOfPassengers++;
		while (numberOfPassengers>0) {
			boardingTime += this.exitingTimeDist.nextDouble();
			numberOfPassengers--;
		}
		return boardingTime;
	}
}
