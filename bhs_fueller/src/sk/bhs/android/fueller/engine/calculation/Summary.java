package sk.bhs.android.fueller.engine.calculation;

import java.util.LinkedList;

import sk.bhs.android.fueller.dataModel.expense.*;
import sk.bhs.android.fueller.dataModel.expense.FuellingEntry.FuelType;

public class Summary {
	public static void printSummary(History history) {
		LinkedList<FuellingEntry> entries = history.getFuellingEntries();
		double totalLPG = 0;
		double totalGasoline = 0;
		
		double totalMileage = entries.getLast().getMileage() - entries.getFirst().getMileage();
		
		for (FuellingEntry e: entries) {
			if (e.getFuelType() == FuelType.LPG)
				totalLPG += e.getFuelVolume();
			if (e.getFuelType() == FuelType.GASOLINE)
				totalGasoline += e.getFuelVolume();
		}
		
		System.out.println("Average consumption of your car is: "+ totalLPG/totalMileage*100 +
				" of LPG and "+ totalGasoline/totalMileage*100 +
				" of gasoline. In total "+ (totalLPG+totalGasoline)/totalMileage*100);
	}
}
