package sk.berops.android.vehiculum.dataModel.expense;


import com.github.mikephil.charting.data.PieEntry;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.charting.PieChartable;
import sk.berops.android.vehiculum.dataModel.charting.Charter;
import sk.berops.android.vehiculum.dataModel.charting.HistoryCharter;
import sk.berops.android.vehiculum.dataModel.expense.Entry.ExpenseType;
import sk.berops.android.vehiculum.engine.synchronization.controllers.HistoryController;

public class History extends Record implements PieChartable {

	private static final long serialVersionUID = -850435628372657221L;
	@ElementList(inline=true, required=false)
	private LinkedList<Entry> entries;

	/**
	 * Charter is a class that's responsible for turning data contained in this object
	 * to data understandable by chart drawer.
	 */
	protected Charter charter;

	/**
	 * If the user wants to zoom in into the PieChart, we relay the data collection to another PieChartable object
	 * stored in relay property.
	 */
	private PieChartable relay;

	public History() {
		super();
		entries = new LinkedList<>();
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

	@SuppressWarnings("unchecked")
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
	
	public LinkedList<BureaucraticEntry> getBureaucraticEntries() {
		return getEntriesOfType(new BureaucraticEntry());
	}
	
	public LinkedList<TyreChangeEntry> getTyreChangeEntries() {
		return getEntriesOfType(new TyreChangeEntry());
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

	public Cost getTotalCost() {
		HashSet<Cost> costs = new HashSet<>();
		LinkedList<Entry> entries = this.getEntries();
		for (Entry e: entries) {
			costs.add(e.getCost());
		}
		return Cost.sum(costs);
	}

	/**
	 * Method used to filter the entries by date. !!! NEEDS TO BE TESTED !!! (especially this '?' magic).
	 * This
	 * @param entries to filter
	 * @param from beginning of the time interval. If null, no entry will be filtered on this parameter.
	 * @param to end of the time interval. If null, no entry will be filtered on this parameter.
	 * @return ArrayList where all entries have eventDate in the interval (from, to)
	 */
	public static void filterEntriesByDate(LinkedList<? extends Entry> entries, Date from, Date to) {
		LinkedList<Entry> outOfScope = new LinkedList<>();
		for (Entry e: entries) {
			if (from == null
					|| from.before(e.getEventDate())
					|| from.equals(e.getEventDate())) {
				if (to == null
						|| to.after(e.getEventDate())
						|| to.equals(e.getEventDate())) {
					// If Entry e has EventDate within the defined interval, it passed the filter
					continue;
				}
			}
			// Otherwise plan for removal of the Entry e from the list
			outOfScope.add(e);
		}

		entries.removeAll(outOfScope);
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public HistoryController getController() {
		return new HistoryController(this);
	}

	/****************************** Searchable interface methods follow ***************************/

	/**
	 * Method used to search for an object by its UUID within the Object tree of this Object.
	 * @param uuid of the searched object
	 * @return Record that matches the searched UUID
	 */
	@Override
	public Record getRecordByUUID(UUID uuid) {
		Record result = null;
		Iterator<Entry> e = entries.iterator();

		while (result == null && e.hasNext()) {
			result = e.next().getRecordByUUID(uuid);
		}

		return result;
	}

	/****************************** PieChartable interface methods follow *************************/

	public Charter getCharter() {
		return (charter == null) ? generateCharter() : charter;
	}

	public Charter generateCharter() {
		return new HistoryCharter(this);
	}

	@Override
	public ArrayList<PieEntry> getPieChartVals() {
		return getCharter().getPieChartVals();
	}

	@Override
	public ArrayList<Integer> getPieChartColors() {
		return getCharter().getPieChartColors();
	}

	@Override
	public String getPieChartLabel() {
		return getCharter().getPieChartLabel();
	}

	@Override
	public boolean invokeSelection(Integer selection) {
		if (relay != null) {
			boolean result = relay.invokeSelection(selection);
			if (!result && selection == null) {
				// Request to remove a leaf (i.e. to move the chart focus one level higher)
				// Relay was asked to process the request, but it failed and the request was to shorten
				// the relay path by 1 level (selection == null), i.e. to display the data of
				// an element one level higher, it means we are becoming the leaf in the chain
				relay = null;
				return true;
			} else {
				// The relay either processed the result correctly or the request wasn't about shortening
				// the relay path. So we're just passing the result of the operation up the chain
				return result;
			}
		} else {
			// relay == null
			if (selection == null) {
				// We are asked to cut the relay path by one level, however we're are the leaf
				return false;
			}
		}

		// Deep dive here and find new Relay PieChartable object based on the selection ID
	}
}
