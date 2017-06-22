package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Garage;
import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 5/26/17
 */

public class GarageController extends RecordController {
	public static final String LOG_TAG = "GarageController";

	private Garage garage;

	public GarageController(Garage garage) {
		super(garage);
		this.garage = garage;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		if (!(child instanceof Car)) {
			logFailure(child);
			return updated;
		}

		logUpdate("adding car");
		garage.getCars().add((Car) child);
		updated = true;

		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof Garage)) {
			logFailure(recordUpdate);
			return updated;
		}

		Garage garageUpdate = (Garage) recordUpdate;
		if (garage.getActiveCarId() != garageUpdate.getActiveCarId()) {
			garage.setActiveCarId(garageUpdate.getActiveCarId());
			logUpdate("activeCarID");
			updated = true;
		}

		if (!garage.getRootTag().equals(garageUpdate.getRootTag())) {
			garage.setRootTag(garageUpdate.getRootTag());
			logUpdate("rootTag");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		Record forDeletion = null;
		for (Record r: garage.getCars()) {
			if (r.getUuid().equals(deleteUUID)) {
				forDeletion = r;
				break;
			}
		}

		if (forDeletion != null) {
			garage.getCars().remove(forDeletion);
			logUpdate("deleted car");
			updated = true;
		} else {
			logFailure(deleteUUID);
		}

		return updated;
	}
}