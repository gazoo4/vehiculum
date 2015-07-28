package sk.berops.android.fueller.dataModel.calculation;

import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.ServiceEntry;

public class BurreaucraticConsumption extends Consumption {
	private double totalBurreaucraticCost;
	private double averageBurreaucraticCost;
	
	public BurreaucraticConsumption() {
		totalBurreaucraticCost = 0.0;
		averageBurreaucraticCost = 0.0;
	}

	public double getTotalBurreaucraticCost() {
		return totalBurreaucraticCost;
	}

	public void setTotalBurreaucraticCost(double totalBurreaucraticCost) {
		this.totalBurreaucraticCost = totalBurreaucraticCost;
	}

	public double getAverageBurreaucraticCost() {
		return averageBurreaucraticCost;
	}

	public void setAverageBurreaucraticCost(double averageBurreaucraticCost) {
		this.averageBurreaucraticCost = averageBurreaucraticCost;
	}
}
