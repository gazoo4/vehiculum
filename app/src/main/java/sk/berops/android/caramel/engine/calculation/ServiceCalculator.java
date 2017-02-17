package sk.berops.android.caramel.engine.calculation;

import java.util.LinkedList;
import java.util.TreeMap;

import sk.berops.android.caramel.dataModel.calculation.ServiceConsumption;
import sk.berops.android.caramel.dataModel.expense.ServiceEntry;
import sk.berops.android.caramel.gui.MainActivity;

public class ServiceCalculator {
	public static void calculate(LinkedList<ServiceEntry> entries) {
		double totalCost = 0;
		double averageCost = 0;
		
		TreeMap<ServiceEntry.Type, Double> totalCostPerServiceType = new TreeMap<ServiceEntry.Type, Double>();
		TreeMap<ServiceEntry.Type, Double> averageCostPerServiceType = new TreeMap<ServiceEntry.Type, Double>();
		
		double mileageDriven = 0;
		TreeMap<ServiceEntry.Type, Integer> countPerServiceType = new TreeMap<ServiceEntry.Type, Integer>();

		ServiceConsumption consumption;
		ServiceEntry.Type type;
		
		for (ServiceEntry s: entries) {
			consumption = (ServiceConsumption) s.getConsumption();
			type = s.getType();
			mileageDriven = s.getMileageSI() - MainActivity.garage.getActiveCar().getInitialMileageSI();
			
			if (countPerServiceType.get(type) == null) {
				countPerServiceType.put(type, 0);
				totalCostPerServiceType.put(type, 0.0);
				averageCostPerServiceType.put(type, 0.0);
			}
			
			countPerServiceType.put(type, countPerServiceType.get(type).intValue() + 1);

			totalCost += s.getCostSI();
			consumption.setTotalServiceCost(totalCost);
			
			if (totalCostPerServiceType.get(type) == null) {
				totalCostPerServiceType.put(type, 0.0);
			}
			double typeTotalCost = totalCostPerServiceType.get(type) + s.getCostSI();
			totalCostPerServiceType.put(type, typeTotalCost);
			TreeMap<ServiceEntry.Type, Double> totalCostPerServiceTypeCopy = new TreeMap<ServiceEntry.Type, Double>(totalCostPerServiceType);
			consumption.setTotalCostPerServiceType(totalCostPerServiceTypeCopy);
			
			if (mileageDriven > 0.0) {
				averageCost = totalCost / mileageDriven * 100;
				consumption.setAverageServiceCost(averageCost);

				double typeAverageCost = typeTotalCost / mileageDriven * 100;
				averageCostPerServiceType.put(type, typeAverageCost);
				TreeMap<ServiceEntry.Type, Double> averageCostPerServiceTypeCopy = new TreeMap<ServiceEntry.Type, Double>(averageCostPerServiceType);
				consumption.setAverageCostPerServiceType(averageCostPerServiceTypeCopy);
			}
		}
	}
}
