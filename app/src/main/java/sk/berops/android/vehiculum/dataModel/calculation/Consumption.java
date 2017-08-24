package sk.berops.android.vehiculum.dataModel.calculation;

import com.github.mikephil.charting.data.PieEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.configuration.Preferences;
import sk.berops.android.vehiculum.dataModel.expense.Cost;
import sk.berops.android.vehiculum.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.vehiculum.engine.charts.PieChartDrawable;
import sk.berops.android.vehiculum.gui.MainActivity;
import sk.berops.android.vehiculum.gui.common.TextFormatter;

public class Consumption implements PieChartDrawable, Serializable {
	private Cost totalCostAllEntries;									//cumulated cost across all entries
	private TreeMap<ExpenseType, Cost> totalCostPerEntryType;			//cumulated cost within the entry types
	private Cost averageCostAllEntries;									//average cost of all entries per distance
	private TreeMap<ExpenseType, Cost> averageCostPerEntryType;			//average cost within the entry type per distance
	
	public Consumption() {
		totalCostAllEntries = new Cost();
		totalCostPerEntryType = new TreeMap<>();
		averageCostAllEntries = new Cost();
		averageCostPerEntryType = new TreeMap<>();
	}

	protected ArrayList<PieEntry> pieChartVals;
	protected ArrayList<Integer> pieChartColors;

	/**
	 * To generate and package the data for charts. This should be overwritten by each sub-class.
	 * @param depth identifies at which level we want to process data (Consumption or FuelConsumption level?)
	 */
	public void reloadChartData(int depth) {
		// As we're on depth level 0 and there's nothing above us, we don't really bother with the depth level
		pieChartVals = new ArrayList<>();
		pieChartColors = new ArrayList<>();

		for (sk.berops.android.vehiculum.dataModel.expense.Entry.ExpenseType t : sk.berops.android.vehiculum.dataModel.expense.Entry.ExpenseType.values()) {
			if (getTotalCostPerEntryType().get(t) == null) {
				continue;
			}
			double value = getTotalCostPerEntryType().get(t).getCost(Preferences.getInstance().getCurrency());
			pieChartVals.add(new PieEntry((float) value, t.getExpenseType(), t.getId()));
			pieChartColors.add(t.getColor());
		}
	}

	@Override
	public ArrayList<PieEntry> getPieChartVals() {
		if (pieChartVals == null) {
			reloadChartData(0);
		}

		return pieChartVals;
	}

	@Override
	public void setPieChartVals(ArrayList<PieEntry> pieChartVals) {
		this.pieChartVals = pieChartVals;
	}

	@Override
	public ArrayList<Integer> getPieChartColors() {
		if (pieChartColors == null) {
			reloadChartData(0);
		}

		return pieChartColors;
	}

	@Override
	public String getPieChartLabel(int depth) {
		double cost = getLabelValue(depth);
		return getLabelText() + ": " + TextFormatter.format(cost, "#####.##");
	}

	public String getLabelText() {
		return MainActivity.getContext().getString(R.string.generic_charts_total);
	}

	public double getLabelValue(int depth) {
		// We're at level 0, so return the total cost
		return getTotalCost();
	}

	public Cost getTotalCost() {
		return totalCostAllEntries;
	}

	public void setTotalCost(Cost totalCost) {
		this.totalCostAllEntries = totalCost;
	}

	public TreeMap<ExpenseType, Cost> getTotalCostPerEntryType() {
		return totalCostPerEntryType;
	}

	public void setTotalCostPerEntryType(
			TreeMap<ExpenseType, Cost> totalCostPerEntryType) {
		this.totalCostPerEntryType = totalCostPerEntryType;
	}

	public Cost getAverageCost() {
		return averageCostAllEntries;
	}

	public void setAverageCost(Cost averageCost) {
		this.averageCostAllEntries = averageCost;
	}

	public TreeMap<ExpenseType, Cost> getAverageCostPerEntryType() {
		return averageCostPerEntryType;
	}

	public void setAverageCostPerEntryType(
			TreeMap<ExpenseType, Cost> averageCostPerEntryType) {
		this.averageCostPerEntryType = averageCostPerEntryType;
	}
}
