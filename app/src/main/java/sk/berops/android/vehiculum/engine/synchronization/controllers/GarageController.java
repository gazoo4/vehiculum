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
	public boolean createRecord(Record child) {
		boolean updated = false;
		updated = super.createRecord(child);

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
	public boolean updateRecord(Record recordUpdate) {
		boolean updated = false;
		updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof Garage)) {
			logFailure(recordUpdate);
			return updated;
		}

		Garage garageUpdate = (Garage) recordUpdate;
		if (garage.getActiveCarId() != garageUpdate.getActiveCarId()) {
			logUpdate("activeCarID");
			garage.setActiveCarId(garageUpdate.getActiveCarId());
		}

		if (!garage.getRootTag().equals(garageUpdate.getRootTag())) {
			logUpdate("rootTag");
			garage.setRootTag(garageUpdate.getRootTag());
		}

		return updated;
	}

	@Override
	public boolean deleteRecursively(UUID deleteUUID) {
		boolean updated = false;
		updated = super.deleteRecursively(deleteUUID);

		for (Record r: garage.getCars()) {
			if (r.getUuid().equals(deleteUUID)) {
				garage.getCars().remove(r);
				updated = true;
				break;
			}
		}

		return updated;
	}
}
