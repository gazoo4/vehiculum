package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.Entry;
import sk.berops.android.vehiculum.dataModel.expense.History;


/**
 * @author Bernard Halas
 * @date 6/21/17
 */

public class HistoryController extends RecordController {
	public static String LOG_TAG = "HistoryController";

	private History history;

	public HistoryController(History history) {
		super(history);
		this.history = history;
	}

	@Override
	public boolean createRecord(Record child)  throws SynchronizationException {
		boolean updated = super.createRecord(child);

		if (!(child instanceof Entry)) {
			logFailure(child);
			return updated;
		}

		logUpdate("addingEntry");
		history.getEntries().add((Entry) child);
		updated = true;

		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof History)) {
			logFailure(recordUpdate);
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		Record forDeletion = null;
		for (Record r: history.getEntries()) {
			if (r.getUuid().equals(deleteUUID)) {
				forDeletion = r;
				break;
			}
		}

		if (forDeletion != null) {
			history.getEntries().remove(forDeletion);
			logUpdate("deleted entry");
			updated = true;
		} else {
			logFailure(deleteUUID);
		}

		return updated;
	}
}
