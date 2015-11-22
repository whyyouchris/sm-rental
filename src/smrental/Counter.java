package smrental;

import java.util.HashSet;

public class Counter {
	protected int numberOfAgent;
	private HashSet<Customer> group = new HashSet<>();

	/**
	 * @param Customer - icCustomer
	 * @return void
	 * Add an customer to the counter group
	 */
	public void insertGrp(Customer icCustomer) { 
		this.group.add(icCustomer);
	}

	public boolean removeGrp(Customer icCustomer) {
		return this.group.remove(icCustomer);
	}

	public int getN() {
		return this.group.size();
	}
	
	public int getNumberOfAgent() {
		return this.numberOfAgent;
	}

}
