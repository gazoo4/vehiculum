package sk.berops.android.vehiculum.engine.synchronization.controllers;

import android.util.Log;

import java.util.LinkedList;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.synchronization.DataChangeSet;
import sk.berops.android.vehiculum.dataModel.synchronization.DataCreate;
import sk.berops.android.vehiculum.dataModel.synchronization.DataDelete;
import sk.berops.android.vehiculum.dataModel.synchronization.DataUpdate;
import sk.berops.android.vehiculum.dataModel.synchronization.DatasetChangeItem;
import sk.berops.android.vehiculum.dataModel.synchronization.DatasetUpdatable;
import sk.berops.android.vehiculum.gui.MainActivity;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class MainController implements DatasetUpdatable {
	private static final String LOG_TAG = "MainController";
	private static MainController instance;
	private static Object mutex = new Object();
	private static LinkedList<DatasetChangeItem> incomingUpdates;
	private static LinkedList<DatasetChangeItem> outgoingUpdates;

	public static MainController getInstance() {
		if (instance == null) {
			synchronized(mutex) {
				if (instance == null) {
					instance = new MainController();
				}
			}
		}

		return instance;
	}

	private MainController() {
		incomingUpdates = new LinkedList<>();
		outgoingUpdates = new LinkedList<>();
	}

	public void initMainActivity() {
		processIncomingUpdates();
		// TODO
		// https://stackoverflow.com/questions/11508613/how-does-push-notification-technology-work-on-android
	}

	public synchronized void processIncomingUpdates() {
		if (incomingUpdates.size() > 0) {
			// We need to process the incoming updates to the dataset
			LinkedList<DatasetChangeItem> successfulUpdates = new LinkedList<>();
			for (DatasetChangeItem change : incomingUpdates) {
				try {
					if (updateLocalDataset(change)) {
						// We can't alter the list we crawl through
						successfulUpdates.add(change);
					}
				} catch (SynchronizationException ex) {
					Log.e(LOG_TAG, "Data synchronization failed for "+ change.getBaseRecordUUID().toString());
				}
			}

			// Remove the successfully processed updates from the incoming queue
			for (DatasetChangeItem change : successfulUpdates) {
				incomingUpdates.remove(change);
			}
		}
	}

	private boolean updateLocalDataset(DatasetChangeItem change) throws SynchronizationException {
		Record record = getRecordByUUID(change.getBaseRecordUUID());
		return record.updateBy(change);
	}

	private Record getRecordByUUID(UUID uuid) {
		return MainActivity.garage.getRecordByUUID(uuid);
	}

	@Override
	public void enqueueUpdates(DataChangeSet changeset) {
		incomingUpdates.addAll(changeset.getChanges());
	}

	@Override
	public void enqueueUpdate(DatasetChangeItem update) {
		incomingUpdates.add(update);
	}
}