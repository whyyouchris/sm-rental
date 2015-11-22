package smrental;

import java.util.LinkedList;
import java.util.List;

import com.smrental.models.Counter;
import com.smrental.models.Customer;
import com.smrental.models.Van;
import com.smrental.procedures.DVPs;
import com.smrental.procedures.RVPs;
import com.smrental.procedures.Seeds;
import com.smrental.procedures.UDPs;
import com.smrental.utils.Parameters;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;
import simulationModelling.SequelActivity;

//
// The Simulation model Class
public class SMRental extends AOSimulationModel
{
	private boolean traceFlag;
	public final double startingTime;
	public final double closingTime;
	public final Parameters params; // experiment parameters

	/*-------------Entity Data Structures-------------------*/
	/* Group and Queue Entities */
	// Define the reference variables to the various 
	// entities with scope Set and Unary
	// Objects can be created here or in the Initialise Action
	public final Counter rgCounter = new Counter();
	public final List<Customer>[] qCustomerLines = new LinkedList[4];
	public final List<Van>[] qVanLines = new LinkedList[4];
	
	/* Input Variables */
	// Define any Independent Input Varaibles here
	
	// References to RVP and DVP objects
	public final RVPs rvp;  // Reference to rvp object - object created in constructor
	public final DVPs dvp = new DVPs(this);  // Reference to dvp object
	public final UDPs udp = new UDPs(this);

	// Output object
	protected Output output = new Output(this);
	
	// Output values - define the public methods that return values
	// required for experimentation.


	// Constructor
	public SMRental(double t0time, double tftime, Seeds sd, Parameters params, boolean traceFlag) {
		this.traceFlag = traceFlag;
		this.startingTime = t0time;
		this.closingTime = tftime;
		
		this.params = params;
	
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


