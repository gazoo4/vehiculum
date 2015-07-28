package sk.berops.android.fueller.engine.calculation;

import java.util.LinkedList;
import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.calculation.TollConsumption;
import sk.berops.android.fueller.dataModel.expense.TollEntry;
import sk.berops.android.fueller.gui.MainActivity;

public class TollCalculator {
	public static void calculate(LinkedList<TollEntry> entries) {
		double totalCost = 0;
		double averageCost = 0;
		
		TreeMap<TollEntry.Type, Double> totalCostPerTollType = new TreeMap<TollEntry.Type, Double>();
		TreeMap<TollEntry.Type, Double> averageCostPerTollType = new TreeMap<TollEntry.Type, Double>();
		
		double mileageDriven = 0;
		TreeMap<TollEntry.Type, Integer> countPerTollType = new TreeMap<TollEntry.Type, Integer>();

		TollConsumption consumption;
		TollEntry.Type type;
		
		for (TollEntry t: entries) {
			consumption = (TollConsumption) t.getConsumption();
			type = t.getType();
			mileageDriven = t.getMileageSI() - MainActivity.garage.getActiveCar().getInitialMileageSI();
			
			if (countPerTollType.get(type) == null) {
				countPerTollType.put(type, 0);
				totalCostPerTollType.put(type, 0.0);
				averageCostPerTollType.put(type, 0.0);
			}
			
			countPerTollType.put(type, countPerTollType.get(type).intValue() + 1);

			totalCost += t.getCostSI();
			consumption.setTotalCost(totalCost);
			
			if (totalCostPerTollType.get(type) == null) {
				totalCostPerTollType.put(type, 0.0);
			}
			double typeTotalCost = totalCostPerTollType.get(type) + t.getCostSI();
			totalCostPerTollType.put(type, typeTotalCost);
			TreeMap<TollEntry.Type, Double> totalCostPerTollTypeCopy = new TreeMap<TollEntry.Type, Double>(totalCostPerTollType);
			consumption.setTotalCostPerTollType(totalCostPerTollTypeCopy);
			
			if (mileageDriven > 0.0) {
				averageCost = totalCost / mileageDriven * 100;
				consumption.setAverageCost(averageCost);

				double typeAverageCost = typeTotalCost / mileageDriven * 100;
				averageCostPerTollType.put(type, typeAverageCost);
				TreeMap<TollEntry.Type, Double> averageCostPerTollTypeCopy = new TreeMap<TollEntry.Type, Double>(averageCostPerTollType);
				consumption.setAverageCostPerTollType(averageCostPerTollTypeCopy);
			}
		}
	}
}
