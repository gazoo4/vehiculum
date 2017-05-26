package sk.berops.android.vehiculum.dataModel.synchronization;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public interface DatasetUpdatable {
	void enqueueUpdates(DataChangeSet changeset);
	void enqueueUpdate(DatasetChangeItem update);
}
