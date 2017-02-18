package sk.berops.android.vehiculum.engine.calculation;

import java.util.LinkedList;
import java.util.TreeMap;

import sk.berops.android.vehiculum.dataModel.calculation.InsuranceConsumption;
import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;
import sk.berops.android.vehiculum.gui.MainActivity;

public class InsuranceCalculator {
	public static void calculate(LinkedList<InsuranceEntry> entries) {
		double totalCost = 0;
		double averageCost = 0;
		
		TreeMap<InsuranceEntry.Type, Double> totalCostPerInsuranceType = new TreeMap<InsuranceEntry.Type, Double>();
		TreeMap<InsuranceEntry.Type, Double> averageCostPerInsuranceType = new TreeMap<InsuranceEntry.Type, Double>();
		
		double mileageDriven = 0;
		TreeMap<InsuranceEntry.Type, Integer> countPerInsuranceType = new TreeMap<InsuranceEntry.Type, Integer>();

		InsuranceConsumption consumption;
		InsuranceEntry.Type type;
		
		for (InsuranceEntry i: entries) {
			consumption = (InsuranceConsumption) i.getConsumption();
			type = i.getType();
			mileageDriven = i.getMileageSI() - MainActivity.garage.getActiveCar().getInitialMileageSI();
			
			if (countPerInsuranceType.get(type) == null) {
				countPerInsuranceType.put(type, 0);
				totalCostPerInsuranceType.put(type, 0.0);
				averageCostPerInsuranceType.put(type, 0.0);
			}
			
			countPerInsuranceType.put(type, countPerInsuranceType.get(type).intValue() + 1);

			totalCost += i.getCostSI();
			consumption.setTotalInsuranceCost(totalCost);
			
			if (totalCostPerInsuranceType.get(type) == null) {
				totalCostPerInsuranceType.put(type, 0.0);
			}
			double typeTotalCost = totalCostPerInsuranceType.get(type) + i.getCostSI();
			totalCostPerInsuranceType.put(type, typeTotalCost);
			TreeMap<InsuranceEntry.Type, Double> totalCostPerInsuranceTypeCopy = new TreeMap<InsuranceEntry.Type, Double>(totalCostPerInsuranceType);
			consumption.setTotalCostPerInsuranceType(totalCostPerInsuranceTypeCopy);
			
			if (mileageDriven > 0.0) {
				averageCost = totalCost / mileageDriven * 100;
				consumption.setAverageInsuranceCost(averageCost);

				double typeAverageCost = typeTotalCost / mileageDriven * 100;
				averageCostPerInsuranceType.put(type, typeAverageCost);
				TreeMap<InsuranceEntry.Type, Double> averageCostPerInsuranceTypeCopy = new TreeMap<InsuranceEntry.Type, Double>(averageCostPerInsuranceType);
				consumption.setAverageCostPerInsuranceType(averageCostPerInsuranceTypeCopy);
			}
		}
	}
}