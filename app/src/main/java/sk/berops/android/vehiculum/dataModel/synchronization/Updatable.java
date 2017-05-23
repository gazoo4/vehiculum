package sk.berops.android.vehiculum.dataModel.synchronization;

import sk.berops.android.vehiculum.dataModel.synchronization.DatasetChange;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public interface Updatable {
	void enqueueUpdate(DatasetChange update);
}
