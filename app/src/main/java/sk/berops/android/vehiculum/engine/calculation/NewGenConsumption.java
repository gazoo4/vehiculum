package sk.berops.android.vehiculum.engine.calculation;

import sk.berops.android.vehiculum.dataModel.expense.Cost;

/**
 * @author Bernard Halas
 * @date 7/25/17
 */

public class NewGenConsumption {
	private Cost totalCost;
	private Cost averageCost;

	private Cost totalTypeCost;
	private Cost averageTypeCost;

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
}
