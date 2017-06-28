package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.MaintenanceEntry;
import sk.berops.android.vehiculum.dataModel.maintenance.ReplacementPart;


/**
 * @author Bernard Halas
 * @date 6/23/17
 */

public class MaintenanceEntryController extends EntryController {
	private MaintenanceEntry maintenanceEntry;

	public MaintenanceEntryController(MaintenanceEntry maintenanceEntry) {
		super(maintenanceEntry);
		this.maintenanceEntry = maintenanceEntry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		if (!(child instanceof ReplacementPart)) {
			logFailure(child);
			return updated;
		}

		maintenanceEntry.getParts().add((ReplacementPart) child);
		logUpdate("replacementPart");
		updated = true;

		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof MaintenanceEntry)) {
			logFailure(recordUpdate);
			return updated;
		}

		MaintenanceEntry entryUpdate = (MaintenanceEntry) recordUpdate;

		if (! maintenanceEntry.getType().equals(entryUpdate.getType())) {
			maintenanceEntry.setType(entryUpdate.getType());
			logUpdate("type");
			updated = true;
		}

		if (! maintenanceEntry.getLaborCost().equals(entryUpdate.getLaborCost())) {
			maintenanceEntry.setLaborCost(entryUpdate.getLaborCost());
			logUpdate("laborCost");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		Record forDeletion = null;
		for (Record r: maintenanceEntry.getParts()) {
			if (r.getUuid().equals(deleteUUID)) {
				forDeletion = r;
				break;
			}
		}

		if (forDeletion != null) {
			maintenanceEntry.getParts().remove(forDeletion);
			logUpdate("deleted entry");
			updated = true;
		} else {
			logFailure(deleteUUID);
		}

		return updated;
	}
}
