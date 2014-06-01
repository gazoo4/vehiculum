package sk.berops.android.fueller.engine.calculation;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.History;

public class FloatingConsumption {
	
	public static void calculateConsumption(History history, int precision, boolean dropMinMax) throws CalculationException {
		LinkedList<FuellingEntry> entries = history.getFuellingEntries();
		LinkedList<Double> consumptions;
		FuellingEntry entry;
		FuellingEntry entryPrevious;
		
		if (precision < 3) {
			throw new CalculationException("Precision should be 3 at minimum");
		}
		if (dropMinMax && precision < 4) {
			throw new CalculationException("Precision should be 4 at minimum when cutting min/max from the samples");
		}
		
		double consumption;
		double distance;
		double volume;
		double avg;
		ListIterator<FuellingEntry> iterator;
		
		//we should calculate the consumption separately for each fuel type
		for (FuellingEntry.FuelType fuelType : FuellingEntry.FuelType.values()) {
			entries = history.getFuellingEntriesFiltered(fuelType);
			if (entries.size() < 2) {
				continue;
			}
			consumption = volume = distance = 0;
			for (int i = 1; i < entries.size(); i++) {
				iterator = entries.listIterator(i+1);
				entry = iterator.previous();
				consumptions = new LinkedList<Double>();
				for (int j = 1; j <= precision && iterator.hasPrevious(); j++) {
					// here we could go backwards as much as precision, cutting off the min/max values and calculating floating consumption easily
					entryPrevious = iterator.previous();
					distance = entry.getMileage() - entryPrevious.getMileage();
					volume = entry.getFuelVolume();
					consumption = 100*volume/distance;
					consumptions.push(consumption);
					entry = entryPrevious;
				}
				if (dropMinMax && consumptions.size() >= 4) {
					double dropValue;
					dropValue = Collections.min(consumptions);
					consumptions.remove(dropValue);
					dropValue = Collections.max(consumptions);
					consumptions.remove(dropValue);
				}
				avg = avg(consumptions);
				entries.get(i).setFloatingConsumption(avg);
			}
		}
	}
	
    public static double avg(Collection<Double> collection) {
        Iterator<Double> it = collection.iterator();
        int counter = 0;
        double sum = 0;
        while (it.hasNext()) {
        	sum += it.next();
            counter++;
        }
        return sum/counter;
    }
}