package sk.berops.android.fueller.dataModel.calculation;

import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.InsuranceEntry;

public class InsuranceConsumption extends Consumption {
	private double totalInsuranceCost;
	private double averageInsuranceCost;
	
	private TreeMap<InsuranceEntry.Type, Double> totalCostPerInsuranceType;
	private TreeMap<InsuranceEntry.Type, Double> averageCostPerInsuranceType;
	
	public InsuranceConsumption() {
		totalInsuranceCost = 0.0;
		averageInsuranceCost = 0.0;
		
		totalCostPerInsuranceType = new TreeMap<InsuranceEntry.Type, Double>();
		averageCostPerInsuranceType = new TreeMap<InsuranceEntry.Type, Double>();
	}

	public double getTotalInsuranceCost() {
		return totalInsuranceCost;
	}

	public void setTotalInsuranceCost(double totalInsuranceCost) {
		this.totalInsuranceCost = totalInsuranceCost;
	}

	public double getAverageInsuranceCost() {
		return averageInsuranceCost;
	}

	public void setAverageInsuranceCost(double averageInsuranceCost) {
		this.averageInsuranceCost = averageInsuranceCost;
	}

	public TreeMap<InsuranceEntry.Type, Double> getTotalCostPerInsuranceType() {
		return totalCostPerInsuranceType;
	}

	public void setTotalCostPerInsuranceType(TreeMap<InsuranceEntry.Type, Double> totalCostPerInsuranceType) {
		this.totalCostPerInsuranceType = totalCostPerInsuranceType;
	}

	public TreeMap<InsuranceEntry.Type, Double> getAverageCostPerInsuranceType() {
		return averageCostPerInsuranceType;
	}

	public void setAverageCostPerInsuranceType(TreeMap<InsuranceEntry.Type, Double> averageCostPerInsuranceType) {
		this.averageCostPerInsuranceType = averageCostPerInsuranceType;
	}
}
