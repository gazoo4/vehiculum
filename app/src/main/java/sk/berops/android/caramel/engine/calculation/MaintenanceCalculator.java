package sk.berops.android.caramel.engine.calculation;

import java.util.LinkedList;
import java.util.TreeMap;

import sk.berops.android.caramel.dataModel.calculation.MaintenanceConsumption;
import sk.berops.android.caramel.dataModel.expense.MaintenanceEntry;
import sk.berops.android.caramel.gui.MainActivity;

public class MaintenanceCalculator {

	public static void calculate(LinkedList<MaintenanceEntry> entries) {
		double totalCost = 0;
		double averageCost = 0;
		double totalLaborCost = 0;
		double averageLaborCost = 0;
		double totalPartsCost = 0;
		double averagePartsCost = 0;

		TreeMap<MaintenanceEntry.Type, Double> totalCostPerMaintenanceType = new TreeMap<MaintenanceEntry.Type, Double>();
		TreeMap<MaintenanceEntry.Type, Double> averageCostPerMaintenanceType = new TreeMap<MaintenanceEntry.Type, Double>();

		double mileageDriven = 0;
		TreeMap<MaintenanceEntry.Type, Integer> countPerMaintenanceType = new TreeMap<MaintenanceEntry.Type, Integer>();

		MaintenanceConsumption consumption;
		MaintenanceEntry.Type type;

		for (MaintenanceEntry m : entries) {
			consumption = (MaintenanceConsumption) m.getConsumption();
			type = m.getType();
			mileageDriven = m.getMileageSI() - MainActivity.garage.getActiveCar().getInitialMileageSI();

			if (countPerMaintenanceType.get(type) == null) {
				countPerMaintenanceType.put(type, 0);
				totalCostPerMaintenanceType.put(type, 0.0);
				averageCostPerMaintenanceType.put(type, 0.0);
			}
			
			countPerMaintenanceType.put(type, countPerMaintenanceType.get(type).intValue() + 1);

			totalCost += m.getCostSI();
			consumption.setTotalMaintenanceCost(totalCost);

			totalLaborCost += m.getLaborCostSI();
			consumption.setTotalLaborCost(totalLaborCost);

			totalPartsCost += m.getPartsCostSI();
			consumption.setTotalPartsCost(totalPartsCost);
			
			if (totalCostPerMaintenanceType.get(type) == null) {
				totalCostPerMaintenanceType.put(type, 0.0);
			}
			double typeTotalCost = totalCostPerMaintenanceType.get(type) + m.getCostSI();
			totalCostPerMaintenanceType.put(type, typeTotalCost);
			TreeMap<MaintenanceEntry.Type, Double> totalCostPerMaintenanceTypeCopy = new TreeMap<MaintenanceEntry.Type, Double>(totalCostPerMaintenanceType);
			consumption.setTotalCostPerMaintenanceType(totalCostPerMaintenanceTypeCopy);

			if (mileageDriven > 0.0) {
				averageCost = totalCost / mileageDriven * 100;
				consumption.setAverageMaintenanceCost(averageCost);

				averageLaborCost = totalLaborCost / mileageDriven * 100;
				consumption.setAverageLaborCost(averageLaborCost);

				averagePartsCost = totalPartsCost / mileageDriven * 100;
				consumption.setAveragePartsCost(averagePartsCost);
				
				double typeAverageCost = typeTotalCost / mileageDriven * 100;
				averageCostPerMaintenanceType.put(type, typeAverageCost);
				TreeMap<MaintenanceEntry.Type, Double> averageCostPerMaintenanceTypeCopy = new TreeMap<MaintenanceEntry.Type, Double>(averageCostPerMaintenanceType);
				consumption.setAverageCostPerMaintenanceType(averageCostPerMaintenanceTypeCopy);
			}
		}
	}

}
