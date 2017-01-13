package sk.berops.android.fueller.dataModel.calculation;

import com.github.mikephil.charting.data.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.engine.charts.IntoPieChartExtractable;

public class Consumption implements IntoPieChartExtractable, Serializable {
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

	/**
	 * To generate and package the data for charts. This should be overwritten by each sub-class.
	 * @param depth identifies at which level we want to process data (Consumption or FuelConsumption level?)
	 */
	public void reloadChartData(int depth) {
		// As we're on depth level 0 and there's nothing above us, we don't really bother with the depth level
		pieChartLegend = new ArrayList<>();
		pieChartVals = new ArrayList<>();
		pieChartColors = new ArrayList<>();

		int i = 0;
		for (sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType t : sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType.values()) {
			if (getTotalCostPerEntryType().get(t) == null) {
				continue;
			}
			double value = getTotalCostPerEntryType().get(t);
			pieChartLegend.add(t.getExpenseType());
			pieChartVals.add(new Entry((float) value, i++, t.getId()));
			pieChartColors.add(t.getColor());
		}
	}

	@Override
	public ArrayList<String> getPieChartLegend() {
		if (pieChartLegend == null) {
			reloadChartData(0);
		}

		return pieChartLegend;
	}

	@Override
	public ArrayList<Entry> getPieChartVals() {
		if (pieChartVals == null) {
			reloadChartData(0);
		}

		return pieChartVals;
	}

	@Override
	public void setPieChartVals(ArrayList<Entry> pieChartVals) {
		this.pieChartVals = pieChartVals;
	}

	@Override
	public ArrayList<Integer> getPieChartColors() {
		if (pieChartColors == null) {
			reloadChartData(0);
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
