package smrental;

import simulationModelling.ScheduledAction;

public class ArrivalT1 extends ScheduledAction{
	private static final CustomerType T1_CUSTOMER_TYPE = CustomerType.CHECK_IN;
	private SMRental model;
	
	public ArrivalT1(SMRental model) {
		this.model = model;
	}
	
	@Override
	protected double timeSequence() {
		// TODO wait for rvp.duC implememntation
		return 0;
	}

	@Override
	protected void actionEvent() {
		int numberOfAdditionalPassenager = 0; // TODO waiting for model.rvp.additionalPassenger
 		Customer icCustomer = new Customer(T1_CUSTOMER_TYPE, this.model.getClock(), numberOfAdditionalPassenager);
 		//TODO add customer to the T1 customer line
	}

}
