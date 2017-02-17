package sk.berops.android.caramel.engine.calculation;

import java.util.LinkedList;

import sk.berops.android.caramel.dataModel.calculation.TyreConsumption;
import sk.berops.android.caramel.dataModel.expense.TyreChangeEntry;
import sk.berops.android.caramel.gui.MainActivity;

/**
 * Class responsible for calculation of the stats of the Tyre changes
 * @author Bernard Halas
 * @date 1/12/17
 */

public class TyreCalculator {
	public static void calculate(LinkedList<TyreChangeEntry> entries) {
		double totalCost = 0.0;
		double averageCost = 0.0;
		double totalTyreCost = 0.0;
		double averageTyreCost = 0.0;
		double totalExtraMaterialCost = 0.0;
		double averageExtraMaterialCost = 0.0;
		double totalLaborCost = 0.0;
		double averageLaborCost = 0.0;

		double mileageDriven = 0;

		TyreConsumption consumption;

		for (TyreChangeEntry e: entries) {
			consumption = (TyreConsumption) e.getConsumption();
			mileageDriven = e.getMileageSI() - MainActivity.garage.getActiveCar().getInitialMileageSI();

			totalTyreCost = e.getTyresCostSI();
			consumption.setTotalTyreCost(totalTyreCost);

			totalExtraMaterialCost = e.getExtraMaterialCostSI();
			consumption.setTotalExtraMaterialCost(totalExtraMaterialCost);

			totalLaborCost = e.getLaborCostSI();
			consumption.setTotalLaborCost(totalLaborCost);

			if (mileageDriven > 0.0) {
				averageTyreCost = totalTyreCost / mileageDriven * 100;
				consumption.setAverageTyreCost(averageTyreCost);

				averageExtraMaterialCost = totalExtraMaterialCost / mileageDriven * 100;
				consumption.setAverageExtraMaterialCost(averageExtraMaterialCost);

				averageLaborCost = totalLaborCost / mileageDriven * 100;
				consumption.setAverageLaborCost(averageLaborCost);
			}
		}
	}
}
