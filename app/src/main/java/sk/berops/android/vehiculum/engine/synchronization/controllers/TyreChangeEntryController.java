package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.TyreChangeEntry;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;

/**
 * @author Bernard Halas
 * @date 6/23/17
 */

public class TyreChangeEntryController extends EntryController {
	private TyreChangeEntry tyreChangeEntry;

	public TyreChangeEntryController(TyreChangeEntry tyreChangeEntry) {
		super(tyreChangeEntry);
		this.tyreChangeEntry = tyreChangeEntry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		if (!(child instanceof Tyre)) {
			logFailure(child);
			return updated;
		}

		logUpdate("adding boughtTyre");
		tyreChangeEntry.getBoughtTyres().add((Tyre) child);
		updated = true;

		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof TyreChangeEntry)) {
			logFailure(recordUpdate);
			return updated;
		}

		TyreChangeEntry entryUpdate =(TyreChangeEntry) recordUpdate;
		if (tyreChangeEntry.getLaborCost() != entryUpdate.getLaborCost()) {
			tyreChangeEntry.setLaborCost(entryUpdate.getLaborCost());
			logUpdate("laborCost");
			updated = true;
		}

		if (tyreChangeEntry.getExtraMaterialCost() != entryUpdate.getExtraMaterialCost()) {
			tyreChangeEntry.setExtraMaterialCost(entryUpdate.getExtraMaterialCost());
			logUpdate("extraMaterialCost");
			updated = true;
		}

		if (tyreChangeEntry.getTyresCost() != entryUpdate.getTyresCost()) {
			tyreChangeEntry.setTyresCost(entryUpdate.getTyresCost());
			logUpdate("tyresCost");
			updated = true;
		}

		ArrayList<UUID> list = tyreChangeEntry.getDeletedTyreIDs();
		ArrayList<UUID> listUpdate = entryUpdate.getDeletedTyreIDs();
		int size = list.size();
		int sizeUpdate = listUpdate.size();

		// Any UUIDs being deleted?
		list.retainAll(listUpdate);
		if (list.size() != size) {
			logUpdate("deletedTyre");
			updated = true;
		}

		// Any UUIDs being added?
		UUID newItem;
		for (int i = 0; i < sizeUpdate; i++) {
			newItem = listUpdate.get(i);
			if (! list.contains(newItem)) {
				list.add(i, newItem);
				logUpdate("deletedTyre");
				updated = true;
			}
		}

		HashMap<UUID, Double> map = tyreChangeEntry.getThreadLevelUpdate();
		HashMap<UUID, Double> mapUpdate = entryUpdate.getThreadLevelUpdate();

		// Any values being deleted?
		for (UUID key: map.keySet()) {
			if (! mapUpdate.containsKey(key)) {
				map.remove(key);
				logUpdate("threadLevel");
				updated = true;
			}
		}

		// Any new values being added?
		for (UUID key: mapUpdate.keySet()) {
			if (! map.containsKey(key)) {
				map.put(key, mapUpdate.get(key));
				logUpdate("threadLevel");
				updated = true;
			}
		}

		// Any values being altered?
		for (UUID key: map.keySet()) {
			if (map.get(key) != mapUpdate.get(key)) {
				map.put(key, mapUpdate.get(key));
				logUpdate("threadLevel");
				updated = true;
			}
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		Record forDeletion = null;
		for (Record r: tyreChangeEntry.getBoughtTyres()) {
			if (r.getUuid().equals(deleteUUID)) {
				forDeletion = r;
				break;
			}
		}

		if (forDeletion != null) {
			tyreChangeEntry.getBoughtTyres().remove(forDeletion);
			logUpdate("deleted boughtTyre");
			updated = true;
		} else {
			logFailure(deleteUUID);
		}

		return updated;
	}
}
