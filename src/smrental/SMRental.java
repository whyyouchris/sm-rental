package smrental;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import com.smrental.actions.ArrivalCounter;
import com.smrental.actions.ArrivalT1;
import com.smrental.actions.ArrivalT2;
import com.smrental.activities.Drive;
import com.smrental.activities.LoadVan;
import com.smrental.activities.Serving;
import com.smrental.activities.UnloadVan;
import com.smrental.models.Counter;
import com.smrental.models.Customer;
import com.smrental.models.CustomerLineID;
import com.smrental.models.Location;
import com.smrental.models.Van;
import com.smrental.procedures.DVPs;
import com.smrental.procedures.RVPs;
import com.smrental.procedures.Seeds;
import com.smrental.procedures.UDPs;
import com.smrental.utils.Parameters;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;

public class SMRental extends AOSimulationModel
{
	private boolean traceFlag;
	public final double startingTime;
	public final double closingTime;
	public final Parameters params; // experiment parameters

	/*-------------Entity Data Structures-------------------*/
	public final Counter rgCounter;
	@SuppressWarnings("unchecked")
	public final List<Customer>[] qCustomerLines = new LinkedList[4];
	@SuppressWarnings("unchecked")
	public final List<Van>[] qVanLines = new LinkedList[4];
	
	// References to RVP and DVP objects
	public final RVPs rvp;
	public final DVPs dvp = new DVPs(this);
	public final UDPs udp = new UDPs(this);

	// Output object
	public final Output output = new Output();
	
	public SMRental(double t0time, double tftime, Seeds sd, Parameters params, boolean traceFlag) {
		this.traceFlag = traceFlag;
		this.startingTime = t0time;
		this.closingTime = tftime;

		this.params = params;

		rvp = new RVPs(this,sd);
		this.rgCounter = new Counter(this.params.getNumberOfAgents());

		for (int i =0; i < this.qCustomerLines.length; i++) {
			this.qCustomerLines[i] = new LinkedList<>();
		}

		for (int i =0; i < this.qVanLines.length; i++) {
			this.qVanLines[i] = new LinkedList<>();
		}

		// Initialise the simulation model
		initAOSimulModel(t0time,tftime);   

		// Schedule the first arrivals and employee scheduling
		Initialise init = new Initialise(this);
		scheduleAction(init);  // Should always be first one scheduled.
		ArrivalT1 arrivalT1 = new ArrivalT1(this);
		scheduleAction(arrivalT1);
		ArrivalT2 arrivalT2 = new ArrivalT2(this);
		scheduleAction(arrivalT2);
		ArrivalCounter arrivalCounter = new ArrivalCounter(this);
		scheduleAction(arrivalCounter);
	}

	/************  Implementation of Data Modules***********/	
	/*
	 * Testing preconditions
	 */
	@Override protected void testPreconditions(Behaviour behObj) {
		reschedule (behObj);
        while (true) {
            if (!preconditionChecker()) {
                break;
            }
        }
	}

	private boolean preconditionChecker() {
		boolean check = false;
		if(LoadVan.precondition(this)) {
			LoadVan loadVan = new LoadVan(this);
			loadVan.startingEvent();
			scheduleActivity(loadVan);
            check = true;
		}

		if(Drive.precondition(this)) {
			Drive drive = new Drive(this);
			drive.startingEvent();
			scheduleActivity(drive);
            check = true;
		}

		if(UnloadVan.precondition(this)) {
			UnloadVan unloadVan = new UnloadVan(this);
			unloadVan.startingEvent();
			scheduleActivity(unloadVan);
		}

		if(Serving.precondition(this)) {
			Serving serving = new Serving(this);
			serving.startingEvent();
			scheduleActivity(serving);
            check = true;
		}

        return check;
	}

	public boolean implicitStopCondition() {
		boolean result = false;
		if (this.getClock() >= this.closingTime &&
				this.rgCounter.getN() == 0) {
			result = true;
		}
		return result;
	}

	@Override public void eventOccured()
	{
		if(this.traceFlag)
		{
			StringJoiner joiner = new StringJoiner(" %s\n");
			joiner.add("Clock:");
			joiner.add("Vanline[T1]:");
			joiner.add("CustomerLine[T1]:");
			joiner.add("Vanline[T2]:");
			joiner.add("CustomerLine[T2]:");
			joiner.add("Vanline[COUNTER]:");
			joiner.add("CustomerAtCounter:");
			joiner.add("CustomerLine[WAIT_FOR_SERVING]:");
			joiner.add("CustomerLine[WAIT_FOR_PICKUP]:");
			joiner.add("Vanline[DROP_OFF]:%s");
			System.out.println(
				String.format(joiner.toString()
					,getClock()
					, this.qVanLines[Location.T1.ordinal()]
					, this.qCustomerLines[CustomerLineID.T1.ordinal()]
					, this.qVanLines[Location.T2.ordinal()]
					, this.qCustomerLines[CustomerLineID.T2.ordinal()]
					, this.qVanLines[Location.COUNTER.ordinal()]
					, this.rgCounter.getN()
					, this.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_SERVICING.ordinal()]
					, this.qCustomerLines[CustomerLineID.COUNTER_WAIT_FOR_PICKUP.ordinal()]
					, this.qVanLines[Location.DROP_OFF.ordinal()]));
			 showSBL();			
		}
	}
}


