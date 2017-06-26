package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.OtherEntry;

/**
 * @author Bernard Halas
 * @date 6/23/17
 */

public class OtherEntryController extends EntryController {
	private OtherEntry otherEntry;

	public OtherEntryController(OtherEntry otherEntry) {
		super(otherEntry);
		this.otherEntry = otherEntry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from OtherEntry object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof OtherEntry)) {
			logFailure(recordUpdate);
			return updated;
		}

		// There are no fields defined in OtherEntry, so nothing to update as of now
		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from OtherEntry object
		return updated;
	}
}
