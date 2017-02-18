package sk.berops.android.vehiculum.dataModel.calculation;

public class BureaucraticConsumption extends Consumption {
	private double totalBureaucraticCost;
	private double averageBureaucraticCost;
	
	public BureaucraticConsumption() {
		totalBureaucraticCost = 0.0;
		averageBureaucraticCost = 0.0;
	}

	public double getTotalBureaucraticCost() {
		return totalBureaucraticCost;
	}

	public void setTotalBureaucraticCost(double totalBureaucraticCost) {
		this.totalBureaucraticCost = totalBureaucraticCost;
	}

	public double getAverageBureaucraticCost() {
		return averageBureaucraticCost;
	}

	public void setAverageBureaucraticCost(double averageBureaucraticCost) {
		this.averageBureaucraticCost = averageBureaucraticCost;
	}
}
