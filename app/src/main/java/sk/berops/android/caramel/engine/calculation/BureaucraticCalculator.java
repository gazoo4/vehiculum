package sk.berops.android.caramel.engine.calculation;

import java.util.LinkedList;

import sk.berops.android.caramel.dataModel.calculation.BureaucraticConsumption;
import sk.berops.android.caramel.dataModel.expense.BureaucraticEntry;
import sk.berops.android.caramel.gui.MainActivity;

public class BureaucraticCalculator {
	public static void calculate(LinkedList<BureaucraticEntry> entries) {
		double totalCost = 0;
		double averageCost = 0;
		
		double mileageDriven = 0;

		BureaucraticConsumption consumption;
		
		for (BureaucraticEntry b: entries) {
			consumption = (BureaucraticConsumption) b.getConsumption();
			mileageDriven = b.getMileageSI() - MainActivity.garage.getActiveCar().getInitialMileageSI();

			totalCost += b.getCostSI();
			consumption.setTotalBureaucraticCost(totalCost);
			
			if (mileageDriven > 0.0) {
				averageCost = totalCost / mileageDriven * 100;
				consumption.setAverageBureaucraticCost(averageCost);
			}
		}
	}
}
