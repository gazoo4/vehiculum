package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Axle;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.maintenance.TyreConfigurationScheme;

/**
 * @author Bernard Halas
 * @date 6/26/17
 */

public class TyreConfigurationSchemeController extends RecordController {
	private TyreConfigurationScheme scheme;

	public TyreConfigurationSchemeController(TyreConfigurationScheme scheme) {
		super(scheme);
		this.scheme = scheme;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		if (! (child instanceof Axle)) {
			logFailure(child);
			return updated;
		}

		scheme.getAxles().add((Axle) child);
		logUpdate("adding Axle");
		updated = true;

		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		// There are no fields to be modified in TyreConfigurationScheme
		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		Record forDeletion = null;
		for (Record r: scheme.getAxles()) {
			if (r.getUuid().equals(deleteUUID)) {
				forDeletion = r;
				break;
			}
		}

		if (forDeletion != null) {
			scheme.getAxles().remove(forDeletion);
			logUpdate("deleted boughtTyre");
			updated = true;
		} else {
			logFailure(deleteUUID);
		}

		return updated;
	}
}
