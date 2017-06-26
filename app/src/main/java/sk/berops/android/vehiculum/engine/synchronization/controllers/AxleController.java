package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Axle;
import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 6/26/17
 */

public class AxleController extends RecordController {
	private Axle axle;

	public AxleController(Axle axle) {
		super(axle);
		this.axle = axle;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no references to other objects from Axle
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (! (recordUpdate instanceof Axle)) {
			logFailure(recordUpdate);
			return updated;
		}

		Axle axleUpdate = (Axle) recordUpdate;

		if (! axle.getType().equals(axleUpdate.getType())) {
			axle.setType(axleUpdate.getType());
			logUpdate("type");
			updated = true;
		}

		if (axle.isDrivable() != axleUpdate.isDrivable()) {
			axle.setDrivable(axleUpdate.isDrivable());
			logUpdate("drivable");
			updated = true;
		}

		TreeMap<Integer, UUID> map = axle.getTyreIDs();
		TreeMap<Integer, UUID> mapUpdate = axleUpdate.getTyreIDs();

		// Any object being removed from the map?
		for (Integer key: map.keySet()) {
			if (! mapUpdate.containsKey(key)) {
				map.remove(key);
				logUpdate("tyre");
				updated = true;
			}
		}

		// Any object being added to the map?
		for (Integer key: mapUpdate.keySet()) {
			if (! map.containsKey(key)) {
				map.put(key, mapUpdate.get(key));
				logUpdate("tyre");
				updated = true;
			}
		}

		// Anyting being altered in the map?
		for (Integer key: mapUpdate.keySet()) {
			if (! map.get(key).equals(mapUpdate.get(key))) {
				map.put(key, mapUpdate.get(key));
				logUpdate("tyre");
				updated = true;
			}
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no references to other objects from Axle
		return updated;
	}
}
