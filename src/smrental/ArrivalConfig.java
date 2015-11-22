package smrental;

public class ArrivalConfig {
	// Customer arrival times for Terminal 1
	public static final double T1_MEAN_1 = 6;
	public static final double T1_MEAN_2 = 13.5;
	public static final double T1_MEAN_3 = 16;
	public static final double T1_MEAN_4 = 11.5;
	public static final double T1_MEAN_5 = 4;
	public static final double T1_MEAN_6 = 12;
	public static final double T1_MEAN_7 = 15.5;
	public static final double T1_MEAN_8 = 5;
	public static final double T1_MEAN_9 = 3;

	// Customer arrival times for Terminal 2
	public static final double T2_MEAN_1 = 4.5;
	public static final double T2_MEAN_2 = 12;
	public static final double T2_MEAN_3 = 18;
	public static final double T2_MEAN_4 = 10;
	public static final double T2_MEAN_5 = 3;
	public static final double T2_MEAN_6 = 17.5;
	public static final double T2_MEAN_7 = 15.5;
	public static final double T2_MEAN_8 = 3.5;
	public static final double T2_MEAN_9 = 3;

	// Customer arrival times for Counter
	public static final double COUNTER_MEAN_1 = 10.5;
	public static final double COUNTER_MEAN_2 = 23;
	public static final double COUNTER_MEAN_3 = 22;
	public static final double COUNTER_MEAN_4 = 18.5;
	public static final double COUNTER_MEAN_5 = 17;
	public static final double COUNTER_MEAN_6 = 30;
	public static final double COUNTER_MEAN_7 = 24;
	public static final double COUNTER_MEAN_8 = 13;
	public static final double COUNTER_MEAN_9 = 4.5;
	
	//Min and Max for service times
	public static final double STCIMIN = 1.6;
	public static final double STCIMAX = 5.1;
	public static final double STCOMIN = 1;
	public static final double STCOMAX = 4.8;
	
	// Percentage of additional customers
	public static final double ZERO_ADDITIONAL_PASSENGER = 0.6;
	public static final double ONE_ADDITIONAL_PASSENGER = 0.2;
	public static final double TWO_ADDITIONAL_PASSENGER = 0.15;
	public static final double THREE_ADDITIONAL_PASSENGER = 0.05;
	
	//Average boarding time
	public static final double AVERAGE_BOARDING_TIME = 0.2; // 12s => 0.2min
	public static final double AVERAGE_EXISTING_TIME = 0.1; // 6s => 0.1min
}
