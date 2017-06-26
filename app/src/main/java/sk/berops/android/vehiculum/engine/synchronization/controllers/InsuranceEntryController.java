package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.InsuranceEntry;

/**
 * @author Bernard Halas
 * @date 6/23/17
 */

public class InsuranceEntryController extends EntryController {
	private InsuranceEntry insuranceEntry;

	public InsuranceEntryController(InsuranceEntry insuranceEntry) {
		super(insuranceEntry);
		this.insuranceEntry = insuranceEntry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from InsuranceEntry object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof InsuranceEntry)) {
			logFailure(recordUpdate);
			return updated;
		}

		InsuranceEntry entryUpdate = (InsuranceEntry) recordUpdate;
		if (! insuranceEntry.getType().equals(entryUpdate.getType())) {
			insuranceEntry.setType(entryUpdate.getType());
			logUpdate("type");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from InsuranceEntry object
		return updated;
	}
}