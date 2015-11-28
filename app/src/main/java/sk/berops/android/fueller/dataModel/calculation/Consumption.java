package sk.berops.android.fueller.dataModel.calculation;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.engine.charts.IntoPieChartExtractable;

public class Consumption implements IntoPieChartExtractable {
	private double totalCostAllEntries;										//cumulated cost across all entries
	private TreeMap<ExpenseType, Double> totalCostPerEntryType;				//cumulated cost within the entries
	private double averageCostAllEntries;									//average cost of all entries per distance
	private TreeMap<ExpenseType, Double> averageCostPerEntryType;			//average cost within the entry type per distance
	private ExpenseType lastEntryType;
	
	public Consumption() {
		totalCostAllEntries = 0.0;
		totalCostPerEntryType = new TreeMap<ExpenseType, Double>();
		averageCostAllEntries = 0.0;
		averageCostPerEntryType = new TreeMap<ExpenseType, Double>();
		lastEntryType = ExpenseType.FUEL;
	}

	protected ArrayList<String> pieChartLegend;
	protected ArrayList<Entry> pieChartVals;
	protected ArrayList<Integer> pieChartColors;

	public void reloadChartData() {
		pieChartLegend = new ArrayList<String>();
		pieChartVals = new ArrayList<Entry>();
		pieChartColors = new ArrayList<Integer>();

		int i = 0;
		for (sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType t : sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType.values()) {
			if (getTotalCostPerEntryType().get(t) == null) {
				continue;
			}
			double value = getTotalCostPerEntryType().get(t);
			pieChartLegend.add(t.getExpenseType());
			pieChartVals.add(new Entry((float) value, i++, t.getId()));
			pieChartColors.add(t.getColor());
			System.out.println("Type: " + t.getExpenseType() +" with ID: "+ t.getId());
		}
	}

	@Override
	public ArrayList<String> getPieChartLegend() {
		if (pieChartLegend == null) {
			reloadChartData();
		}

		return pieChartLegend;
	}

	@Override
	public ArrayList<Entry> getPieChartVals() {
		if (pieChartVals == null) {
			reloadChartData();
		}

		return pieChartVals;
	}

	@Override
	public ArrayList<Integer> getPieChartColors() {
		if (pieChartColors == null) {
			reloadChartData();
		}

		return pieChartColors;
	}

	public double getTotalCost() {
		return totalCostAllEntries;
	}

	public void setTotalCost(double totalCost) {
		this.totalCostAllEntries = totalCost;
	}

	public TreeMap<ExpenseType, Double> getTotalCostPerEntryType() {
		return totalCostPerEntryType;
	}

	public void setTotalCostPerEntryType(
			TreeMap<ExpenseType, Double> totalCostPerEntryType) {
		this.totalCostPerEntryType = totalCostPerEntryType;
	}

	public double getAverageCost() {
		return averageCostAllEntries;
	}

	public void setAverageCost(double averageCost) {
		this.averageCostAllEntries = averageCost;
	}

	public TreeMap<ExpenseType, Double> getAverageCostPerEntryType() {
		return averageCostPerEntryType;
	}

	public void setAverageCostPerEntryType(
			TreeMap<ExpenseType, Double> averageCostPerEntryType) {
		this.averageCostPerEntryType = averageCostPerEntryType;
	}

	public ExpenseType getLastEntryType() {
		return lastEntryType;
	}

	public void setLastEntryType(ExpenseType lastEntryType) {
		this.lastEntryType = lastEntryType;
	}
}
