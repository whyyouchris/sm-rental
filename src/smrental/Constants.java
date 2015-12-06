package smrental;

public class Constants
{
	public static final int ACCEPTABLE_CHECK_IN_TIME = 20; //Acceptable turn around time for check in customer
	public static final int ACCEPTABLE_CHECK_OUT_TIME = 18; // Acceptable turn around time for check out customer

	public static final double VAN_12S_COST_RATE = 0.48; // Per mile cost for a 12-seat van
	public static final double VAN_18S_COST_RATE = 0.73; // Per mile cost for a 18-seat van
	public static final double VAN_30S_COST_RATE = 0.92; // Per mile cost for a 30-seat van

	public static final double AGENT_RATE = 11.5; // Per hour salary of agent
	public static final double DRIVER_RATE = 12.5; // Per hour salary of driver

	public static final double TOTAL_TIME = 4.5; // Total experiment hours

    // VanLine Ids
    public static final int VANLINE_T1 = 0;
    public static final int VANLINE_T2 = 1;
    public static final int VANLINE_COUNTER_PICKUP = 2;
    public static final int VANLINE_COUNTER_DROPOFF = 3;
    public static final int VANLINE_DROPOFF = 4;

    // Customer line Ids
    public static final int CUSTOMERLINE_T1 = 0;
    public static final int CUSTOMERLINE_T2 = 1;
    public static final int CUSTOMERLINE_WAIT_FOR_SERVING = 2;
    public static final int CUSTOMERLINE_WAIT_FOR_PICKUP = 3;

    // Location Ids
    public enum Location { T1, T2, COUNTER, DROP_OFF }

}
