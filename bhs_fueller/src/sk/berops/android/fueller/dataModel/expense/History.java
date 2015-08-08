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
	
	public Entry getEntry(int dynamicID) {
		if (getEntries() == null) return null;
		
		for (Entry e : getEntries()) {
			if (dynamicID == e.getDynamicId()) {
				return e;
			}
		}
		
		return null;
	}
	
	private <T extends Entry> LinkedList<T> getEntriesOfType(T entry) {
		LinkedList<T> entriesOfType = new LinkedList<T>();
		for (Entry e: getEntries()) {
			if (e.getExpenseType() == entry.getExpenseType()) {
				entriesOfType.add((T) e);
			}
		}
		Collections.sort(entriesOfType);
		return entriesOfType;
	}
	
	public LinkedList<Entry> getEntriesFiltered(ExpenseType filter) {
		LinkedList<Entry> entries = this.getEntries();
		LinkedList<Entry> entriesFiltered = new LinkedList<Entry>();
		for (Entry entry : entries) {
			if (entry.getExpenseType() == filter) {
				entriesFiltered.add(entry);
			}
		}
		return entriesFiltered;
	}
	
	public LinkedList<FuellingEntry> getFuellingEntries() {
		return getEntriesOfType(new FuellingEntry());
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
		return getEntriesOfType(new MaintenanceEntry());
	}
	
	public LinkedList<ServiceEntry> getServiceEntries() {
		return getEntriesOfType(new ServiceEntry());
	}
	
	public LinkedList<TollEntry> getTollEntries() {
		return getEntriesOfType(new TollEntry());
	}
	
	public LinkedList<InsuranceEntry> getInsuranceEntries() {
		return getEntriesOfType(new InsuranceEntry());
	}
	
	public LinkedList<BurreaucraticEntry> getBurreaucraticEntries() {
		return getEntriesOfType(new BurreaucraticEntry());
	}
	
	public LinkedList<OtherEntry> getOtherEntries() {
		return getEntriesOfType(new OtherEntry());
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
