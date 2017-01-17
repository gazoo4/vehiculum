package sk.berops.android.fueller.dataModel.expense;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.maintenance.TyreConfigurationScheme;
import sk.berops.android.fueller.gui.MainActivity;

public class TyreChangeEntry extends Entry {
	
	@Element(name="laborCost", required=false)
	private double laborCost;

	@Element(name="extraMaterialCost", required=false)
	private double extraMaterialCost;

	@Element(name="tyresCost", required=false)
	private double tyresCost;

	/**
	 * Car's initial tyreScheme. Not used yet. So far we assume that the initiall tyreScheme is always null (meaning no tyres installed). 
	 */
	@Element(name="tyreScheme", required=false)
	private TyreConfigurationScheme tyreScheme;

	/**
	 * List of tyres bought 
	 */
	@ElementList(inline=true, required=false)
	private ArrayList<Tyre> boughtTyres;
	
	/**
	 * List of tyres thrown away
	 */
	@ElementList(inline=true, required=false)
	private ArrayList<UUID> deletedTyreIDs;

	/**
	 * Map of tyre IDs with new thread levels of the respective tyres
	 */
	@ElementMap(inline = false, required = false)
	private HashMap<UUID, Double> threadLevelUpdate;

	public TyreChangeEntry() {
		super();
		boughtTyres = new ArrayList<>();
		deletedTyreIDs = new ArrayList<>();
		threadLevelUpdate = new HashMap<>();
		setExpenseType(ExpenseType.TYRES);
	}
	
	@Override
	public void initAfterLoad(Car car) {
		super.initAfterLoad(car);
		getTyreScheme().initAfterLoad(car);
	}

	public double getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(double laborCost) {
		this.laborCost = laborCost;
	}
	
	public double getLaborCostSI() {
		return Currency.convertToSI(getLaborCost(), getCurrency(), getEventDate());
	}

	public double getExtraMaterialCost() {
		return extraMaterialCost;
	}

	public void setExtraMaterialCost(double extraMaterialCost) {
		this.extraMaterialCost = extraMaterialCost;
	}
	
	public double getExtraMaterialCostSI() {
		return Currency.convertToSI(getExtraMaterialCost(), getCurrency(), getEventDate());
	}

	public double getTyresCost() {
		return tyresCost;
	}

	public void setTyresCost(double tyresCost) {
		this.tyresCost = tyresCost;
	}
	
	public double getTyresCostSI() {
		return Currency.convertToSI(getTyresCost(), getCurrency(), getEventDate());
	}
	
	public TyreConfigurationScheme getTyreScheme() {
		if (tyreScheme == null) {
			tyreScheme = new TyreConfigurationScheme(car);
		}
		return tyreScheme;
	}
	
	public void setTyreScheme(TyreConfigurationScheme tyreScheme) {
		this.tyreScheme = tyreScheme;
	}

	public ArrayList<Tyre> getBoughtTyres() {
		return boughtTyres;
	}

	public void setBoughtTyres(ArrayList<Tyre> boughtTyres) {
		this.boughtTyres = boughtTyres;
	}

	public ArrayList<UUID> getDeletedTyreIDs() {
		return deletedTyreIDs;
	}

	public void setDeletedTyreIDs(ArrayList<UUID> deletedTyreIDs) {
		this.deletedTyreIDs = deletedTyreIDs;
	}

	public ArrayList<Tyre> getDeletedTyres() {
		ArrayList<Tyre> list = new ArrayList<>();
		Tyre t;
		for (UUID tUUID: getDeletedTyreIDs()) {
			t = MainActivity.garage.getTyreByID(tUUID);
			if (t == null) {
				for (Tyre bt: getBoughtTyres()) {
					if (bt.getUuid().equals(tUUID)) {
						t = bt;
					}
				}
			}
			list.add(t);
		}
		return list;
	}

	/**
	 * Map of tyre IDs with new thread levels of the respective tyres
	 */
	public HashMap<UUID, Double> getThreadLevelUpdate() {
		return threadLevelUpdate;
	}

	public void setThreadLevelUpdate(HashMap<UUID, Double> threadLevelUpdate) {
		this.threadLevelUpdate = threadLevelUpdate;
	}

	/**
	 * Gives the list of all tyres ever bought and not trashed
	 * @return list of all tyres
	 */
	public LinkedList<Tyre> getAllTyres() {
		return getAllTyresToDate(null);
	}

	/**
	 * Get a snapshot of the tyre inventory (i.e. available tyres - bought and not trashed)
	 * from just before a specific date. This doesn't take into the consideration whether tyres are
	 * installed on the car or not to the specific date. This needs to be taken care of outside of
	 * this method.
	 * @param dateTo date to which a snapshot is required
	 * @return LinkedList<Tyre> tyre inventory snapshot
	 */
	public LinkedList<Tyre> getAllTyresToDate(Date dateTo) {
		LinkedList<Tyre> tyres = new LinkedList<>();
		LinkedList<TyreChangeEntry> entries;

		for (Car c: MainActivity.garage.getCars()) {
			entries = c.getHistory().getTyreChangeEntries();
			if (c == MainActivity.garage.getActiveCar()
					&& !entries.contains(this)) {
				// Consider the entry being edited as well here, so that we can use the tyres just bought
				entries.add(this);
			}
			History.filterEntriesByDate(entries, null, dateTo);
			for (TyreChangeEntry e: entries) {
				tyres.addAll(e.getBoughtTyres());
				tyres.removeAll(e.getDeletedTyres());
			}
		}

		return tyres;
	}

	public LinkedList<Tyre> getAllTyresByNow() {
		return getAllTyresToDate(this.getEventDate());
	}

	/**
	 * Get a snapshot of the tyre inventory from just before a specific entry occurred
	 * @return LinkedList<Tyre> tyre inventory snapshot
	 */
	public LinkedList<Tyre> getAvailableTyres() {
		LinkedList<Tyre> list = getAllTyresByNow();
		list.removeAll(getInstalledTyres());
		return list;
	}

	public LinkedList<Tyre> getInstalledTyres() {
		LinkedList<Tyre> list = new LinkedList<>();

		for (Axle a: getTyreScheme().getAxles()) {
			for (UUID u: a.getTyreIDs()) {
				list.add(getTyreByID(u));
			}
		}

		return list;
	}

	/**
	 * Based on the supplied identifier, return the matching tyre
	 * @param id identifier
	 * @return Tyre instance of the matching tyre. If tyre not found, returns null.
	 */
	public Tyre getTyreByID(UUID id) {
		for (Tyre t : getAllTyres()) {
			if (t.getUuid().equals(id)) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Get a list of tyres from the supplied list of tyre UUIDs
	 * Here we search the garage for the available tyres + the current instance of TyreChangeEntry
	 * (to see tyres which were just bought, but the entry hasn't been saved yet).
	 * @param uuids
	 * @return
	 */
	public LinkedList<Tyre> getTyresByIDs(Collection<UUID> uuids) {
		LinkedList<Tyre> tyrePool = getAllTyres();

		LinkedList<Tyre> tyres = new LinkedList<>();
		for (UUID u : uuids) {
			if (u == null) {
				tyres.add(null);
				continue;
			}
			for (Tyre t: tyrePool) {
				if (t.getUuid().equals(u)) {
					tyres.add(t);
					break;
				}
			}
		}
		return tyres;
	}
}