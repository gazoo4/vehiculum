package sk.berops.android.fueller.engine.calculation;

import java.util.LinkedList;
import java.util.TreeMap;

import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.calculation.FuelConsumption;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;

public class GenericCalculator {
	
	public static void calculate(LinkedList<Entry> entries) {
		Entry entryFirst = null;
		Entry entryLast = new FuellingEntry();
		
		TreeMap<ExpenseType, Entry> entryFirstPerEntryType = new TreeMap<ExpenseType, Entry>();
		TreeMap<ExpenseType, Entry> entryLastPerEntryType = new TreeMap<ExpenseType, Entry>();
		
		int entryCount = 0;
		TreeMap<ExpenseType, Integer> entryCountPerEntryType = new TreeMap<ExpenseType, Integer>();
		
		double totalCost = 0.0;
		double typeCost = 0.0;
		TreeMap<ExpenseType, Double> totalCostPerEntryType = new TreeMap<ExpenseType, Double>();
		double averageCost = 0.0;
		TreeMap<ExpenseType, Double> averageCostPerEntryType = new TreeMap<ExpenseType, Double>();
		
		for (Entry e : entries) {
			ExpenseType entryType = e.getExpenseType();
			entryCount++;
			if (entryCountPerEntryType.get(entryType) == null) {
				entryCountPerEntryType.put(entryType, 0);
				totalCostPerEntryType.put(entryType, 0.0);
				averageCostPerEntryType.put(entryType, 0.0);
			}
			entryCountPerEntryType.put(entryType, entryCountPerEntryType.get(entryType) + 1);
			
			Consumption consumption = null;
			
			switch (e.getExpenseType()) {
			case FEE:
				break;
			case FUEL:
				consumption = new FuelConsumption();
				break;
			case MAINTENANCE:
				break;
			case SERVICE:
				break;
			case TYRES:
				break;
			default:
				break;
			}
			
			consumption.setLastEntryType(e.getExpenseType());
			
			totalCost += e.getCost();
			consumption.setTotalCost(totalCost);
			
			typeCost = totalCostPerEntryType.get(e.getExpenseType());
			typeCost += e.getCost();
			totalCostPerEntryType.put(e.getExpenseType(), typeCost);
			TreeMap<ExpenseType, Double> totalCostPerEntryTypeCopy = new TreeMap<ExpenseType, Double>(totalCostPerEntryType);
			consumption.setTotalCostPerEntryType(totalCostPerEntryTypeCopy);
			
			if (entryCount == 1) {
				averageCost = 0.0;
			} else {
				double cost = totalCost - entryFirst.getCost();
				double mileage = e.getMileage() - entryFirst.getMileage();
				averageCost = cost / mileage;
			}
			consumption.setAverageCost(averageCost);
			
			if (entryCountPerEntryType.get(entryType) == 1) {
				averageCostPerEntryType.put(entryType, 0.0);
			} else {
				double fuelCost = totalCostPerEntryType.get(entryType) - entryFirstPerEntryType.get(entryType).getCost();
				double mileage = e.getMileage() - entryFirstPerEntryType.get(entryType).getMileage();
				double averageFuelCostType = fuelCost / mileage;
				averageCostPerEntryType.put(entryType, averageFuelCostType);
			}
			TreeMap<ExpenseType, Double> averageCostPerEntryTypeCopy = new TreeMap<ExpenseType, Double>(averageCostPerEntryType);
			consumption.setAverageCostPerEntryType(averageCostPerEntryTypeCopy);
			
			e.setConsumption(consumption);
			
			if (entryFirst == null) {
				entryFirst = e;
			}
			if (entryFirstPerEntryType.get(entryType) == null) {
				entryFirstPerEntryType.put(entryType, e);
			}
			entryLast = e;
			entryLastPerEntryType.put(entryType, e);
		}
	}
}
