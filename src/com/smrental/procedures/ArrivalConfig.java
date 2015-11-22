package com.smrental.procedures;

public class ArrivalConfig {
	// Customer arrival times for Terminal 1
	public static final double T1_MEAN_1 = 2.5;
	public static final double T1_MEAN_2 = 1.111;
	public static final double T1_MEAN_3 = 0.9375;
	public static final double T1_MEAN_4 = 1.304;
	public static final double T1_MEAN_5 = 7.5;
	public static final double T1_MEAN_6 = 1.25;
	public static final double T1_MEAN_7 = 0.968;
	public static final double T1_MEAN_8 = 3;
	public static final double T1_MEAN_9 = 5;

	public static final double INCREASED_T1_MEAN_1 = 2.083;
	public static final double INCREASED_T1_MEAN_2 = 0.925;
	public static final double INCREASED_T1_MEAN_3 = 0.781;
	public static final double INCREASED_T1_MEAN_4 = 1.087;
	public static final double INCREASED_T1_MEAN_5 = 6.25;
	public static final double INCREASED_T1_MEAN_6 = 1.042;
	public static final double INCREASED_T1_MEAN_7 = 0.806;
	public static final double INCREASED_T1_MEAN_8 = 2.5;
	public static final double INCREASED_T1_MEAN_9 = 4.167;

	// Customer arrival times for Terminal 2
	public static final double T2_MEAN_1 = 3.333;
	public static final double T2_MEAN_2 = 1.25;
	public static final double T2_MEAN_3 = 0.833;
	public static final double T2_MEAN_4 = 1.5;
	public static final double T2_MEAN_5 = 10;
	public static final double T2_MEAN_6 = 0.857;
	public static final double T2_MEAN_7 = 0.968;
	public static final double T2_MEAN_8 = 4.23;
	public static final double T2_MEAN_9 = 5;

	public static final double INCREASED_T2_MEAN_1 = 2.778;
	public static final double INCREASED_T2_MEAN_2 = 1.042;
	public static final double INCREASED_T2_MEAN_3 = 0.735;
	public static final double INCREASED_T2_MEAN_4 = 1.25;
	public static final double INCREASED_T2_MEAN_5 = 8.333;
	public static final double INCREASED_T2_MEAN_6 = 0.714;
	public static final double INCREASED_T2_MEAN_7 = 0.806;
	public static final double INCREASED_T2_MEAN_8 = 3.571;
	public static final double INCREASED_T2_MEAN_9 = 4.167;

	// Customer arrival times for Counter
	public static final double COUNTER_MEAN_1 = 1.429;
	public static final double COUNTER_MEAN_2 = 0.652;
	public static final double COUNTER_MEAN_3 = 0.682;
	public static final double COUNTER_MEAN_4 = 1.111;
	public static final double COUNTER_MEAN_5 = 1.765;
	public static final double COUNTER_MEAN_6 = 0.5;
	public static final double COUNTER_MEAN_7 = 0.625;
	public static final double COUNTER_MEAN_8 = 1.154;
	public static final double COUNTER_MEAN_9 = 3.333;

	public static final double INCREASED_COUNTER_MEAN_1 = 1.190;
	public static final double INCREASED_COUNTER_MEAN_2 = 0.543;
	public static final double INCREASED_COUNTER_MEAN_3 = 0.568;
	public static final double INCREASED_COUNTER_MEAN_4 = 0.925;
	public static final double INCREASED_COUNTER_MEAN_5 = 1.47;
	public static final double INCREASED_COUNTER_MEAN_6 = 0.417;
	public static final double INCREASED_COUNTER_MEAN_7 = 0.521;
	public static final double INCREASED_COUNTER_MEAN_8 = 0.962;
	public static final double INCREASED_COUNTER_MEAN_9 = 2.778;

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
