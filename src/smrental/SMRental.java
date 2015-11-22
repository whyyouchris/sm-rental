package smrental;

import java.util.LinkedList;
import java.util.List;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;
import simulationModelling.SequelActivity;

//
// The Simulation model Class
public class SMRental extends AOSimulationModel
{
	// Constants available from Constants class
	/* Parameter */
    // Define the parameters
	private boolean traceFlag;
	protected double startingTime;
	protected double closingTime;

	/*-------------Entity Data Structures-------------------*/
	/* Group and Queue Entities */
	// Define the reference variables to the various 
	// entities with scope Set and Unary
	// Objects can be created here or in the Initialise Action
	protected Counter rgCounter = new Counter();
	protected List<Customer>[] qCustomerLines = new LinkedList[4];
	protected List<Van>[] qVanLines = new LinkedList[4];
	
	/* Input Variables */
	// Define any Independent Input Varaibles here
	
	// References to RVP and DVP objects
	protected RVPs rvp;  // Reference to rvp object - object created in constructor
	protected DVPs dvp = new DVPs(this);  // Reference to dvp object
	protected UDPs udp = new UDPs(this);

	// Output object
	protected Output output = new Output(this);
	
	// Output values - define the public methods that return values
	// required for experimentation.


	// Constructor
	public SMRental(double t0time, double tftime, Seeds sd, boolean traceFlag) {
		this.traceFlag = traceFlag;
		this.startingTime = t0time;
		this.closingTime = tftime;
	
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// rgCounter and qCustLine objects created in Initalise Action
		
		// Initialise the simulation model
		initAOSimulModel(t0time,tftime);   

		     // Schedule the first arrivals and employee scheduling
		Initialise init = new Initialise(this);
		scheduleAction(init);  // Should always be first one scheduled.
		// Schedule other scheduled actions and acitvities here
	}

	/************  Implementation of Data Modules***********/	
	/*
	 * Testing preconditions
	 */
	protected void testPreconditions(Behaviour behObj)
	{
		reschedule (behObj);
		// Check preconditions of Conditional Activities

		// Check preconditions of Interruptions in Extended Activities
	}
	
	@Override
	public void eventOccured()
	{
		if(this.traceFlag)
		{
			System.out.println(
					String.format("Clock: {0}",
							getClock()));
			 showSBL();			
		}
	}

	// Standard Procedure to start Sequel Activities with no parameters
	protected void spStart(SequelActivity seqAct)
	{
		seqAct.startingEvent();
		scheduleActivity(seqAct);
	}	

}


