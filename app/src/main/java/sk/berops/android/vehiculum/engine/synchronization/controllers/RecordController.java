package sk.berops.android.vehiculum.engine.synchronization.controllers;

import android.util.Log;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.synchronization.DataCreate;
import sk.berops.android.vehiculum.dataModel.synchronization.DataDelete;
import sk.berops.android.vehiculum.dataModel.synchronization.DataUpdate;
import sk.berops.android.vehiculum.dataModel.synchronization.DatasetChangeItem;

/**
 * @author Bernard Halas
 * @date 5/25/17
 */

public class RecordController {
	public static final String LOG_TAG = "RecordController";

	private Record record;

	public RecordController(Record record) {
		this.record = record;
	}

	/**
	 * Method used for updating the associated Record object. This method identifies the type of update
	 * and calls the appropriate method for handling the update (whether it's creation, modification or
	 * deletion of the object).
	 * @param change that needs to be applied on the object
	 * @return true if update was successful, false otherwise
	 * @throws SynchronizationException in case the object update failed for technical reason
	 */
	public boolean processUpdate(DatasetChangeItem change) throws SynchronizationException {
		// Check for a potential ClassCastException
		if (!change.getChangeType().getAClass().isInstance(change)) {
			throw new SynchronizationException("Exception from DatasetChangeItem class-casting: "+ change.toString());
		}

		switch (change.getChangeType()) {
			case CREATE:
				DataCreate createRequest = (DataCreate) change;
				return createRecord(createRequest.getNewRecord());
			case UPDATE:
				DataUpdate updateRequest = (DataUpdate) change;
				return updateRecord(updateRequest.getUpdatedRecord());
			case DELETE:
				DataDelete deleteRequest = (DataDelete) change;
				return deleteRecord(deleteRequest.getUuid());
		}
		return false;
	}

	public boolean createRecord(Record child) throws SynchronizationException {
		// There's no complex object structure in Record object where a new object could be referenced
		return false;
	}

	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = false;
		if (!record.getComment().equals(recordUpdate.getComment())) {
			record.setComment(recordUpdate.getComment());
			logUpdate("comment");
			updated = true;
		}

		if (!record.getModifiedDate().equals(recordUpdate.getModifiedDate())) {
			record.setModifiedDate(recordUpdate.getModifiedDate());
			logUpdate("modifiedDate");
			updated = true;
		}

		return updated;
	}

	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		// There's no complex object structure in Record object where a referenced object needs to be deleted from
		return false;
	}

	public void logUpdate(String component) {
		Log.d(LOG_TAG, getClass().toString() + ": updated: "+ component);
	}

	public void logFailure(Record record) {
		logFailure(record.getUuid());
	}

	public void logFailure(UUID uuid) {
		Log.w(LOG_TAG, getClass().toString() + ": failure: "+ uuid.toString());
	}
}
