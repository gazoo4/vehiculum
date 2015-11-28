package sk.berops.android.fueller.dataModel.calculation;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
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

	@Override
	public void reloadChartData(int data) {
		// We're on level 1. For anything smaller than level 1, we should call our parent class for the data generation instead
		if (data < 1) {
			super.reloadChartData(data);
			return;
		}
		pieChartLegend = new ArrayList<String>();
		pieChartVals = new ArrayList<Entry>();
		pieChartColors = new ArrayList<Integer>();

		for (ServiceEntry.Type t : ServiceEntry.Type.values()) {
			if (getTotalCostPerServiceType().get(t) == null) {
				continue;
			}
			double value = getTotalCostPerServiceType().get(t);
			pieChartLegend.add(t.getType());
			pieChartVals.add(new Entry((float) value, t.getId()));
			pieChartColors.add(t.getColor());
		}
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