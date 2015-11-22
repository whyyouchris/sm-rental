package smrental;

import utils.FixedSizeList;

public class Van {
	public final int capacity;
	private FixedSizeList<Customer> onBoardCustomers;
	private VanStatus status;
	

	public Van(int capacity) {
		this.capacity = capacity;
		this.onBoardCustomers = new FixedSizeList<>(capacity);
	}

	public void boardCustomer(Customer icCustomer) {
		this.onBoardCustomers.add(icCustomer);
	}

	public Customer unBoardCustomer() {
		return this.onBoardCustomers.remove(0);
	}

	public VanStatus getStatus() {
		return status;
	}

	public void setStatus(VanStatus status) {
		this.status = status;
	}
}
