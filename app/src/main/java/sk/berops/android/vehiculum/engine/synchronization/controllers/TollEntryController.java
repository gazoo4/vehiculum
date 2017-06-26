package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.TollEntry;

/**
 * @author Bernard Halas
 * @date 6/23/17
 */

public class TollEntryController extends EntryController {
	private TollEntry tollEntry;

	public TollEntryController(TollEntry tollEntry) {
		super(tollEntry);
		this.tollEntry = tollEntry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from TollEntry object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof TollEntry)) {
			logFailure(recordUpdate);
			return updated;
		}

		TollEntry entryUpdate = (TollEntry) recordUpdate;

		if (! tollEntry.getType().equals(entryUpdate.getType())) {
			tollEntry.setType(entryUpdate.getType());
			logUpdate("type");
			updated = true;
		}

		// There are no fields defined in TollEntry, so nothing to update as of now
		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from TollEntry object
		return updated;
	}
}
