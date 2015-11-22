package smrental;

import java.sql.Time;

public class Customer {
	private CustomerType type;
	private double timeEnterSystem;
	private int numberOfAdditionalPassenager;

    public Customer(CustomerType type, double timeEnterSystem, int numberOfAdditionalPassenager) {
    	this.type = type;
    	this.timeEnterSystem = timeEnterSystem;
    	this.numberOfAdditionalPassenager = numberOfAdditionalPassenager;
    }

	public CustomerType getType() {
		return type;
	}
	public void setType(CustomerType type) {
		this.type = type;
	}
	public double getTimeEnterSystem() {
		return timeEnterSystem;
	}
	public void setTimeEnterSystem(double timeEnterSystem) {
		this.timeEnterSystem = timeEnterSystem;
	}
	public int getNumberOfAdditionalPassenager() {
		return numberOfAdditionalPassenager;
	}
	public void setNumberOfAdditionalPassenager(int numberOfAdditionalPassenager) {
		this.numberOfAdditionalPassenager = numberOfAdditionalPassenager;
	}
	
}
