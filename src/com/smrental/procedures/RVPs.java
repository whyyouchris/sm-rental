package com.smrental.procedures;

import cern.jet.random.Exponential;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import com.smrental.models.CustomerType;
import smrental.SMRental;

import static com.smrental.procedures.ArrivalConfig.*;
public class RVPs 
{
	
	SMRental model;
	
	private Exponential t1ArrDist;  // Exponential distribution for T1 interarrival times
	private Exponential t2ArrDist; // Exponential distribution for T2 interarrival times
	private Exponential counterArrDist; // Exponential distribution for counter interarrival times


	private MersenneTwister additionalPassengerGen;// additional passenger
	private Exponential boardingTimeDist; // Boarding time distribution
	private Exponential exitingTimeDist; // Exiting time distribution

	private Uniform uCheckInServiceTime; // Check in customer service time distribution
	private Uniform uCheckOutServiceTime;// Check out customer service time distribution

	public RVPs(SMRental model, Seeds sd) { 
		
		
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
		this.uCheckInServiceTime = new Uniform(STCIMIN, STCIMAX, sd.cistm);
		this.uCheckOutServiceTime = new Uniform(STCOMIN, STCOMAX, sd.costm);
	}
	
	/**
	 * 
	 * @param customerIncrease
	 * @return customer in T1
	 */

	public double DuCT1(boolean customerIncrease) {
		double nextArrival;
		double mean;
		if (this.model.getClock() <= 30) {
			mean = customerIncrease? INCREASED_T1_MEAN_1 : T1_MEAN_1;
		}
		else if (this.model.getClock() <= 60) {
			mean = customerIncrease? INCREASED_T1_MEAN_2 : T1_MEAN_2;
		}
		else if (this.model.getClock() <= 90) {
			mean = customerIncrease? INCREASED_T1_MEAN_3 : T1_MEAN_3;
		}
		else if (this.model.getClock() <= 120) {
			mean = customerIncrease? INCREASED_T1_MEAN_4 : T1_MEAN_4;
		}
		else if (this.model.getClock() <= 150) {
			mean = customerIncrease? INCREASED_T1_MEAN_5 : T1_MEAN_5;
		}
		else if (this.model.getClock() <= 180) { 
			mean = customerIncrease? INCREASED_T1_MEAN_6 : T1_MEAN_6;
		}
		else if (this.model.getClock() <= 210) {
			mean = customerIncrease ? INCREASED_T1_MEAN_7 : T1_MEAN_7;
		}
		else if (this.model.getClock() <= 240) {
			mean = customerIncrease ? INCREASED_T1_MEAN_8 : T1_MEAN_8;
		}
		else {
			mean = customerIncrease? INCREASED_T1_MEAN_9 : T1_MEAN_9;
		}
		nextArrival = this.model.getClock()+ this.t1ArrDist.nextDouble(1.0/mean);
		if (nextArrival > this.model.closingTime) {
			nextArrival = -1.0;
		}
		return nextArrival;
	}
	
	/**
	 * 
	 * @param customerIncrease
	 * @return customer in T2
	 */

	public double DuCT2(boolean customerIncrease) {
		double nextArrival;
		double mean;
		if (this.model.getClock() <= 30) {
			mean = customerIncrease? INCREASED_T2_MEAN_1 : T2_MEAN_1;
		}
		else if (this.model.getClock() <= 60) {
			mean = customerIncrease? INCREASED_T2_MEAN_2 : T2_MEAN_2;
		}
		else if (this.model.getClock() <= 90) {
			mean = customerIncrease? INCREASED_T2_MEAN_3 : T2_MEAN_3;
		}
		else if (this.model.getClock() <= 120) {
			mean = customerIncrease? INCREASED_T2_MEAN_4 : T2_MEAN_4;
		}
		else if (this.model.getClock() <= 150) {
			mean = customerIncrease? INCREASED_T2_MEAN_5 : T2_MEAN_5;
		}
		else if (this.model.getClock() <= 180) { 
			mean = customerIncrease? INCREASED_T2_MEAN_6 : T2_MEAN_6;
		}
		else if (this.model.getClock() <= 210) {
			mean = customerIncrease ? INCREASED_T2_MEAN_7 : T2_MEAN_7;
		}
		else if (this.model.getClock() <= 240) {
			mean = customerIncrease ? INCREASED_T2_MEAN_8 : T2_MEAN_8;
		}
		else {
			mean = customerIncrease? INCREASED_T2_MEAN_9 : T2_MEAN_9;
		}
		nextArrival = this.model.getClock()+ this.t2ArrDist.nextDouble(1.0/mean);
		if (nextArrival > this.model.closingTime) {
			nextArrival = -1.0;
		}
		return nextArrival;
	}
	
	/**
	 * 
	 * @param customerIncrease
	 * @return customer in Counter
	 */
	public double DuCCounter(boolean customerIncrease) {
		double nextArrival;
		double mean;
		if (this.model.getClock() <= 30) {
			mean = customerIncrease? INCREASED_COUNTER_MEAN_1 : COUNTER_MEAN_1;
		}
		else if (this.model.getClock() <= 60) {
			mean = customerIncrease? INCREASED_COUNTER_MEAN_2 : COUNTER_MEAN_2;
		}
		else if (this.model.getClock() <= 90) {
			mean = customerIncrease? INCREASED_COUNTER_MEAN_3 : COUNTER_MEAN_3;
		}
		else if (this.model.getClock() <= 120) {
			mean = customerIncrease? INCREASED_COUNTER_MEAN_4 : COUNTER_MEAN_4;
		}
		else if (this.model.getClock() <= 150) {
			mean = customerIncrease? INCREASED_COUNTER_MEAN_5 : COUNTER_MEAN_5;
		}
		else if (this.model.getClock() <= 180) { 
			mean = customerIncrease? INCREASED_COUNTER_MEAN_6 : COUNTER_MEAN_6;
		}
		else if (this.model.getClock() <= 210) {
			mean = customerIncrease ? INCREASED_COUNTER_MEAN_7 : COUNTER_MEAN_7;
		}
		else if (this.model.getClock() <= 240) {
			mean = customerIncrease ? INCREASED_COUNTER_MEAN_8 : COUNTER_MEAN_8;
		}
		else {
			mean = customerIncrease? INCREASED_COUNTER_MEAN_9 : COUNTER_MEAN_9;
		}
		nextArrival = this.model.getClock()+ this.counterArrDist.nextDouble(1.0/mean);
		if (nextArrival > this.model.closingTime) {
			nextArrival = -1.0;
		}
		return nextArrival;
	}
   
	/**
	 * 
	 * @param uType
	 * @return service time
	 */
	public double uServiceTime(CustomerType uType) {
		double serviceTime = -1;
		if (uType == CustomerType.CHECK_IN) {
			serviceTime = this.uCheckInServiceTime.nextDouble();
		}
		if (uType == CustomerType.CHECK_OUT) {
			serviceTime = this.uCheckOutServiceTime.nextDouble();
		}
		return serviceTime;
	}
	
	/**
	 * 
	 * @return number of additional customers
	 */
	public int uAdditionalPassengers() {
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
	public double uBoardingTime(int numberOfPassengers) {
		double boardingTime = 0;
		// Customer himself should also be considered as an passenger
		numberOfPassengers++;
		while (numberOfPassengers>0) {
			boardingTime += this.boardingTimeDist.nextDouble();
			numberOfPassengers--;
		}
		return boardingTime/60;
	}

	/**
	 * This method return the total exiting time 
	 * of the customer and additional customers  
	 * 
	 * @param numberOfPassengers
	 * @return double - exitingTime
	 */
	public double uExitingTime(int numberOfPassengers) {
		double exitingTime = 0;
		// Customer himself should also be considered as an passenger
		numberOfPassengers++;
		while (numberOfPassengers>0) {
			exitingTime += this.exitingTimeDist.nextDouble();
			numberOfPassengers--;
		}
		return exitingTime/60;
	}
}
