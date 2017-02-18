package sk.berops.android.vehiculum.dataModel.calculation;

/**
 * Class for carrying consumption information calculated from TyreChangeEntries
 *
 * @author  Bernard Halas
 * @date    1/12/17
 */

public class TyreConsumption extends Consumption {
	private double totalTyreCost;
	private double averageTyreCost;
	private double totalExtraMaterialCost;
	private double averageExtraMaterialCost;
	private double totalLaborCost;
	private double averageLaborCost;

	public TyreConsumption() {
		setTotalTyreCost(0.0);
		setAverageTyreCost(0.0);
		setTotalExtraMaterialCost(0.0);
		setAverageExtraMaterialCost(0.0);
		setTotalLaborCost(0.0);
		setAverageLaborCost(0.0);
	}

	public double getTotalTyreCost() {
		return totalTyreCost;
	}

	public void setTotalTyreCost(double totalTyreCost) {
		this.totalTyreCost = totalTyreCost;
	}

	public double getAverageTyreCost() {
		return averageTyreCost;
	}

	public void setAverageTyreCost(double averageTyreCost) {
		this.averageTyreCost = averageTyreCost;
	}

	public double getTotalExtraMaterialCost() {
		return totalExtraMaterialCost;
	}

	public void setTotalExtraMaterialCost(double totalExtraMaterialCost) {
		this.totalExtraMaterialCost = totalExtraMaterialCost;
	}

	public double getAverageExtraMaterialCost() {
		return averageExtraMaterialCost;
	}

	public void setAverageExtraMaterialCost(double averageExtraMaterialCost) {
		this.averageExtraMaterialCost = averageExtraMaterialCost;
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
}
