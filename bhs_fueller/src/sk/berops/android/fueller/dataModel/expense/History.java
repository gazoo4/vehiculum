package sk.berops.android.fueller.dataModel.expense;


import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Car.DistanceUnit;
import sk.berops.android.fueller.dataModel.Car.VolumeUnit;
import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;

public class History implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -850435628372657221L;
	@ElementList(inline=true, required=false)
	private LinkedList<Entry> entries;
	
	public History() {
		super();
		entries = new LinkedList<Entry>();
	}
	
	public void initAfterLoad(Car car) {
		for (Entry e : getEntries()) {
			e.initAfterLoad(car);
		}
		
	}

	public LinkedList<FuellingEntry> getFuellingEntries() {
		LinkedList<FuellingEntry> fuellingEntries = new LinkedList<FuellingEntry>();
		for (Entry e: getEntries()) {
			if (e.getExpenseType() == ExpenseType.FUEL) {
				fuellingEntries.add((FuellingEntry) e);
			}
		}
		Collections.sort(fuellingEntries);
		return fuellingEntries;
	}
	
	public LinkedList<FuellingEntry> getFuellingEntriesFiltered(FuellingEntry.FuelType filter) {
		LinkedList<FuellingEntry> entries = this.getFuellingEntries();
		LinkedList<FuellingEntry> entriesFiltered = new LinkedList<FuellingEntry>();
		for (FuellingEntry fuellingEntry : entries) {
			if (fuellingEntry.getFuelType() == filter) {
				entriesFiltered.add(fuellingEntry);
			}
		}
		return entriesFiltered;
		
	}
	
	public LinkedList<MaintenanceEntry> getMaintenanceEntries() {
		LinkedList<MaintenanceEntry> maintenanceEntries = new LinkedList<MaintenanceEntry>();
		for (Entry e: getEntries()) {
			if (e.getExpenseType() == ExpenseType.MAINTENANCE) {
				maintenanceEntries.add((MaintenanceEntry) e);
			}
		}
		Collections.sort(maintenanceEntries);
		return maintenanceEntries;
	}
	
	public void runAutoCalculations() {
		for (Entry e: getEntries()) {
			if (e.getExpenseType() == Entry.ExpenseType.FUEL) {
				FuellingEntry fe = (FuellingEntry) e;
				fe.setFuelPrice(fe.getCost()/fe.getFuelVolume());
			}
		}
	}
	public LinkedList<Entry> getEntries() {
		Collections.sort(this.entries);
		return this.entries;
	}
	public void setEntries(LinkedList<Entry> entries) {
		this.entries = entries;
	}
	public void removeEntry(Entry entry) {
		this.getEntries().remove(entry);
	}
	public void removeEntry(int dynamicId) {
		Entry entry;
		LinkedList<Entry> entries = this.getEntries();
		Iterator<Entry> i = entries.iterator();
		while (i.hasNext()) {
			entry = i.next();
			if (entry.getDynamicId() == dynamicId) {
				entries.remove(entry);
				return;
			}
		}
	}

	public double getTotalCost() {
		double cost = 0;
		LinkedList<Entry> entries = this.getEntries();
		for (Entry e: entries) {
			cost += e.getCost();
		}
		return cost;
	}
}
