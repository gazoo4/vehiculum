package sk.berops.android.vehiculum.engine.calculation;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import sk.berops.android.vehiculum.dataModel.expense.Cost;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenConsumption {
	/**
	 * Total cost of the all the entries including the current one
	 */
	private Cost totalCost;
	/**
	 * Average cost (per distance unit) of all the entries including the current one
	 */
	private Cost averageCost;
	/**
	 * Total count of the entries by now (including the current one)
	 */
	private int count;

	/**
	 * Total cost of all the entries of the same type as current one (including the current one)
	 */
	private Cost totalTypeCost;
	/**
	 * Average cost (per distance unit) of all the entries of the same type as current one (including the current one)
	 */
	private Cost averageTypeCost;
	/**
	 * Total count of the entries of the same type as current one by now (including the current one)
	 */
	private int typeCount;

	protected ArrayList<PieEntry> pieChartVals;
	protected ArrayList<Integer> pieChartColors;

	public void reloadChartData

	public Cost getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Cost totalCost) {
		this.totalCost = totalCost;
	}

	public Cost getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(Cost averageCost) {
		this.averageCost = averageCost;
	}

	public Cost getTotalTypeCost() {
		return totalTypeCost;
	}

	public void setTotalTypeCost(Cost totalTypeCost) {
		this.totalTypeCost = totalTypeCost;
	}

	public Cost getAverageTypeCost() {
		return averageTypeCost;
	}

	public void setAverageTypeCost(Cost averageTypeCost) {
		this.averageTypeCost = averageTypeCost;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


	public int getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}
}
