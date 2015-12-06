package smrental;

import com.smrental.actions.ArrivalCounter;
import com.smrental.actions.ArrivalT1;
import com.smrental.actions.ArrivalT2;
import com.smrental.activities.Drive;
import com.smrental.activities.LoadVan;
import com.smrental.activities.Serving;
import com.smrental.activities.UnloadVan;
import com.smrental.entities.Counter;
import com.smrental.entities.Customer;
import com.smrental.entities.Van;
import com.smrental.entities.Van.VanStatus;
import com.smrental.procedures.DVPs;
import com.smrental.procedures.RVPs;
import com.smrental.procedures.Seeds;
import com.smrental.procedures.UDPs;
import com.smrental.utils.Parameters;
import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import static smrental.Constants.*;

public class SMRental extends AOSimulationModel
{
	private boolean traceFlag;
	public final double startingTime;
	public final double closingTime;
	public final Parameters params; // experiment parameters

	/*-------------Entity Data Structures-------------------*/
	public Counter rgCounter;
	@SuppressWarnings("unchecked")
	public final List<Customer>[] qCustomerLines = new LinkedList[4];
	@SuppressWarnings("unchecked")
	public final List<Integer>[] qVanLines = new LinkedList[5];

    public final Van[] rqVans;
	
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

        // Initialize customer lines
		for (int i =0; i < this.qCustomerLines.length; i++) {
			this.qCustomerLines[i] = new LinkedList<>();
		}

        // Initialize van lines
		for (int i =0; i < this.qVanLines.length; i++) {
			this.qVanLines[i] = new LinkedList<>();
		}

        // Initialize vans
        rqVans = new Van[params.getNumberOfVans()];
        for (int i=0; i< rqVans.length; i++) {
        	rqVans[i] = new Van(i, params.getTypeOfVan());
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
			check = true;
		}

		if(Serving.precondition(this)) {
			Serving serving = new Serving(this);
			serving.startingEvent();
			scheduleActivity(serving);
            check = true;
		}

        return check;
	}

	@Override public boolean implicitStopCondition() {
		boolean result = false;
		if (this.getClock() >= this.closingTime &&
				this.rgCounter.getN() == 0) {
			result = true;
		}
		return result;
	}

	@Override public void eventOccured() {
		if(this.traceFlag) {
			StringJoiner joiner = new StringJoiner(" %s\n");
			joiner.add("Clock:");
			joiner.add("Vanline[T1]:");
			joiner.add("CustomerLine[T1]:");
			joiner.add("Vanline[T2]:");
			joiner.add("CustomerLine[T2]:");
			joiner.add("Vanline[COUNTER_PICKUP]:");
			joiner.add("Vanline[COUNTER_DROPOFF]:");
			joiner.add("CustomerAtCounter:");
			joiner.add("CustomerLine[WAIT_FOR_SERVING]:");
			joiner.add("CustomerLine[WAIT_FOR_PICKUP]:");
			joiner.add("Vanline[DROP_OFF]:%s");
			System.out.println(
				String.format(joiner.toString()
					,getClock()
					, printVanLine(this.qVanLines[VANLINE_T1])
					, "(n="+this.qCustomerLines[CUSTOMERLINE_T1].size()+")"+this.qCustomerLines[CUSTOMERLINE_T1]
					, printVanLine(this.qVanLines[VANLINE_T2])
					, "(n="+this.qCustomerLines[CUSTOMERLINE_T2].size()+")"+this.qCustomerLines[CUSTOMERLINE_T2]
					, printVanLine(this.qVanLines[VANLINE_COUNTER_PICKUP])
					, printVanLine(this.qVanLines[VANLINE_COUNTER_DROPOFF])
					, this.rgCounter.getN()
					, "(n="+this.qCustomerLines[CUSTOMERLINE_WAIT_FOR_SERVING].size()+")" + this.qCustomerLines[CUSTOMERLINE_WAIT_FOR_SERVING]
					, "(n="+this.qCustomerLines[CUSTOMERLINE_WAIT_FOR_PICKUP].size()+")" + this.qCustomerLines[CUSTOMERLINE_WAIT_FOR_PICKUP]
					, printVanLine(this.qVanLines[VANLINE_COUNTER_DROPOFF])));

            printRunningVanStatus();
			System.out.println("Current Cost: "+ udp.calculateCosts());
			double currentServiceLevel;

			if (output.numOfServed == 0) {
				currentServiceLevel = 0.0;
			} else {
                currentServiceLevel = (double) output.numOfSatisfiedCustomer / output.numOfServed;
			}
			System.out.println("Current service level: "+ currentServiceLevel);
            showSBL();
		}
	}

    private void printRunningVanStatus() {
        List<Van> counterToT1 = new LinkedList<>();
        List<Van> counterToDropOff = new LinkedList<>();
        List<Van> dropOffToT1 = new LinkedList<>();
        List<Van> t1ToT2 = new LinkedList<>();
        List<Van> t2ToCounter = new LinkedList<>();

		for (Van rqVan : this.rqVans) {
			if (rqVan.status == VanStatus.DRIVING_COUNTER_T1) {
				counterToT1.add(rqVan);
			}
			if (rqVan.status == VanStatus.DRIVING_COUNTER_DROP_OFF) {
				counterToDropOff.add(rqVan);
			}
			if (rqVan.status == VanStatus.DRIVING_DROP_OFF_T1) {
				dropOffToT1.add(rqVan);
			}
			if (rqVan.status == VanStatus.DRIVING_T1_T2) {
				t1ToT2.add(rqVan);
			}
			if (rqVan.status == VanStatus.DRIVING_T2_COUNTER) {
				t2ToCounter.add(rqVan);
			}
		}
        StringJoiner joiner = new StringJoiner(" %s\n");
        joiner.add("Counter -> T1:");
        joiner.add("Counter -> Drop-Off:");
        joiner.add("Drop-Off -> T1:");
        joiner.add("T1 -> T2:");
        joiner.add("T2 -> Counter:%s");
		System.out.println("=====================Running Van Status=====================");
        System.out.println(
                String.format(
                joiner.toString(),
                        counterToT1.toString(),
                        counterToDropOff.toString(),
                        dropOffToT1.toString(),
                        t1ToT2.toString(),
                        t2ToCounter.toString()
                ));
    }

    private String  printVanLine(List<Integer> vanLine) {
        List<Van> theRealVanLine = new LinkedList<>();
        for (int vanId : vanLine) {
            theRealVanLine.add(this.rqVans[vanId]);
        }
        return "(n="+theRealVanLine.size()+")" + theRealVanLine.toString();
    }

}