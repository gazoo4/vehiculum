package sk.berops.android.vehiculum.dataModel.expense;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Axle;
import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.charting.PieCharter;
import sk.berops.android.vehiculum.dataModel.charting.TyreCharter;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;
import sk.berops.android.vehiculum.dataModel.maintenance.TyreConfigurationScheme;
import sk.berops.android.vehiculum.engine.calculation.NewGenTyreConsumption;
import sk.berops.android.vehiculum.engine.synchronization.controllers.TyreChangeEntryController;
import sk.berops.android.vehiculum.gui.MainActivity;

public class TyreChangeEntry extends Entry {
	
	@Element(name="laborCost", required=false)
	private Cost laborCost;

	@Element(name="extraMaterialCost", required=false)
	private Cost extraMaterialCost;

	@Element(name="tyresCost", required=false)
	private Cost tyresCost;

	/**
	 * Car's initial tyreScheme. Not used yet. So far we assume that the initial tyreScheme is always null (meaning no tyres installed).
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

	public Cost getLaborCost() {
		return laborCost;
	}

	public void setLaborCost(Cost laborCost) {
		this.laborCost = laborCost;
	}

	public Cost getExtraMaterialCost() {
		return extraMaterialCost;
	}

	public void setExtraMaterialCost(Cost extraMaterialCost) {
		this.extraMaterialCost = extraMaterialCost;
	}

	public Cost getTyresCost() {
		return tyresCost;
	}

	public void setTyresCost(Cost tyresCost) {
		this.tyresCost = tyresCost;
	}
	
	public TyreConfigurationScheme getTyreScheme() {
		if (tyreScheme == null) {
			tyreScheme = car.getTyreSchemeByDate(getEventDate()).clone();
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
			LinkedList<Tyre> deletedTyres;
			for (TyreChangeEntry e: entries) {
				// The entry carries Tyre boughtTyres(), so we can access them directly
				tyres.addAll(e.getBoughtTyres());
				// For the deletedTyres the entry carries only UUIDs so we can't access them directly
				deletedTyres = new LinkedList<>();
				for (UUID uuid: e.getDeletedTyreIDs()) {
					for (Tyre t: tyres) {
						if (uuid.equals(t.getUuid())) {
							deletedTyres.add(t);
						}
					}
				}
				tyres.removeAll(deletedTyres);
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
			for (int i = 0; i < a.getType().getTyreCount(); i ++) {
				list.add(getTyreByID(a.getTyreIDs().get(i)));
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
		if (id == null) return null;

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

	public NewGenTyreConsumption getTyreConsumption() {
		return (NewGenTyreConsumption) getConsumption();
	}

	public NewGenTyreConsumption generateConsumption() {
		return new NewGenTyreConsumption();
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public TyreChangeEntryController getController() {
		return new TyreChangeEntryController(this);
	}

	/****************************** Searchable interface methods follow ***************************/

	/**
	 * Method used to search for an object by its UUID within the Object tree of this Object.
	 * @param uuid of the searched object
	 * @return Record that matches the searched UUID
	 */
	@Override
	public Record getRecordByUUID(UUID uuid) {
		// Are they looking for me? Delegate task to Record.getRecordByUUID to find out.
		Record result = super.getRecordByUUID(uuid);

		if (result == null) {
			result = laborCost.getRecordByUUID(uuid);
		}

		if (result == null) {
			result = extraMaterialCost.getRecordByUUID(uuid);
		}

		if (result == null) {
			result = tyresCost.getRecordByUUID(uuid);
		}

		// Check if the object is in the TyreConfigurationScheme
		if (result == null) {
			result = tyreScheme.getRecordByUUID(uuid);
		}

		Iterator<Tyre> t = boughtTyres.iterator();
		// Check if the object is among the boughtTyres
		while (result == null && t.hasNext()) {
			result = t.next().getRecordByUUID(uuid);
		}

		return result;
	}

	/****************************** PieChartable interface methods follow *************************/

	public PieCharter getPieCharter() {
		return (charter == null) ? generatePieCharter() : charter;
	}

	public TyreCharter generatePieCharter() {
		return new TyreCharter(getTyreConsumption());
	}
}