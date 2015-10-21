package sk.berops.android.fueller.dataModel.calculation;

import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.TollEntry;

public class TollConsumption extends Consumption {
	private double totalTollCost;
	private double averageTollCost;
	
	private TreeMap<TollEntry.Type, Double> totalCostPerTollType;
	private TreeMap<TollEntry.Type, Double> averageCostPerTollType;
	
	public TollConsumption() {
		totalTollCost = 0.0;
		averageTollCost = 0.0;
		
		totalCostPerTollType = new TreeMap<TollEntry.Type, Double>();
		averageCostPerTollType = new TreeMap<TollEntry.Type, Double>();
	}

	public double getTotalTollCost() {
		return totalTollCost;
	}

	public void setTotalTollCost(double totalTollCost) {
		this.totalTollCost = totalTollCost;
	}

	public double getAverageTollCost() {
		return averageTollCost;
	}

	public void setAverageTollCost(double averageTollCost) {
		this.averageTollCost = averageTollCost;
	}

	public TreeMap<TollEntry.Type, Double> getTotalCostPerTollType() {
		return totalCostPerTollType;
	}

	public void setTotalCostPerTollType(TreeMap<TollEntry.Type, Double> totalCostPerTollType) {
		this.totalCostPerTollType = totalCostPerTollType;
	}

	public TreeMap<TollEntry.Type, Double> getAverageCostPerTollType() {
		return averageCostPerTollType;
	}

	public void setAverageCostPerTollType(TreeMap<TollEntry.Type, Double> averageCostPerTollType) {
		this.averageCostPerTollType = averageCostPerTollType;
	}
}
