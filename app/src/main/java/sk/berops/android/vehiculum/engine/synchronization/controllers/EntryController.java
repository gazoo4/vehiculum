package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.Entry;

/**
 * @author Bernard Halas
 * @date 6/21/17
 */

public class EntryController extends RecordController {
	public static String LOG_TAG = "EntryController";

	private Entry entry;

	public EntryController(Entry entry) {
		super(entry);
		this.entry = entry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There's no child record attached to Entry
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof Entry)) {
			logFailure(recordUpdate);
			return updated;
		}

		Entry entryUpdate = (Entry) recordUpdate;

		ArrayList<UUID> tags = entry.getTagUuids();
		ArrayList<UUID> tagsUpdate = entryUpdate.getTagUuids();
		int size = tags.size();
		int sizeUpdate = tagsUpdate.size();

		// Any UUIDs being deleted?
		tags.retainAll(tagsUpdate);
		if (tags.size() != size) {
			size = tags.size();
			logUpdate("tag");
			updated = true;
		}

		// Any UUIDs being added?
		UUID tagUpdate;
		for (int i = 0; i < sizeUpdate; i++) {
			tagUpdate = tagsUpdate.get(i);
			if (! tags.contains(tagUpdate)) {
				tags.add(i, tagUpdate);
				logUpdate("tag");
				updated = true;
			}
		}

		if (!(entry.getMileage() == entryUpdate.getMileage())) {
			entry.setMileage(entryUpdate.getMileage());
			logUpdate("mileage");
			updated = true;
		}
		if (!(entry.getExpenseType() == entryUpdate.getExpenseType())) {
			entry.setExpenseType(entryUpdate.getExpenseType());
			logUpdate("expenseType");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child objects referenced in Entry, so nothing to delete in this scope
		return updated;
	}
}
