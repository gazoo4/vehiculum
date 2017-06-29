package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.HashSet;
import java.util.TreeMap;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Currency;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.Cost;

/**
 * @author Bernard Halas
 * @date 6/28/17
 */

public class CostController extends RecordController {
	private Cost cost;

	public CostController(Cost cost) {
		super(cost);
		this.cost = cost;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from Cost objects
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof Cost)) {
			logFailure(recordUpdate);
			return updated;
		}

		Cost costUpdate = (Cost) recordUpdate;
		if (! cost.getRecordUnit().equals(costUpdate.getRecordUnit())) {
			cost.setRecordUnit(costUpdate.getRecordUnit());
			logUpdate("recordUnit");
			updated = true;
		}

		TreeMap<Currency.Unit, Double> map = cost.getValues();
		TreeMap<Currency.Unit, Double> mapUpdate = costUpdate.getValues();

		// Check which Currency units are being removed
		HashSet<Currency.Unit> removeSet = new HashSet<>();
		for (Currency.Unit u: map.keySet()) {
			if (! mapUpdate.containsKey(u)) {
				removeSet.add(u);
				logUpdate("currencyUnit " + u.toString());
				updated = true;
			}
		}
		for (Currency.Unit u: removeSet) {
			map.remove(u);
		}

		// Check which units are being added
		for (Currency.Unit u: mapUpdate.keySet()) {
			if (! map.containsKey(u)) {
				map.put(u, costUpdate.getValues().get(u));
				logUpdate("currencyUnit "+ u.toString());
				updated = true;
			}
		}

		// Check which units are being altered
		for (Currency.Unit u: map.keySet()) {
			if (map.get(u) != mapUpdate.get(u)) {
				map.put(u, mapUpdate.get(u));
				logUpdate("currencyUnit "+ u.toString());
				updated = true;
			}
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child objects referenced in Cost, so nothing to delete in this scope
		return updated;
	}
}
