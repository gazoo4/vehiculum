package sk.berops.android.fueller.dataModel.calculation;

import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.ServiceEntry;

public class ServiceConsumption extends Consumption {
	private double totalServiceCost;
	private double averageServiceCost;
	
	private TreeMap<ServiceEntry.Type, Double> totalCostPerServiceType;
	private TreeMap<ServiceEntry.Type, Double> averageCostPerServiceType;
	
	public ServiceConsumption() {
		totalServiceCost = 0.0;
		averageServiceCost = 0.0;
		
		totalCostPerServiceType = new TreeMap<ServiceEntry.Type, Double>();
		averageCostPerServiceType = new TreeMap<ServiceEntry.Type, Double>();
	}

	public double getTotalServiceCost() {
		return totalServiceCost;
	}

	public void setTotalServiceCost(double totalServiceCost) {
		this.totalServiceCost = totalServiceCost;
	}

	public double getAverageServiceCost() {
		return averageServiceCost;
	}

	public void setAverageServiceCost(double averageServiceCost) {
		this.averageServiceCost = averageServiceCost;
	}

	public TreeMap<ServiceEntry.Type, Double> getTotalCostPerServiceType() {
		return totalCostPerServiceType;
	}

	public void setTotalCostPerServiceType(TreeMap<ServiceEntry.Type, Double> totalCostPerServiceType) {
		this.totalCostPerServiceType = totalCostPerServiceType;
	}

	public TreeMap<ServiceEntry.Type, Double> getAverageCostPerServiceType() {
		return averageCostPerServiceType;
	}

	public void setAverageCostPerServiceType(TreeMap<ServiceEntry.Type, Double> averageCostPerServiceType) {
		this.averageCostPerServiceType = averageCostPerServiceType;
	}
}