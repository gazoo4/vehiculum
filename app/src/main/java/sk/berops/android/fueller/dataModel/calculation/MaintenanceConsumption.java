package sk.berops.android.fueller.dataModel.calculation;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.MaintenanceEntry;

public class MaintenanceConsumption extends Consumption {

	private double totalMaintenanceCost;
	private double averageMaintenanceCost;
	private double totalLaborCost;
	private double averageLaborCost;
	private double totalPartsCost;
	private double averagePartsCost;
	
	private TreeMap<MaintenanceEntry.Type, Double> totalCostPerMaintenanceType;
	private TreeMap<MaintenanceEntry.Type, Double> averageCostPerMaintenanceType;
	
	public MaintenanceConsumption() {
		totalMaintenanceCost = 0.0;
		averageMaintenanceCost = 0.0;
		totalLaborCost = 0.0;
		averageLaborCost = 0.0;
		totalPartsCost = 0.0;
		averagePartsCost = 0.0;
		totalCostPerMaintenanceType = new TreeMap<MaintenanceEntry.Type, Double>();
		averageCostPerMaintenanceType = new TreeMap<MaintenanceEntry.Type, Double>();
	}

	@Override
	public void reloadChartData(int data) {
		// We're on level 1. For anything smaller than level 1, we should call our parent class for the data generation instead
		if (data < 1) {
			super.reloadChartData(data);
			return;
		}
		pieChartLegend = new ArrayList<>();
		pieChartVals = new ArrayList<>();
		pieChartColors = new ArrayList<>();

		for (MaintenanceEntry.Type t : MaintenanceEntry.Type.values()) {
			if (getTotalCostPerMaintenanceType().get(t) == null) {
				continue;
			}
			double value = getTotalCostPerMaintenanceType().get(t);
			pieChartLegend.add(t.getType());
			pieChartVals.add(new Entry((float) value, t.getId()));
			pieChartColors.add(t.getColor());
		}
	}
	
	public double getTotalMaintenanceCost() {
		return totalMaintenanceCost;
	}
	public void setTotalMaintenanceCost(double totalMaintenanceCost) {
		this.totalMaintenanceCost = totalMaintenanceCost;
	}
	public double getAverageMaintenanceCost() {
		return averageMaintenanceCost;
	}
	public void setAverageMaintenanceCost(double averageMaintenanceCost) {
		this.averageMaintenanceCost = averageMaintenanceCost;
	}
	public double getTotalLaborCost() {
		return totalLaborCost;
	}
	public void setTotalLaborCost(double totalLaborCost) {
		this.totalLaborCost = totalLaborCost;
	}
	public double getAverageLaborCost() {
		return averageLaborCost;
	}
	public void setAverageLaborCost(double averageLaborCost) {
		this.averageLaborCost = averageLaborCost;
	}
	public double getTotalPartsCost() {
		return totalPartsCost;
	}
	public void setTotalPartsCost(double totalPartsCost) {
		this.totalPartsCost = totalPartsCost;
	}
	public double getAveragePartsCost() {
		return averagePartsCost;
	}
	public void setAveragePartsCost(double averagePartsCost) {
		this.averagePartsCost = averagePartsCost;
	}
	public TreeMap<MaintenanceEntry.Type, Double> getTotalCostPerMaintenanceType() {
		return totalCostPerMaintenanceType;
	}
	public void setTotalCostPerMaintenanceType(TreeMap<MaintenanceEntry.Type, Double> totalCostPerMaintenanceType) {
		this.totalCostPerMaintenanceType = totalCostPerMaintenanceType;
	}
	public TreeMap<MaintenanceEntry.Type, Double> getAverageCostPerMaintenanceType() {
		return averageCostPerMaintenanceType;
	}
	public void setAverageCostPerMaintenanceType(TreeMap<MaintenanceEntry.Type, Double> averageCostPerMaintenanceType) {
		this.averageCostPerMaintenanceType = averageCostPerMaintenanceType;
	}
}