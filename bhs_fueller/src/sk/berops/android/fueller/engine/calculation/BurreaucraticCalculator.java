package sk.berops.android.fueller.engine.calculation;

import java.util.LinkedList;
import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.calculation.BurreaucraticConsumption;
import sk.berops.android.fueller.dataModel.calculation.InsuranceConsumption;
import sk.berops.android.fueller.dataModel.expense.BurreaucraticEntry;
import sk.berops.android.fueller.dataModel.expense.InsuranceEntry;
import sk.berops.android.fueller.gui.MainActivity;

public class BurreaucraticCalculator {
	public static void calculate(LinkedList<BurreaucraticEntry> entries) {
		double totalCost = 0;
		double averageCost = 0;
		
		double mileageDriven = 0;

		BurreaucraticConsumption consumption;
		
		for (BurreaucraticEntry b: entries) {
			consumption = (BurreaucraticConsumption) b.getConsumption();
			mileageDriven = b.getMileageSI() - MainActivity.garage.getActiveCar().getInitialMileageSI();

			totalCost += b.getCostSI();
			consumption.setTotalBurreaucraticCost(totalCost);
			
			if (mileageDriven > 0.0) {
				averageCost = totalCost / mileageDriven * 100;
				consumption.setAverageBurreaucraticCost(averageCost);
			}
		}
	}
}
