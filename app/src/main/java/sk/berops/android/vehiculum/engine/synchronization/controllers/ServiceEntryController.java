package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.ServiceEntry;

/**
 * @author Bernard Halas
 * @date 6/23/17
 */

public class ServiceEntryController extends EntryController {
	private ServiceEntry serviceEntry;

	public ServiceEntryController(ServiceEntry serviceEntry) {
		super(serviceEntry);
		this.serviceEntry = serviceEntry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from ServiceEntry object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof ServiceEntry)) {
			logFailure(recordUpdate);
			return updated;
		}

		ServiceEntry entryUpdate = (ServiceEntry) recordUpdate;

		if (serviceEntry.getType().equals(entryUpdate.getType())) {
			serviceEntry.setType(entryUpdate.getType());
			logUpdate("type");
			updated = true;
		}

		// There are no fields defined in ServiceEntry, so nothing to update as of now
		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from ServiceEntry object
		return updated;
	}
}
