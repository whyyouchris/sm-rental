package com.smrental.procedures;

import cern.jet.random.Exponential;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import com.smrental.entities.Customer.CustomerType;
import smrental.SMRental;

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

    // Customer arrival times for Terminal 1
    private static final double T1_MEAN_1 = 2.5;
    private static final double T1_MEAN_2 = 1.111;
    private static final double T1_MEAN_3 = 0.9375;
    private static final double T1_MEAN_4 = 1.304;
    private static final double T1_MEAN_5 = 7.5;
    private static final double T1_MEAN_6 = 1.25;
    private static final double T1_MEAN_7 = 0.968;
    private static final double T1_MEAN_8 = 3;
    private static final double T1_MEAN_9 = 5;

    public static final double INCREASED_T1_MEAN_1 = 2.083;
    public static final double INCREASED_T1_MEAN_2 = 0.925;
    public static final double INCREASED_T1_MEAN_3 = 0.781;
    public static final double INCREASED_T1_MEAN_4 = 1.087;
    public static final double INCREASED_T1_MEAN_5 = 6.25;
    public static final double INCREASED_T1_MEAN_6 = 1.042;
    public static final double INCREASED_T1_MEAN_7 = 0.806;
    public static final double INCREASED_T1_MEAN_8 = 2.5;
    public static final double INCREASED_T1_MEAN_9 = 4.167;
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

    // Customer arrival times for Terminal 2
    private static final double T2_MEAN_1 = 3.333;
    private static final double T2_MEAN_2 = 1.25;
    private static final double T2_MEAN_3 = 0.833;
    private static final double T2_MEAN_4 = 1.5;
    private static final double T2_MEAN_5 = 10;
    private static final double T2_MEAN_6 = 0.857;
    private static final double T2_MEAN_7 = 0.968;
    private static final double T2_MEAN_8 = 4.23;
    private static final double T2_MEAN_9 = 5;

    private static final double INCREASED_T2_MEAN_1 = 2.778;
    private static final double INCREASED_T2_MEAN_2 = 1.042;
    private static final double INCREASED_T2_MEAN_3 = 0.735;
    private static final double INCREASED_T2_MEAN_4 = 1.25;
    private static final double INCREASED_T2_MEAN_5 = 8.333;
    private static final double INCREASED_T2_MEAN_6 = 0.714;
    private static final double INCREASED_T2_MEAN_7 = 0.806;
    private static final double INCREASED_T2_MEAN_8 = 3.571;
    private static final double INCREASED_T2_MEAN_9 = 4.167;
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

    // Customer arrival times for Counter
    private static final double COUNTER_MEAN_1 = 1.429;
    private static final double COUNTER_MEAN_2 = 0.652;
    private static final double COUNTER_MEAN_3 = 0.682;
    private static final double COUNTER_MEAN_4 = 1.111;
    private static final double COUNTER_MEAN_5 = 1.765;
    private static final double COUNTER_MEAN_6 = 0.5;
    private static final double COUNTER_MEAN_7 = 0.625;
    private static final double COUNTER_MEAN_8 = 1.154;
    private static final double COUNTER_MEAN_9 = 3.333;

    private static final double INCREASED_COUNTER_MEAN_1 = 1.190;
    private static final double INCREASED_COUNTER_MEAN_2 = 0.543;
    private static final double INCREASED_COUNTER_MEAN_3 = 0.568;
    private static final double INCREASED_COUNTER_MEAN_4 = 0.925;
    private static final double INCREASED_COUNTER_MEAN_5 = 1.47;
    private static final double INCREASED_COUNTER_MEAN_6 = 0.417;
    private static final double INCREASED_COUNTER_MEAN_7 = 0.521;
    private static final double INCREASED_COUNTER_MEAN_8 = 0.962;
    private static final double INCREASED_COUNTER_MEAN_9 = 2.778;
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

	//Min and Max for service times
	private static final double STCIMIN = 1.6;
	private static final double STCIMAX = 5.1;
	private static final double STCOMIN = 1;
	private static final double STCOMAX = 4.8;
	/**
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

	// Percentage of additional customers
	private static final double ONE_ADDITIONAL_PASSENGER = 0.2;
	private static final double TWO_ADDITIONAL_PASSENGER = 0.15;
	private static final double THREE_ADDITIONAL_PASSENGER = 0.05;
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

	//Average boarding time
	private static final double AVERAGE_BOARDING_TIME = 0.2; // 12s => 0.2min
	/**
	 * This method return the total boarding time
	 * of the customer and additional customers
	 *
	 * @param numberOfPassengers
	 * @return double - uBoardingTime
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

	//Average existing time
	private static final double AVERAGE_EXISTING_TIME = 0.1; // 6s => 0.1min
	/**
	 * This method return the total exiting time
	 * of the customer and additional customers
	 *
	 * @param numberOfPassengers
	 * @return double - uExitingTime
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
