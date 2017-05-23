package sk.berops.android.vehiculum.gui;

import java.util.LinkedList;

import sk.berops.android.vehiculum.dataModel.synchronization.DatasetChange;
import sk.berops.android.vehiculum.dataModel.synchronization.DatasetUpdatable;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class MainController implements DatasetUpdatable {
	private static MainController instance;
	private static Object mutex = new Object();
	private static LinkedList<DatasetChange> incomingUpdates;
	private static LinkedList<DatasetChange> outgoingUpdates;

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

	public void processIncomingUpdates() {
		if (incomingUpdates.size() > 0) {
			// We need to process the incoming updates to the dataset
			LinkedList<DatasetChange> successfulUpdates = new LinkedList<>();
			for (DatasetChange change: incomingUpdates) {
				if (updateLocalDataset(change)) {
					successfulUpdates.add(change);
				}
			}

			// Remove processed updates from the incoming queue
			for (DatasetChange change: successfulUpdates) {
				incomingUpdates.remove(change);
			}
		}
	}

	private boolean updateLocalDataset(DatasetChange change) {
		// TODO
		// return true if local dataset was successfully updated
		return false;
	}

	@Override
	public void enqueueUpdate(DatasetChange update) {
		incomingUpdates.add(update);
	}
}
