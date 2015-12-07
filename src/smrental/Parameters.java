package smrental;

public class Parameters {
    public int typeOfVan;
    public int numberOfAgents;
    public int numberOfVans;
    public boolean customerIncrease;

    @Override public String toString() {
        return String
                .format("[typeOfVan = %s, numberOfAgents: %s, numberOfVans: %s, customerIncrease: %s]",
                        typeOfVan, numberOfAgents, numberOfVans, customerIncrease);
    }
}
