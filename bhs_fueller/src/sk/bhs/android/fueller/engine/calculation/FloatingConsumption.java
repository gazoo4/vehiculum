package sk.bhs.android.fueller.engine.calculation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import sk.bhs.android.fueller.dataModel.Car;
import sk.bhs.android.fueller.dataModel.expense.FuellingEntry;

public class FloatingConsumption {
	private Car car;
	
	public FloatingConsumption(Car car) {
	}
	
	public void computeFloatingConsumption(int precision, boolean dropMinMax) throws CalculationException {
		LinkedList<FuellingEntry> entries = car.getHistory().getFuellingEntries();
		
		if (precision < 3) {
			throw new CalculationException("Precision should be 3 at minimum");
		}
		if (dropMinMax && precision < 4) {
			throw new CalculationException("Precision should be 4 at minimum when cutting min/max from the samples");
		}
		
		double consumption;
		double distance;
		double volume;
		FuellingEntry entry;
		//we should calculate the consumption separately for each fuel type
		for (FuellingEntry.FuelType fuelType : FuellingEntry.FuelType.values()) {
			entries = car.getHistory().getFuellingEntriesFiltered(fuelType);
			ListIterator iterator;
			if (entries.size() < 2) {
				continue;
			}
			consumption = volume = distance = 0;
			for (int i = 1; i < entries.size(); i++) {
				for (int j = 1; j <= precision; j++) {
					iterator = entries.listIterator(i);
					// here we could go backwards as much as precision, cutting off the min/max values and calculating floating consumption easily
				}
			}
		}
	}
}