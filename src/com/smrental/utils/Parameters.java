package com.smrental.utils;

public class Parameters {
	private int typeOfVan;
	private int numberOfAgents;
	private int numberOfVans;
	private boolean customerIncrease;

	private Parameters(Builder builder) {
		this.typeOfVan = builder.typeOfVan;
		this.numberOfAgents = builder.numberOfAgents;
		this.numberOfVans = builder.numberOfVans;
		this.customerIncrease = builder.customerIncrease;
	}
	
    public int getTypeOfVan() {
		return typeOfVan;
	}

	public int getNumberOfAgents() {
		return numberOfAgents;
	}

	public int getNumberOfVans() {
		return numberOfVans;
	}

	public boolean isCustomerIncrease() {
		return customerIncrease;
	}

	@Override
    public String toString() {
        return String
                .format("[typeOfVan = %s, numberOfAgents: %s, numberOfVans: %s, customerIncrease: %s]",
                        typeOfVan, numberOfAgents, numberOfVans, customerIncrease);
    }

	public static class Builder {
		private int typeOfVan;
		private int numberOfAgents;
		private int numberOfVans;
		private boolean customerIncrease;

		public Builder typeOfVan(int typeOfVan) {
			this.typeOfVan = typeOfVan;
			return this;
		}

		public Builder numberOfAgents(int numberOfAgents) {
			this.numberOfAgents = numberOfAgents;
			return this;
		}

		public Builder numberOfVans(int numberOfVans) {
			this.numberOfVans = numberOfVans;
			return this;
		}

		public Builder customerIncrease(boolean customerIncrease) {
			this.customerIncrease = customerIncrease;
			return this;
		}

		public Parameters build() {
			return new Parameters(this);
		}
	}
}
