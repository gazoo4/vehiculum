package sk.berops.android.fueller.engine.calculation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.SortedMap;
import java.util.TreeMap;

import android.graphics.Color;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.dataModel.expense.FuellingEntry.FuelType;
import sk.berops.android.fueller.R;

public class SimpleConsumption {

	public static Consumption getTotalAverage(Car car) throws CalculationException {
		History history = car.getHistory();
		LinkedList<FuellingEntry> entries = history.getFuellingEntries();
		FuellingEntry e;
		double volume = 0;
		FuelType type;
		
		if (entries.size() < 2) {
			throw new CalculationException("Too little data for statistics");
		}
		
		Consumption consumption = new Consumption();
		
		double totalDistance = entries.getLast().getMileage() - entries.getFirst().getMileage();
		TreeMap<FuellingEntry.FuelType, Double> totalFuels = new TreeMap();
		for (FuelType t : FuelType.values()) {
			totalFuels.put(t, (double) 0);
		}

		/* Collect the values across fuellings */
		for (int i = 1; i < entries.size() ; i++) {
			e = entries.get(i);
			type = e.getFuelType();
			volume = e.getFuelVolume();
			volume += totalFuels.get(type);
			totalFuels.put(type, volume);
		}
		
		/* Count the average consumption */
		for (FuelType t: FuelType.values()) {
			consumption.getPerType().put(t, totalFuels.get(t) / totalDistance * 100);
			consumption.setTotal(consumption.getTotal() + totalFuels.get(t) / totalDistance * 100);
		}
		
		return consumption;
	}
	
	public static void calculateConsumption(History history) {
		LinkedList<FuellingEntry> entries;
		ListIterator<FuellingEntry> i;
		FuellingEntry e;
		double volume, mileage1, mileage2;
		
		for (FuellingEntry.FuelType type : FuellingEntry.FuelType.values()) {
			entries = history.getFuellingEntriesFiltered(type);
			if (entries.size() <= 1) continue;
			i = entries.listIterator();
			e = i.next();
			mileage1 = e.getMileage();
			while (i.hasNext()) {
				e = i.next();
				mileage2 = e.getMileage();
				volume = e.getFuelVolume();
				e.setConsumptionSI(100*volume/(mileage2 - mileage1));
				mileage1 = mileage2;
			}
		}
	}
	
	public static Consumption.SinceLastRefuel sinceLastRefuel(History history) throws CalculationException {
		LinkedList<FuellingEntry> entries = history.getFuellingEntries();
		FuellingEntry e;
		double volume = 0;
		double mileage = 0;
		FuelType type;
		
		if (entries.size() < 2) {
			throw new CalculationException("Too little data for statistics");
		}
	
		Consumption.SinceLastRefuel consumption = new Consumption().new SinceLastRefuel();
		
		ListIterator<FuellingEntry> i = entries.listIterator(entries.size());
		
		e = i.previous();
		type = e.getFuelType();
		volume = e.getFuelVolume();
		mileage = e.getMileage();
		
		boolean found = false;
		while (i.hasPrevious()) {
			e = i.previous();
			if (e.getFuelType() == type) {
				found = true;
				break;
			}
		}
		if (!found) return null;
		
		consumption.setFuelType(type);
		consumption.setConsumption(volume / (mileage - e.getMileage()) * 100);
		
		return consumption;
	}
	
	public static int getConsumptionColor(double average, double oneOff) {
		double ratio = oneOff/average;
		if (ratio >= 1.1) {
			return Color.rgb(255, 0, 0);
		} else if (ratio > 0.9) {
			double coef = (ratio - 0.9)/0.2;
			int red = (int) (255 * coef);
			int green = (int) (255 * (1 - coef));
			return Color.rgb(red, green, 0);
		} else {
			return Color.rgb(0, 255, 0);
		}
	}
	
	public static double getTotalMileage(Car car) {
		double totalMileage = car.getCurrentMileage() - car.getInitialMileage();
		return totalMileage;
	}
	
	public static double getTotalFuelVolume(History history) {
		double totalFuel = 0;
		LinkedList<FuellingEntry> entries = history.getFuellingEntries();
		for (FuellingEntry e: entries) {
			totalFuel += e.getFuelVolume();
		}
		return totalFuel;
	}
}
